package practicum.controller.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import practicum.dto.events.EventDto;
import practicum.dto.events.EventParamAdmin;
import practicum.dto.events.EventParamUpdateAdmin;
import practicum.service.event.EventServiceImpl;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/admin/events")
public class EventAdminController {
    private final EventServiceImpl service;

    @ResponseBody
    @GetMapping
    public List<EventDto> getEvents(@Valid EventParamAdmin eventParamAdmin) {
        log.info("Запрос на получение событий, параметры - {}", eventParamAdmin);
        return service.getEventsFromAdmin(eventParamAdmin);
    }

    @ResponseBody
    @PatchMapping("/{eventId}")
    public EventDto updateEventById(@PathVariable Long eventId,
                                    @RequestParam EventParamUpdateAdmin eventParamUpdateAdmin) {
        log.info("Запрос на получение события, ID = {}, параметры - {}", eventId, eventParamUpdateAdmin);
        return service.updateEventFromAdmin(eventId, eventParamUpdateAdmin);
    }
}
