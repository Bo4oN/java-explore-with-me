package practicum.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RequestStatus {
    PENDING,
    CONFIRMED,
    REJECTED,
    CANCELED
}
