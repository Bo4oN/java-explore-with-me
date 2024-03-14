package practicum.service.event;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import practicum.dto.events.*;
import practicum.dto.mappers.EventMapper;
import practicum.dto.mappers.LocationMapper;
import practicum.dto.mappers.RequestMapper;
import practicum.dto.requests.RequestChangingStatusDto;
import practicum.dto.requests.RequestDto;
import practicum.dto.requests.RequestUpdateDto;
import practicum.exceptions.ConflictException;
import practicum.exceptions.ForbiddenException;
import practicum.exceptions.NotFoundException;
import practicum.model.*;
import practicum.model.enums.Status;
import practicum.repository.*;
import practicum.service.stats.StatServiceImpl;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;
    private final StatServiceImpl statService;

    @Override
    public List<EventDto> getEventsFromAdmin(EventParamAdmin eventParamsAdmin) {
        Pageable pageable = PageRequest.of(eventParamsAdmin.getFrom(), eventParamsAdmin.getSize());
        List<Event> events = eventRepository.findAllEventsByAdminFilter(
                eventParamsAdmin.getUsers(),
                eventParamsAdmin.getStates(),
                eventParamsAdmin.getCategories(),
                eventParamsAdmin.getRangeStart(),
                eventParamsAdmin.getRangeEnd(),
                pageable
        );
        return events.stream()
                .map(EventMapper::toEventDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventDto updateEventFromAdmin(Long eventId, EventParamUpdateAdmin eventParamUpdateAdmin) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие не найдено или недоступно"));

        updatingEvent(event, eventParamUpdateAdmin);

        if (!event.getState().equals("PENDING")) {
            throw new ForbiddenException("Событие не удовлетворяет правилам редактирования");
        }
        switch (eventParamUpdateAdmin.getState()) {
            case PENDING:
                break;

            case CANCELED:
                event.setState("CANCELED");

            case PUBLISHED: {
                event.setState("PUBLISHED");
                event.setPublishedDate(LocalDateTime.now());
            }
        }
        return EventMapper.toEventDto(event);
    }

    @Override
    public List<EventDtoLight> getEventsCurrentUser(Long userId, Integer from, Integer size) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден или недоступен"));
        Pageable pageable = PageRequest.of(from, size);
        List<Event> events = eventRepository.findAllByInitiatorId(userId, pageable);

        return events.stream()
                .map(EventMapper::toEventDtoLight)
                .collect(Collectors.toList());
    }

    @Override
    public EventDto createEvent(Long userId, EventDtoIn eventDtoIn) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден или недоступен"));
        Category category = categoryRepository
                .findById(eventDtoIn.getCategory()).orElseThrow(() -> new NotFoundException("Категория не найдена"));
        Location location = LocationMapper.toLocation(eventDtoIn.getLocationDto());
        Event event = EventMapper.toEvent(eventDtoIn, category, locationRepository.save(location));
        event.setInitiator(user);
        event.setCreatedDate(LocalDateTime.now());
        event.setState("PENDING");

        return EventMapper.toEventDto(event);
    }

    @Override
    public EventDto getEventByIdCurrentUser(Long userId, Long eventId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден или недоступен"));
        Event event = eventRepository.findByInitiatorIdAndId(userId, eventId)
                .orElseThrow(() -> new NotFoundException("Событие не найдено или недоступно"));
        return EventMapper.toEventDto(event);
    }

    @Override
    public EventDto changeEvent(Long userId, Long eventId, EventParamUpdateAdmin eventParamUpdateAdmin) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден или недоступен"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие не найдено или недоступно"));
        if (event.getInitiator() != user) {
            throw new ConflictException("Пользователь не является инициатором события");
        }
        if (eventParamUpdateAdmin.getState() != null) {
            event.setState(eventParamUpdateAdmin.getState().toString());
        }
        updatingEvent(event, eventParamUpdateAdmin);
        return EventMapper.toEventDto(eventRepository.save(event));
    }

    @Override
    public List<RequestDto> getRequestsParticipantEvent(Long userId, Long eventId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден или недоступен"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие не найдено или недоступно"));
        if (event.getInitiator() != user) {
            throw new ConflictException("Пользователь не является инициатором события");
        }
        return requestRepository.findByEventId(eventId).stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public RequestChangingStatusDto changeRequestStatus(Long userId, Long eventId, RequestUpdateDto requestUpdateDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден или недоступен"));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие не найдено или недоступно"));

        if (event.getParticipantLimit() != 0 &&
                (event.getParticipantLimit() - event.getConfirm()) < requestUpdateDto.getRequestsId().size()) {
            throw new ConflictException("Достигнут лимит одобренных заявок");
        }
        if (event.getInitiator() != user) {
            throw new ConflictException("Пользователь не является инициатором события");
        }

        Status status = requestUpdateDto.getStatus();
        List<Request> requests = requestRepository.findAllById(requestUpdateDto.getRequestsId());

        for (Request r : requests) {
            r.setStatus(status);
            requestRepository.save(r);
        }

        List<RequestDto> confirmedRequests = requestRepository.findByStatus(Status.CONFIRMED).stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
        List<RequestDto> rejectedRequests = requestRepository.findByStatus(Status.REJECTED).stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());

        return new RequestChangingStatusDto(confirmedRequests, rejectedRequests);
    }

    @Override
    public List<EventDtoLight> getAllEvent(EventParam searchEventParams, HttpServletRequest request) {
        Pageable pageable = PageRequest.of(searchEventParams.getFrom(), searchEventParams.getSize());
        List<Event> events = eventRepository.findAllEventsByFilter(searchEventParams.getText(),
                searchEventParams.getCategories(),
                searchEventParams.getPaid(),
                searchEventParams.getRangeStart(),
                searchEventParams.getRangeEnd(),
                searchEventParams.getOnlyAvailable(),
                searchEventParams.getSort(),
                pageable);

        statService.addHits(request);

        return events.stream()
                .map(EventMapper::toEventDtoLight)
                .collect(Collectors.toList());
    }

    @Override
    public EventDto getEventById(Long eventId, HttpServletRequest request) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие не найдено или недоступно"));

        if (!event.getState().equals("PUBLISHED")) {
            throw new NotFoundException("Событие не опубликовано");
        }

        statService.toView(Collections.emptyList());
        statService.addHits(request);

        return EventMapper.toEventDto(event);
    }

    private Event updatingEvent(Event event, EventParamUpdateAdmin updateEvent) {
        if (updateEvent.getAnnotation() != null) {
            event.setAnnotation(updateEvent.getAnnotation());
        }
        if (updateEvent.getCategory() != null) {
            Category category = categoryRepository.findById(updateEvent.getCategory())
                    .orElseThrow(() -> new NotFoundException("Категория не найдена"));
            event.setCategory(category);
        }
        if (updateEvent.getDescription() != null) {
            event.setDescription(updateEvent.getDescription());
        }
        if (updateEvent.getEventDate() != null) {
            event.setEventDate(updateEvent.getEventDate());
        }
        if (updateEvent.getLocationDto() != null) {
            event.setLocation(LocationMapper.toLocation(updateEvent.getLocationDto()));
        }
        if (updateEvent.getPaid() != null) {
            event.setPaid(updateEvent.getPaid());
        }
        if (updateEvent.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEvent.getParticipantLimit());
        }
        if (updateEvent.getRequestModeration() != null) {
            event.setModeration(updateEvent.getRequestModeration());
        }
        if (updateEvent.getTitle() != null) {
            event.setTitle(updateEvent.getTitle());
        }
        return event;
    }
}
