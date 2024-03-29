package practicum.dto.mappers.event;

import org.mapstruct.Mapper;
import practicum.dto.events.EventDto;
import practicum.dto.mappers.BaseMapper;
import practicum.dto.mappers.MappingConfig;
import practicum.dto.mappers.category.CategoryMapper;
import practicum.dto.mappers.location.LocationMapper;
import practicum.dto.mappers.user.UserLightMapper;
import practicum.model.Event;

@Mapper(config = MappingConfig.class,
        uses = {CategoryMapper.class, UserLightMapper.class, LocationMapper.class}
)
public interface EventMapper extends BaseMapper<EventDto, Event> {
}
