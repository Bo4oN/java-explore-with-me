package practicum.dto.mappers.user;

import org.mapstruct.Mapper;
import practicum.dto.mappers.BaseMapper;
import practicum.dto.mappers.MappingConfig;
import practicum.dto.users.UserDtoLight;
import practicum.model.User;

@Mapper(config = MappingConfig.class)
public interface UserLightMapper extends BaseMapper<UserDtoLight, User> {
}
