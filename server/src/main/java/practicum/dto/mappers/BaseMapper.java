package practicum.dto.mappers;

public interface BaseMapper<Dto, Entity> {

    Dto toDto(Entity entity);

    Entity fromDto(Dto dto);
}
