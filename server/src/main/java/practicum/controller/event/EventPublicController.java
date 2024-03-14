package practicum.controller.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import practicum.dto.events.EventDto;
import practicum.dto.events.EventDtoLight;
import practicum.dto.events.EventParam;
import practicum.service.event.EventServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/events")
public class EventPublicController {
    private final EventServiceImpl service;

    @ResponseBody
    @GetMapping
    public List<EventDtoLight> getAllEvents(@Valid EventParam eventParam,
                                            HttpServletRequest httpServletRequest) {
        log.info("Запрос на получение событий по фильтрам - {}", eventParam);
        return service.getAllEvent(eventParam, httpServletRequest);
    }

    @ResponseBody
    @GetMapping("/{id}")
    public EventDto getEventById(@PathVariable long id,
                                 HttpServletRequest httpServletRequest) {
        log.info("Запрос на получение события по ID = {}", id);
        return service.getEventById(id, httpServletRequest);
    }
}
