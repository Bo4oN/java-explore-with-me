package practicum.service.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import practicum.dto.mappers.request.RequestMapper;
import practicum.dto.requests.RequestDto;
import practicum.exceptions.ConflictException;
import practicum.exceptions.NotFoundException;
import practicum.model.Event;
import practicum.model.Request;
import practicum.model.User;
import practicum.model.enums.RequestStatus;
import practicum.repository.EventRepository;
import practicum.repository.RequestRepository;
import practicum.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final RequestMapper requestMapper;

    @Override
    public List<RequestDto> getUsersRequests(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден или недоступен"));
        return requestRepository.findByRequester(user).stream()
                .map(requestMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public RequestDto sendRequest(long userId, long eventId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден или недоступен"));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие не найдено или недоступно"));

        Request request = creatingRequest(event, user, LocalDateTime.now());
        return requestMapper.toDto(requestRepository.save(request));
    }

    private Request creatingRequest(Event event, User user, LocalDateTime created) {
        Request request;
        if (event.getParticipantLimit() != 0 && event.getParticipantLimit() <= event.getConfirm()) {
            throw new ConflictException("Нарушение целостности данных");
        }
        if (event.getInitiator().getId() == user.getId()) {
            throw new ConflictException("Нарушение целостности данных");
        }
        if (!event.isModeration()) {
            request = new Request(created, event, user, RequestStatus.CONFIRMED);
        } else {
            request = new Request(created, event, user, RequestStatus.PENDING);
        }
        return request;
    }

    @Override
    public RequestDto cancelledRequest(long userId, long requestId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден или недоступен"));
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Запрос не найден или недоступен"));
        if (request.getStatus() == RequestStatus.PENDING) {
            request.setStatus(RequestStatus.CANCELED);
        }
        return requestMapper.toDto(requestRepository.save(request));
    }
}