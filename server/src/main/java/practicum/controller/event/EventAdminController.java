package practicum.controller.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import practicum.dto.events.EventDto;
import practicum.dto.parameters.EventParamUpdateAdmin;
import practicum.service.event.EventServiceImpl;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/admin/events")
@Validated
public class EventAdminController {
    private final EventServiceImpl service;

    @ResponseBody
    @GetMapping
    public List<EventDto> getEvents(@RequestParam(required = false) List<Long> users,
                                    @RequestParam(required = false) List<String> states,
                                    @RequestParam(required = false) List<Long> categories,
                                    @RequestParam(required = false) String rangeStart,
                                    @RequestParam(required = false) String rangeEnd,
                                    @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                    @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Запрос на получение событий, параметры - {}, {}, {}, {}, {}, {}, {}",
                users, states, categories, rangeStart, rangeEnd, from, size);
        return service.getEventsFromAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @ResponseBody
    @PatchMapping("/{eventId}")
    public EventDto updateEventById(@PathVariable Long eventId,
                                    @RequestBody @Valid EventParamUpdateAdmin eventParamUpdateAdmin) {
        log.info("Запрос на изменение события, ID = {}, параметры - {}", eventId, eventParamUpdateAdmin);
        return service.updateEventFromAdmin(eventId, eventParamUpdateAdmin);
    }
}
