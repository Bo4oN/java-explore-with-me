package practicum.dto.mappers;

public interface BaseMapper<DTO, ENTITY> {

    DTO toDto(ENTITY entity);

    ENTITY fromDto(DTO dto);
}
