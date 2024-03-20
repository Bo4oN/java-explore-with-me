package practicum.controller.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import practicum.dto.requests.RequestDto;
import practicum.service.request.RequestServiceImpl;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/users/{userId}/requests")
public class RequestController {
    private final RequestServiceImpl service;

    @ResponseBody
    @GetMapping
    public List<RequestDto> getRequestsCurrentUser(@PathVariable Long userId) {
        log.info("Запрос на получение всех запросов на участие пользователя. ID = {}", userId);
        return service.getUsersRequests(userId);
    }

    @ResponseBody
    @PostMapping
    public RequestDto createRequest(@PathVariable Long userId,
                                    @RequestParam Long eventId) {
        log.info("Запрос на создание запроса на участие. Пользователь ID = {}, событие ID = {}", userId, eventId);
        return service.sendRequest(userId, eventId);
    }

    @ResponseBody
    @PatchMapping("/{requestId}/cancel")
    public RequestDto cancelRequest(@PathVariable Long userId,
                                    @PathVariable Long requestId) {
        log.info("Запрос на отмену запроса на участие. Пользователь ID = {}, запрос ID = {}", userId, requestId);
        return service.cancelledRequest(userId, requestId);
    }
}
