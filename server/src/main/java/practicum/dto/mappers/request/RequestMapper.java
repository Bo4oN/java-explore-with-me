package practicum.dto.mappers.request;

import org.mapstruct.Mapper;
import practicum.dto.mappers.BaseMapper;
import practicum.dto.mappers.MappingConfig;
import practicum.dto.requests.RequestDto;
import practicum.model.Event;
import practicum.model.Request;
import practicum.model.User;

@Mapper(config = MappingConfig.class)
public interface RequestMapper extends BaseMapper<RequestDto, Request> {

    //public static RequestDto toRequestDto(Request request) {
    //    return new RequestDto(request.getId(),
    //            request.getCreatedDate(),
    //            request.getEvent().getId(),
    //            request.getRequester().getId(),
    //            request.getStatus());
    //}
//
    //public Request toRequest(RequestDto requestDto, Event event, User user) {
    //    return new Request(requestDto.getId(),
    //            requestDto.getCreatedDate(),
    //            event,
    //            user,
    //            requestDto.getStatus());
    //}
}
