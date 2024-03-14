package practicum.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import practicum.dto.mappers.UserMapper;
import practicum.dto.users.UserDto;
import practicum.exceptions.NotFoundException;
import practicum.model.User;
import practicum.repository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public List<UserDto> getUsers(List<Long> ids, int from, int size) {
        Pageable pageable = PageRequest.of(from, size);
        List<User> users;
        if (ids != null) {
            for (Long id : ids) {
                repository.findById(id).orElseThrow(() -> new NotFoundException("Пользователь не найден или недоступен"));
            }
            users = repository.findByIdIn(ids, pageable);
        } else {
            users = repository.findAll(pageable).toList();
        }
        if (users.isEmpty()) {
            return Collections.emptyList();
        } else {
            return users.stream()
                    .map(UserMapper::toUserDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = repository.save(UserMapper.toUser(userDto));
        return UserMapper.toUserDto(user);
    }

    @Override
    public void deleteUser(long userId) {
        repository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден или недоступен"));
        repository.deleteById(userId);
    }
}
