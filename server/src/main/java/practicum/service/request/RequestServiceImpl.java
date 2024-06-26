package practicum.service.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import practicum.dto.mappers.request.RequestMapper;
import practicum.dto.requests.RequestDto;
import practicum.exceptions.ConflictException;
import practicum.exceptions.NotFoundException;
import practicum.model.Event;
import practicum.model.Request;
import practicum.model.User;
import practicum.repository.EventRepository;
import practicum.repository.RequestRepository;
import practicum.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    public List<RequestDto> getUsersRequests(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден или недоступен"));
        return requestRepository.findByRequester(user).stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RequestDto sendRequest(long userId, long eventId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден или недоступен"));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие не найдено или недоступно"));

        if (requestRepository.existsByEventAndRequester(event, user)) {
            throw new ConflictException("Запрос на участие уже создан");
        }
        Request request = creatingRequest(event, user, LocalDateTime.now());
        return RequestMapper.toRequestDto(requestRepository.save(request));
    }

    private Request creatingRequest(Event event, User user, LocalDateTime created) {
        Request request;
        if (event.getParticipantLimit() != 0 &&
                event.getParticipantLimit() <= requestRepository.countByEventIdAndStatus(event.getId(), "CONFIRMED")) {
            throw new ConflictException("Нарушение целостности данных");
        }
        if (event.getInitiator().getId() == user.getId()) {
            throw new ConflictException("Нарушение целостности данных");
        }
        if (event.getPublishedOn() == null) {
            throw new ConflictException("Событие еще не опубликовано");
        }
        if (!event.isRequestModeration()) {
            request = new Request(created, event, user, "CONFIRMED");
        } else {
            request = new Request(created, event, user, "PENDING");
        }
        if (event.getParticipantLimit() == 0) {
            request.setStatus("CONFIRMED");
        }
        return request;
    }

    @Override
    @Transactional
    public RequestDto cancelledRequest(long userId, long requestId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден или недоступен"));
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Запрос не найден или недоступен"));
        if (request.getStatus().equals("PENDING")) {
            request.setStatus("CANCELED");
        }
        return RequestMapper.toRequestDto(requestRepository.save(request));
    }
}
