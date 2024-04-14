package practicum.service.subscription;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import practicum.dto.mappers.user.UserWithEventsMapper;
import practicum.dto.users.UserDtoWithEvents;
import practicum.exceptions.BadRequestException;
import practicum.exceptions.NotFoundException;
import practicum.model.SubId;
import practicum.model.Subscriber;
import practicum.model.User;
import practicum.repository.EventRepository;
import practicum.repository.SubRepository;
import practicum.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubServiceImpl implements SubService {

    private final SubRepository subRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final UserWithEventsMapper mapper;

    @Override
    public UserDtoWithEvents addSub(long userId, long subId) {
        if (userId == subId) {
            throw new BadRequestException("Нельзя подписаться на самого себя");
        }
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден или недоступен"));
        User sub = userRepository.findById(subId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден или недоступен"));
        subRepository.save(new Subscriber(userId, subId));
        UserDtoWithEvents subUser = mapper.toDto(sub);
        subUser.setPastEvents(
                eventRepository.findAllIdByInitiatorIdAndEventDateBefore(subId, LocalDateTime.now())
                );
        subUser.setFutureEvents(
                eventRepository.findAllIdByInitiatorIdAndEventDateAfter(subId, LocalDateTime.now())
                );
        return subUser;
    }

    @Override
    public List<UserDtoWithEvents> getUsersSub(long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден или недоступен"));
        List<UserDtoWithEvents> userList = new ArrayList<>(Collections.emptyList());
        if (subRepository.existsByUserId(userId)) {
            List<Long> subsList = subRepository.findAllUserSubscription(userId);
            for (User user : userRepository.findAllById(subsList)) {
                UserDtoWithEvents userWithEvents = mapper.toDto(user);
                userWithEvents.setPastEvents(
                        eventRepository.findAllIdByInitiatorIdAndEventDateBefore(user.getId(), LocalDateTime.now())
                );
                userWithEvents.setFutureEvents(
                        eventRepository.findAllIdByInitiatorIdAndEventDateAfter(user.getId(), LocalDateTime.now())
                        );
                userList.add(userWithEvents);
            }
        }
        return userList;
    }

    @Override
    public void deleteSub(long userId, long subId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден или недоступен"));
        userRepository.findById(subId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден или недоступен"));

        subRepository.delete(new Subscriber(userId, subId));
    }
}
