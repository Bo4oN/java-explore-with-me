package practicum.dto.compilation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompilationDtoIn {
    private Boolean pinned;
    @NotBlank
    @Size(min = 1, max = 50)
    private String title;
    private Set<Long> eventIds;
}
