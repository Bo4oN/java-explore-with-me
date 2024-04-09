package practicum.dto.compilation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import practicum.dto.events.EventDtoLight;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompilationDto {
    private long id;
    private boolean pinned;
    private String title;
    private Set<EventDtoLight> events;
}
