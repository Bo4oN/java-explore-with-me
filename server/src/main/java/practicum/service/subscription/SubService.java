package practicum.service.subscription;

import practicum.dto.mappers.user.UserWithEventsMapper;
import practicum.dto.users.UserDtoWithEvents;

import java.util.List;

public interface SubService {

    UserDtoWithEvents addSub(long userId, long subId);

    List<UserDtoWithEvents> getUsersSub(long userId);

    void deleteSub(long userId, long subId);
}
