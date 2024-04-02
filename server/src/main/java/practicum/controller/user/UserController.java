package practicum.controller.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import practicum.dto.users.UserDto;
import practicum.service.user.UserServiceImpl;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/admin/users")
public class UserController {
    private final UserServiceImpl service;

    @ResponseBody
    @GetMapping
    public List<UserDto> getUsers(@RequestParam(value = "ids", required = false) List<Long> ids,
                                  @RequestParam(value = "from", defaultValue = "0") int from,
                                  @RequestParam(value = "size", defaultValue = "10") int size) {
        log.info("Запрос на получение пользователей. IDs = {}, from = {}, size = {}", ids, from, size);
        return service.getUsers(ids, from, size);
    }

    @ResponseBody
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@RequestBody @Valid UserDto userDto) {
        log.info("Запрос на добавление пользователя - {}", userDto);
        return service.createUser(userDto);
    }

    @ResponseBody
    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long userId) {
        log.info("Запрос на удаление пользователя. ID = {}", userId);
        service.deleteUser(userId);
    }
}
