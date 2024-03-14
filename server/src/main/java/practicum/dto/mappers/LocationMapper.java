package practicum.dto.mappers;

import practicum.dto.locations.LocationDto;
import practicum.model.Location;

public class LocationMapper {

    public static LocationDto toDto(Location location) {
        return new LocationDto(location.getLatitude(), location.getLongitude());
    }

    public static Location toLocation(LocationDto locationDto) {
        return new Location(locationDto.getLatitude(), locationDto.getLongitude());
    }
}
