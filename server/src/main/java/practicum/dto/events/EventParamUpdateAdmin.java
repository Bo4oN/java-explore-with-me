package practicum.dto.events;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import practicum.dto.locations.LocationDto;
import practicum.model.enums.EventState;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventParamUpdateAdmin {
    @Size(min = 20, max = 2000)
    private String annotation;
    private Long category;
    @Size(min = 20, max = 7000)
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    @Valid
    private LocationDto locationDto;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private EventState state;
    @Size(min = 3, max = 120)
    private String title;
}
