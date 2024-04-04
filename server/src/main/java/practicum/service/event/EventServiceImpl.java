package practicum.service.event;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import practicum.client.StatsClient;
import practicum.dto.events.EventDto;
import practicum.dto.events.EventDtoIn;
import practicum.dto.events.EventDtoLight;
import practicum.dto.mappers.event.EventLightMapper;
import practicum.dto.mappers.event.EventMapper;
import practicum.dto.mappers.location.LocationMapper;
import practicum.dto.mappers.request.RequestMapper;
import practicum.dto.parameters.EventParam;
import practicum.dto.parameters.EventParamUpdateAdmin;
import practicum.dto.requests.RequestChangingStatusDto;
import practicum.dto.requests.RequestDto;
import practicum.dto.requests.RequestUpdateDto;
import practicum.exceptions.BadRequestException;
import practicum.exceptions.ConflictException;
import practicum.exceptions.NotFoundException;
import practicum.model.*;
import practicum.repository.*;
import stats.StatsDto;
import stats.StatsDtoOut;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;
    private final StatsClient statsClient;
    private final ObjectMapper objectMapper;
    private final EventMapper eventMapper;
    private final EventLightMapper eventLightMapper;
    private final LocationMapper locationMapper;

    @Override
    public List<EventDto> getEventsFromAdmin(List<Long> users, List<String> states, List<Long> categories,
                                             String start, String end, int from, int size) {
        Pageable pageable = PageRequest.of(from, size);
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;
        List<Event> events;
        if (start != null) {
            startTime = LocalDateTime.parse(start, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        if (end != null) {
            endTime = LocalDateTime.parse(end, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        if (start == null && end == null) {
            events = eventRepository.findAllEventsByAdminFilter(
                    users,
                    states,
                    categories,
                    pageable
            );
        } else {
            events = eventRepository.findAllEventsByAdminFilter(
                    users,
                    states,
                    categories,
                    startTime,
                    endTime,
                    pageable
            );
        }

        return events.stream()
                .map(eventMapper::toDto)
                .peek(eventDto -> eventDto.setConfirmedRequests(
                        requestRepository.countByEventIdAndStatus(eventDto.getId(), "CONFIRMED")))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventDto updateEventFromAdmin(Long eventId, EventParamUpdateAdmin eventParamUpdateAdmin) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие не найдено или недоступно"));

        updatingEvent(event, eventParamUpdateAdmin);

        if (!event.getState().equals("PENDING")) {
            throw new ConflictException("Событие не удовлетворяет правилам редактирования");
        }
        if (eventParamUpdateAdmin.getStateAction() != null) {

            switch (eventParamUpdateAdmin.getStateAction()) {

                case "REJECT_EVENT":
                    event.setState("CANCELED");

                case "PUBLISH_EVENT": {
                    event.setState("PUBLISHED");
                    event.setPublishedOn(LocalDateTime.now());
                }
            }
        }
        eventRepository.save(event);
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
    @Transactional
    public EventDto createEvent(Long userId, EventDtoIn eventDtoIn) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден или недоступен"));
        Category category = categoryRepository
                .findById(eventDtoIn.getCategory()).orElseThrow(() -> new NotFoundException("Категория не найдена"));
        Location location = locationMapper.fromDto(eventDtoIn.getLocation());
        Event event = addNewEvent(eventDtoIn, category, locationRepository.save(location));
        event.setInitiator(user);
        event.setCreatedOn(LocalDateTime.now());
        event.setState("PENDING");
        eventRepository.save(event);

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
    @Transactional
    public EventDto changeEvent(Long userId, Long eventId, EventParamUpdateAdmin eventParamUpdateAdmin) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден или недоступен"));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие не найдено или недоступно"));

        if (event.getInitiator().getId() != user.getId()) {
            throw new ConflictException("Пользователь не является инициатором события");
        }

        if (event.getState().equals("PUBLISHED") && (eventParamUpdateAdmin.getEventDate() != null)) {
            throw new ConflictException("Попытка изменения опубликованного события");
        }

        if (eventParamUpdateAdmin.getEventDate() != null &&
                eventParamUpdateAdmin.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new BadRequestException("Неверная дата события");
        }

        if (eventParamUpdateAdmin.getStateAction() != null) {

            if (eventParamUpdateAdmin.getStateAction().equals("CANCEL_REVIEW")) {
                event.setState("CANCELED");
            }
            if (eventParamUpdateAdmin.getStateAction().equals("SEND_TO_REVIEW")) {
                event.setState("PENDING");
            }
        }

        return eventMapper.toDto(event);
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
    @Transactional
    public RequestChangingStatusDto changeRequestStatus(Long userId, Long eventId, RequestUpdateDto requestUpdateDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден или недоступен"));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие не найдено или недоступно"));

        if (!event.isRequestModeration() || event.getParticipantLimit() == 0) {
            throw new ConflictException("Событие не требует подтверждения запросов");
        }

        if (event.getInitiator() != user) {
            throw new ConflictException("Пользователь не является инициатором события");
        }

        String status = requestUpdateDto.getStatus();

        int confirmCount = requestRepository.countByEventIdAndStatus(eventId, status);
        if (event.getParticipantLimit() <= confirmCount) {
            throw new ConflictException("Достигнут лимит одобренных заявок");
        }

        List<Request> requests = requestRepository.findAllById(requestUpdateDto.getRequestIds());

        for (Request r : requests) {
            if (r.getStatus().equals("CONFIRMED") && status.equals("REJECTED")) {
                throw new ConflictException("Заявка на участие уже принята");
            }
            r.setStatus(status);
            requestRepository.save(r);
        }

        List<RequestDto> confirmedRequests = requestRepository.findByStatusOrderByIdDesc("CONFIRMED").stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
        List<RequestDto> rejectedRequests = requestRepository.findByStatusOrderByIdDesc("REJECTED").stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());

        return new RequestChangingStatusDto(confirmedRequests, rejectedRequests);
    }

    @Override
    public List<EventDtoLight> getAllEvent(EventParam eventParam, HttpServletRequest request) {
        Pageable pageable = PageRequest.of(eventParam.getFrom(), eventParam.getSize());
        LocalDateTime start = null;
        LocalDateTime end = null;
        List<Event> events;

        statsClient.saveStats(new StatsDto("ewm-main-service",
                request.getRequestURI(),
                request.getRemoteAddr(),
                LocalDateTime.now()));

        if (eventParam.getRangeStart() != null) {
            start = LocalDateTime.parse(eventParam.getRangeStart(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        if (eventParam.getRangeEnd() != null) {
            end = LocalDateTime.parse(eventParam.getRangeEnd(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        if (start != null && start.isAfter(end)) {
            throw new BadRequestException("RangeStart должен быть раньше чем rangeEnd");
        }
        if (start == null && end == null) {
            events = eventRepository.findAllEventsByFilter(eventParam.getText(),
                    eventParam.getCategories(),
                    eventParam.getPaid(),
                    eventParam.getOnlyAvailable(),
                    pageable);
        } else {
            events = eventRepository.findAllEventsByFilter(eventParam.getText(),
                    eventParam.getCategories(),
                    eventParam.getPaid(),
                    start,
                    end,
                    eventParam.getOnlyAvailable(),
                    pageable);
        }

        List<EventDtoLight> eventsDto = events.stream()
                .map(eventLightMapper::toDto)
                .peek(eventDto -> eventDto.setConfirmedRequests(
                        requestRepository.countByEventIdAndStatus(eventDto.getId(), "CONFIRMED")))
                .collect(Collectors.toList());

        Map<Long, Integer> viewStatsMap = getViewsEvents(events);

        for (EventDtoLight e : eventsDto) {
            Integer views = viewStatsMap.getOrDefault(e.getId(), 0);
            e.setViews(views);
        }

        return eventsDto;
    }

    @Override
    public EventDto getEventById(Long eventId, HttpServletRequest request) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие не найдено или недоступно"));

        statsClient.saveStats(new StatsDto("ewm-main-service",
                request.getRequestURI(),
                request.getRemoteAddr(),
                LocalDateTime.now()));

        if (!event.getState().equals("PUBLISHED")) {
            throw new NotFoundException("Событие не опубликовано");
        }

        EventDto eventDto = eventMapper.toDto(event);
        eventDto.setViews(getViewsEvents(List.of(event)).getOrDefault(event.getId(), 0));
        return eventDto;
    }

    private Event addNewEvent(EventDtoIn eventDtoIn, Category category, Location location) {
        if (eventDtoIn.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new BadRequestException("Неверная дата события");
        }
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
            if (updateEvent.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
                throw new BadRequestException("Некорректная дата");
            }
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
            event.setRequestModeration(updateEvent.getRequestModeration());
        }
        if (updateEvent.getTitle() != null) {
            event.setTitle(updateEvent.getTitle());
        }
        return event;
    }

    private Map<Long, Integer> getViewsEvents(List<Event> events) {
        List<String> uris = events.stream()
                .map(event -> String.format("/events/%s", event.getId()))
                .collect(Collectors.toList());

        List<LocalDateTime> startDates = events.stream()
                .map(Event::getCreatedOn)
                .collect(Collectors.toList());
        LocalDateTime earliestDate = startDates.stream()
                .min(LocalDateTime::compareTo)
                .orElse(null);
        Map<Long, Integer> viewStatsMap = new HashMap<>();

        if (earliestDate != null) {
            ResponseEntity<Object> response = statsClient.getStats(earliestDate, LocalDateTime.now(),
                    uris, true);

            List<StatsDtoOut> viewStatsList = objectMapper.convertValue(response.getBody(), new TypeReference<>() {
            });

            viewStatsMap = viewStatsList.stream()
                    .filter(statsDtoOut -> statsDtoOut.getUri().startsWith("/events/"))
                    .collect(Collectors.toMap(
                            statsDto -> Long.parseLong(statsDto.getUri().substring("/events/".length())),
                            StatsDtoOut::getHits));
        }
        return viewStatsMap;
    }
}
