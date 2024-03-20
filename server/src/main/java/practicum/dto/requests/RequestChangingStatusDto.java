package practicum.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RequestChangingStatusDto {
    private List<RequestDto> confirmedRequests;
    private List<RequestDto> rejectedRequests;
}
