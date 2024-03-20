package practicum.dto.mappers.event;

import org.mapstruct.Mapper;
import practicum.dto.events.EventDtoLight;
import practicum.dto.mappers.BaseMapper;
import practicum.dto.mappers.MappingConfig;
import practicum.dto.mappers.category.CategoryMapper;
import practicum.dto.mappers.user.UserLightMapper;
import practicum.model.Event;

@Mapper(config = MappingConfig.class,
        uses = {CategoryMapper.class, UserLightMapper.class}
)
public interface EventLightMapper extends BaseMapper<EventDtoLight, Event> {
}
