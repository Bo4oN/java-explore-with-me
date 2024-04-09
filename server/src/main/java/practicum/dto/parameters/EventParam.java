package practicum.dto.parameters;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventParam {
    private String text;
    private List<Long> categories;
    private Boolean paid;
    private String rangeStart;
    private String rangeEnd;
    private Boolean onlyAvailable = false;
    private String sort;
    @PositiveOrZero
    private Integer from = 0;
    @Positive
    private Integer size = 10;
}
