package practicum.dto.mappers.compilation;

import org.mapstruct.Mapper;
import practicum.dto.compilation.CompilationDto;
import practicum.dto.mappers.BaseMapper;
import practicum.dto.mappers.MappingConfig;
import practicum.dto.mappers.event.EventLightMapper;
import practicum.model.Compilation;

@Mapper(config = MappingConfig.class,
        uses = EventLightMapper.class)
public interface CompilationMapper extends BaseMapper<CompilationDto, Compilation> {

    //public static CompilationDto toCompilationDto(Compilation compilation) {
    //    return new CompilationDto(compilation.getId(),
    //            compilation.isPinned(),
    //            compilation.getTitle(),
    //            compilation.getEvents().stream()
    //                    .map(EventMapper::toEventDtoLight)
    //                    .collect(Collectors.toSet())
    //    );
    //}
}
