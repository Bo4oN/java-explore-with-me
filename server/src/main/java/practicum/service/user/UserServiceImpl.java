package practicum.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import practicum.dto.mappers.user.UserMapper;
import practicum.dto.users.UserDto;
import practicum.exceptions.ConflictException;
import practicum.exceptions.NotFoundException;
import practicum.model.User;
import practicum.repository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserMapper userMapper;

    @Override
    public List<UserDto> getUsers(List<Long> ids, int from, int size) {
        Pageable pageable = PageRequest.of(from, size);
        List<User> users;
        if (ids != null) {
            users = repository.findByIdIn(ids, pageable);
        } else {
            users = repository.findAll(pageable).toList();
        }
        if (users.isEmpty()) {
            return Collections.emptyList();
        } else {
            return users.stream()
                    .map(userMapper::toDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    @Transactional
    public UserDto createUser(UserDto userDto) {
        if (repository.existsByEmail(userDto.getEmail())) {
            throw new ConflictException("Пользователь с таким email уже существует");
        }
        User user = repository.save(userMapper.fromDto(userDto));
        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public void deleteUser(long userId) {
        repository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден или недоступен"));
        repository.deleteById(userId);
    }
}
