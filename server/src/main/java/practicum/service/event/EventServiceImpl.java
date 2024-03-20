package practicum.service.event;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import practicum.client.StatsClient;
import practicum.dto.events.*;
import practicum.dto.mappers.event.EventLightMapper;
import practicum.dto.mappers.event.EventMapper;
import practicum.dto.mappers.location.LocationMapper;
import practicum.dto.mappers.request.RequestMapper;
import practicum.dto.parameters.EventParam;
import practicum.dto.parameters.EventParamAdmin;
import practicum.dto.parameters.EventParamUpdateAdmin;
import practicum.dto.requests.RequestChangingStatusDto;
import practicum.dto.requests.RequestDto;
import practicum.dto.requests.RequestUpdateDto;
import practicum.exceptions.ConflictException;
import practicum.exceptions.ForbiddenException;
import practicum.exceptions.NotFoundException;
import practicum.model.*;
import practicum.model.enums.RequestStatus;
import practicum.repository.*;

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
    //private final StatServiceImpl statService;
    private final StatsClient statClient;
    private final EventMapper eventMapper;
    private final EventLightMapper eventLightMapper;
    private final LocationMapper locationMapper;

    @Override
    public List<EventDto> getEventsFromAdmin(EventParamAdmin eventParamAdmin) {
        Pageable pageable = PageRequest.of(eventParamAdmin.getFrom(), eventParamAdmin.getSize());
        //List<Event> events = eventRepository.findAllEventsByAdminFilter(
        //        eventParamAdmin.getUsers(),
        //        eventParamAdmin.getStates(),
        //        eventParamAdmin.getCategories(),
        //        eventParamAdmin.getRangeStart(),
        //        eventParamAdmin.getRangeEnd(),
        //        pageable
        //);
        List<Event> events = eventRepository.findAll();
        return events.stream()
                .map(eventMapper::toDto)
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
        return eventMapper.toDto(event);
    }

    @Override
    public List<EventDtoLight> getEventsCurrentUser(Long userId, Integer from, Integer size) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден или недоступен"));
        Pageable pageable = PageRequest.of(from, size);
        List<Event> events = eventRepository.findAllByInitiatorId(userId, pageable);
        if (events == null) {
            return Collections.emptyList();
        } else {
            return events.stream()
                    .map(eventLightMapper::toDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public EventDto createEvent(Long userId, EventDtoIn eventDtoIn) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден или недоступен"));
        Category category = categoryRepository
                .findById(eventDtoIn.getCategory()).orElseThrow(() -> new NotFoundException("Категория не найдена"));
        Location location = locationMapper.fromDto(eventDtoIn.getLocationDto());
        Event event = addNewEvent(eventDtoIn, category, locationRepository.save(location));
        event.setInitiator(user);
        event.setCreatedDate(LocalDateTime.now());
        event.setState("PENDING");

        return eventMapper.toDto(event);
    }

    @Override
    public EventDto getEventByIdCurrentUser(Long userId, Long eventId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден или недоступен"));
        Event event = eventRepository.findByInitiatorIdAndId(userId, eventId)
                .orElseThrow(() -> new NotFoundException("Событие не найдено или недоступно"));
        return eventMapper.toDto(event);
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
        return eventMapper.toDto(eventRepository.save(event));
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

        RequestStatus status = requestUpdateDto.getStatus();
        List<Request> requests = requestRepository.findAllById(requestUpdateDto.getRequestsId());

        for (Request r : requests) {
            r.setStatus(status);
            requestRepository.save(r);
        }

        List<RequestDto> confirmedRequests = requestRepository.findByStatus(RequestStatus.CONFIRMED).stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
        List<RequestDto> rejectedRequests = requestRepository.findByStatus(RequestStatus.REJECTED).stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());

        return new RequestChangingStatusDto(confirmedRequests, rejectedRequests);
    }

    @Override
    public List<EventDtoLight> getAllEvent(EventParam searchEventParams, HttpServletRequest request) {
        Pageable pageable = PageRequest.of(searchEventParams.getFrom(), searchEventParams.getSize());
        //List<Event> events = eventRepository.findAllEventsByFilter(searchEventParams.getText(),
        //        searchEventParams.getCategories(),
        //        searchEventParams.getPaid(),
        //        searchEventParams.getRangeStart(),
        //        searchEventParams.getRangeEnd(),
        //        searchEventParams.getOnlyAvailable(),
        //        searchEventParams.getSort(),
        //        pageable);

        //statService.addHits(request);

        List<Event> events = Collections.emptyList();

        return events.stream()
                .map(eventLightMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventDto getEventById(Long eventId, HttpServletRequest request) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие не найдено или недоступно"));

        if (!event.getState().equals("PUBLISHED")) {
            throw new NotFoundException("Событие не опубликовано");
        }

        //statService.toView(Collections.emptyList());
        //statService.addHits(request);

        return eventMapper.toDto(event);
    }

    private Event addNewEvent(EventDtoIn eventDtoIn, Category category, Location location) {
        return new Event(eventDtoIn.getAnnotation(),
                            category,
                            eventDtoIn.getDescription(),
                            eventDtoIn.getEventDate(),
                            location,
                            eventDtoIn.isPaid(),
                            eventDtoIn.getParticipantLimit(),
                            eventDtoIn.isRequestModeration(),
                            eventDtoIn.getTitle()
                    );
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
            event.setLocation(locationMapper.fromDto(updateEvent.getLocationDto()));
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
