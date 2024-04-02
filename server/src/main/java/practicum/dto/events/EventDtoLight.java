package practicum.dto.events;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import practicum.dto.categories.CategoryDto;
import practicum.dto.users.UserDtoLight;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDtoLight {
    private Long id;
    private String annotation;
    private CategoryDto category;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private Boolean paid;
    private String title;
    private int confirmedRequests;
    private UserDtoLight initiator;
    private long views;

    public EventDtoLight(Long id, String annotation,
                         CategoryDto category, LocalDateTime eventDate,
                         Boolean paid, String title,
                         int confirm, UserDtoLight initiator) {
        this.id = id;
        this.annotation = annotation;
        this.category = category;
        this.eventDate = eventDate;
        this.paid = paid;
        this.title = title;
        this.confirmedRequests = confirm;
        this.initiator = initiator;
    }
}
