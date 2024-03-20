package practicum.dto.events;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import practicum.dto.categories.CategoryDto;
import practicum.dto.locations.LocationDto;
import practicum.dto.users.UserDtoLight;
import practicum.model.enums.EventState;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {
    private long id;
    private String annotation;
    private CategoryDto categoryDto;
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private LocationDto locationDto;
    private boolean paid;
    private int participantLimit;
    private boolean moderation;
    private String title;
    private int confirm;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;
    private UserDtoLight initiator;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedDate;
    private EventState state;
    private long views;

    public EventDto(long id,
                    String annotation,
                    CategoryDto categoryDto,
                    String description,
                    LocalDateTime eventDate,
                    LocationDto locationDto,
                    boolean paid, int participantLimit,
                    boolean moderation, String title,
                    int confirm, LocalDateTime createdDate,
                    UserDtoLight initiator,
                    LocalDateTime publishedDate,
                    EventState state) {
        this.id = id;
        this.annotation = annotation;
        this.categoryDto = categoryDto;
        this.description = description;
        this.eventDate = eventDate;
        this.locationDto = locationDto;
        this.paid = paid;
        this.participantLimit = participantLimit;
        this.moderation = moderation;
        this.title = title;
        this.confirm = confirm;
        this.createdDate = createdDate;
        this.initiator = initiator;
        this.publishedDate = publishedDate;
        this.state = state;
    }
}
