package practicum.dto.compilation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompilationDtoUpdate {
    private Boolean pinned;
    @Size(max = 50)
    private String title;
    private Set<Long> events;
}
