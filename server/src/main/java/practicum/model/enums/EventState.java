package practicum.model.enums;

public enum EventState {
    PENDING,
    PUBLISHED,
    CANCELED;

    public static EventState toEnum(String str) {
        if (str.equals("PENDING")) {
            return EventState.PENDING;
        } else if (str.equals("PUBLISHED")) {
            return EventState.PUBLISHED;
        } else {
            return EventState.CANCELED;
        }
    }
}
