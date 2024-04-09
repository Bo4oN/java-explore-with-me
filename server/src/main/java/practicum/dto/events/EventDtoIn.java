package practicum.dto.events;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import practicum.dto.locations.LocationDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDtoIn {
    @NotNull
    @NotBlank
    @Size(max = 2000, min = 20)
    private String annotation;
    @NotNull
    private long category;
    @NotNull
    @NotBlank
    @Size(max = 7000, min = 20)
    private String description;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    @NotNull
    private LocationDto location;
    private boolean paid = false;
    @PositiveOrZero
    private int participantLimit = 0;
    private boolean requestModeration = true;
    @NotNull
    @Size(max = 120, min = 3)
    private String title;
}
