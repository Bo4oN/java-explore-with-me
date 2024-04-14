package practicum.controller.subscription;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import practicum.dto.users.UserDtoWithEvents;
import practicum.service.subscription.SubServiceImpl;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/users/{userId}/subs")
public class SubController {

    private final SubServiceImpl service;

    @ResponseBody
    @PostMapping("/{subId}")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDtoWithEvents addSubscription(@PathVariable Long userId,
                                             @PathVariable Long subId) {
        log.info("Добавление подписки пользователя - {}, на пользователя - {}", userId, subId);
        return service.addSub(userId, subId);
    }

    @ResponseBody
    @GetMapping
    public List<UserDtoWithEvents> getUsersSubscription(@PathVariable Long userId) {
        log.info("Получение всех подписок пользователя - {}", userId);
        return service.getUsersSub(userId);
    }

    @ResponseBody
    @DeleteMapping("/{subId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSubscription(@PathVariable Long userId,
                                   @PathVariable Long subId) {
        log.info("Удаление подписки пользователя - {}, на пользователя - {}", userId, subId);
        service.deleteSub(userId, subId);
    }
}
