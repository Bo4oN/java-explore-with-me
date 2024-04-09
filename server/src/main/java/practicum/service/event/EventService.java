package practicum.service.event;

import practicum.dto.events.EventDto;
import practicum.dto.events.EventDtoIn;
import practicum.dto.events.EventDtoLight;
import practicum.dto.parameters.EventParam;
import practicum.dto.parameters.EventParamUpdateAdmin;
import practicum.dto.requests.RequestChangingStatusDto;
import practicum.dto.requests.RequestDto;
import practicum.dto.requests.RequestUpdateDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EventService {

    List<EventDto> getEventsFromAdmin(List<Long> users, List<String> states, List<Long> categories,
                                      String start, String end, int from, int size);

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
