package practicum.dto.mappers;

public interface BaseMapper<T, S> {

    T toDto(S entity);

    S fromDto(T dto);
}
