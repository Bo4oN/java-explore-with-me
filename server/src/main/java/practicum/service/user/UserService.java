package practicum.service.user;

import practicum.dto.users.UserDto;

import java.util.List;

public interface UserService {

    public List<UserDto> getUsers(List<Long> ids, int from, int size);

    public UserDto createUser(UserDto userDto);

    public void deleteUser(long userId);
}
