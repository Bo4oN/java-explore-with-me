package practicum.dto.locations;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationDto {
    @NotNull
    private float lat;
    @NotNull
    private float lon;
}
