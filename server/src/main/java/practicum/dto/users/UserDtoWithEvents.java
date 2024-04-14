package practicum.dto.users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDtoWithEvents {
    private Long id;
    private String name;
    private List<Long> pastEvents;
    private List<Long> futureEvents;
}
