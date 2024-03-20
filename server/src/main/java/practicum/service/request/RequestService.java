package practicum.service.request;

import practicum.dto.requests.RequestDto;

import java.util.List;

public interface RequestService {

    List<RequestDto> getUsersRequests(long userId);

    RequestDto sendRequest(long userId, long eventId);

    RequestDto cancelledRequest(long userId, long requestId);
}
