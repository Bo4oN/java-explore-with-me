package practicum.dto.mappers.user;

import org.mapstruct.Mapper;
import practicum.dto.mappers.BaseMapper;
import practicum.dto.mappers.MappingConfig;
import practicum.dto.users.UserDto;
import practicum.model.User;

@Mapper(config = MappingConfig.class)
public interface UserMapper extends BaseMapper<UserDto, User> {

    //public static User toUser(UserDto userDto) {
    //    return new User(userDto.getId(), userDto.getName(), userDto.getEmail());
    //}
//
    //public static UserDto toUserDto(User user) {
    //    return new UserDto(user.getId(), user.getName(), user.getEmail());
    //}
//
    //public static UserDtoLight toUserDtoLight(User user) {
    //    return new UserDtoLight(user.getId(), user.getName());
    //}
}
