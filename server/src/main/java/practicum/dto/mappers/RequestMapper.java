package practicum.dto.mappers;

import practicum.dto.requests.RequestDto;
import practicum.model.Event;
import practicum.model.Request;
import practicum.model.User;

public class RequestMapper {

    public static RequestDto toRequestDto(Request request) {
        return new RequestDto(request.getId(),
                request.getCreatedDate(),
                request.getEvent().getId(),
                request.getRequester().getId(),
                request.getStatus());
    }

    public Request toRequest(RequestDto requestDto, Event event, User user) {
        return new Request(requestDto.getId(),
                requestDto.getCreatedDate(),
                event,
                user,
                requestDto.getStatus());
    }
}
