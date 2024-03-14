package practicum.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import practicum.model.enums.Status;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestUpdateDto {
    private List<Long> requestsId;
    private Status status;
}
