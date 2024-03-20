package practicum.dto.mappers.location;

import org.mapstruct.Mapper;
import practicum.dto.locations.LocationDto;
import practicum.dto.mappers.BaseMapper;
import practicum.dto.mappers.MappingConfig;
import practicum.model.Location;

@Mapper(config = MappingConfig.class)
public interface LocationMapper extends BaseMapper<LocationDto, Location> {
}
