package practicum.dto.mappers;

import practicum.dto.users.UserDto;
import practicum.dto.users.UserDtoLight;
import practicum.model.User;

public class UserMapper {

    public static User toUser(UserDto userDto) {
        return new User(userDto.getId(), userDto.getName(), userDto.getEmail());
    }

    public static UserDto toUserDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }

    public static UserDtoLight toUserDtoLight(User user) {
        return new UserDtoLight(user.getId(), user.getName());
    }
}
