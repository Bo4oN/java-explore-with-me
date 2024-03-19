package practicum.dto.mappers.event;

import org.mapstruct.Mapper;
import practicum.dto.events.EventDto;
import practicum.dto.events.EventDtoIn;
import practicum.dto.events.EventDtoLight;
import practicum.dto.mappers.BaseMapper;
import practicum.dto.mappers.MappingConfig;
import practicum.dto.mappers.category.CategoryMapper;
import practicum.model.Category;
import practicum.model.Event;
import practicum.model.Location;
import practicum.model.enums.EventState;

@Mapper(config = MappingConfig.class,
uses = CategoryMapper.class)
public interface EventMapper extends BaseMapper<EventDto, Event> {

    //public static EventDtoLight toEventDtoLight(Event event) {
    //    return new EventDtoLight(event.getId(),
    //            event.getAnnotation(),
    //            CategoryMapper.toCategoryDto(event.getCategory()),
    //            event.getEventDate(),
    //            event.isPaid(),
    //            event.getTitle(),
    //            event.getConfirm(),
    //            UserMapper.toUserDtoLight(event.getInitiator())
    //    );
    //}

    //public static EventDto toEventDto(Event event) {
    //    return new EventDto(event.getId(),
    //            event.getAnnotation(),
    //            CategoryMapper.toCategoryDto(event.getCategory()),
    //            event.getDescription(),
    //            event.getEventDate(),
    //            LocationMapper.toDto(event.getLocation()),
    //            event.isPaid(),
    //            event.getParticipantLimit(),
    //            event.isModeration(),
    //            event.getTitle(),
    //            event.getConfirm(),
    //            event.getCreatedDate(),
    //            UserMapper.toUserDtoLight(event.getInitiator()),
    //            event.getPublishedDate(),
    //            EventState.toEnum(event.getState()));
    //}
//
    //public static Event toEvent(EventDtoIn eventDtoIn, Category category, Location location) {
    //    return new Event(eventDtoIn.getAnnotation(),
    //            category,
    //            eventDtoIn.getDescription(),
    //            eventDtoIn.getEventDate(),
    //            location,
    //            eventDtoIn.isPaid(),
    //            eventDtoIn.getParticipantLimit(),
    //            eventDtoIn.isRequestModeration(),
    //            eventDtoIn.getTitle()
    //    );
    //}
}
