package practicum.dto.mappers.user;

import org.mapstruct.Mapper;
import practicum.dto.mappers.BaseMapper;
import practicum.dto.mappers.MappingConfig;
import practicum.dto.users.UserDtoWithEvents;
import practicum.model.User;

@Mapper(config = MappingConfig.class)
public interface UserWithEventsMapper extends BaseMapper<UserDtoWithEvents, User> {
}
