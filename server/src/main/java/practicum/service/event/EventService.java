package practicum.service.event;

import practicum.dto.events.*;
import practicum.dto.requests.RequestChangingStatusDto;
import practicum.dto.requests.RequestDto;
import practicum.dto.requests.RequestUpdateDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EventService {

    List<EventDto> getEventsFromAdmin(EventParamAdmin eventParamAdmin);

    EventDto updateEventFromAdmin(Long eventId, EventParamUpdateAdmin eventParamUpdateAdmin);

    List<EventDtoLight> getEventsCurrentUser(Long userId, Integer from, Integer size);

    EventDto createEvent(Long userId, EventDtoIn eventDtoIn);

    EventDto getEventByIdCurrentUser(Long userId, Long eventId);

    EventDto changeEvent(Long userId, Long eventId, EventParamUpdateAdmin eventParamUpdateAdmin);

    List<RequestDto> getRequestsParticipantEvent(Long userId, Long eventId);

    RequestChangingStatusDto changeRequestStatus(Long userId, Long eventId, RequestUpdateDto requestUpdateDto);

    List<EventDtoLight> getAllEvent(EventParam searchEventParams, HttpServletRequest request);

    EventDto getEventById(Long eventId, HttpServletRequest request);
}
