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
public class EventParamAdmin {
    private List<Long> users;
    private List<String> states;
    private List<Long> categories;
    private String rangeStart;
    private String rangeEnd;
    @PositiveOrZero
    private Integer from = 0;
    @Positive
    private Integer size = 10;
}
