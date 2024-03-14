package practicum.controller.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import practicum.dto.events.EventDto;
import practicum.dto.events.EventDtoIn;
import practicum.dto.events.EventDtoLight;
import practicum.dto.events.EventParamUpdateAdmin;
import practicum.dto.requests.RequestChangingStatusDto;
import practicum.dto.requests.RequestDto;
import practicum.dto.requests.RequestUpdateDto;
import practicum.service.event.EventServiceImpl;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/users/{userId}/events")
public class EventPrivateController {
    private final EventServiceImpl service;

    @ResponseBody
    @GetMapping
    public List<EventDtoLight> getEventsCurrentUser(@PathVariable Long userId,
                                                    @RequestParam(value = "from", defaultValue = "0") int from,
                                                    @RequestParam(value = "size", defaultValue = "10") int size) {
        log.info("Запрос на получение всех событий добавленных пользователем, ID = {}, from = {}, size = {}",
                userId, from, size);
        return service.getEventsCurrentUser(userId, from, size);
    }

    @ResponseBody
    @PostMapping
    public EventDto createEvent(@PathVariable Long userId,
                                @RequestBody @Valid EventDtoIn eventDtoIn) {
        log.info("Запрос на создание события, пользовательский ID = {}, события ID = {}", userId, eventDtoIn);
        return service.createEvent(userId, eventDtoIn);
    }

    @ResponseBody
    @GetMapping("/{eventId}")
    public EventDto getEventById(@PathVariable Long userId,
                                 @PathVariable Long eventId) {
        log.info("Запрос на получение события, пользовательский ID = {}, события ID = {}", userId, eventId);
        return service.getEventByIdCurrentUser(userId, eventId);
    }

    @ResponseBody
    @PatchMapping("/{eventId}")
    public EventDto changeEvent(@PathVariable Long userId,
                                @PathVariable Long eventId,
                                @RequestBody @Valid EventParamUpdateAdmin eventParamUpdateAdmin) {
        log.info("Запрос на изменение события. Пользовательский ID = {}, события ID = {}, новые данные - {}",
                userId, eventId, eventParamUpdateAdmin);
        return service.changeEvent(userId, eventId, eventParamUpdateAdmin);
    }

    @ResponseBody
    @GetMapping("/{eventId}/requests")
    public List<RequestDto> getRequestsParticipantEvent(@PathVariable Long userId,
                                                        @PathVariable Long eventId) {
        log.info("Запрос на получение запросов на участие в событии пользователя. " +
                "Пользовательский ID = {}, события ID = {}", userId, eventId);
        return service.getRequestsParticipantEvent(userId, eventId);
    }

    @ResponseBody
    @PatchMapping("/{eventId}/requests")
    public RequestChangingStatusDto changeRequestStatus(@PathVariable Long userId,
                                                        @PathVariable Long eventId,
                                                        @RequestBody RequestUpdateDto requestUpdateDto) {
        log.info("Запрос на изменения статусов запросов, Пользовательский ID = {}, события ID = {}. " +
                "Новый статус для заявок на события - {}", userId, eventId, requestUpdateDto);
        return service.changeRequestStatus(userId, eventId, requestUpdateDto);
    }
}
