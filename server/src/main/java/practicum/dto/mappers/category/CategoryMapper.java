package practicum.dto.mappers.category;

import org.mapstruct.Mapper;
import practicum.dto.categories.CategoryDto;
import practicum.dto.mappers.BaseMapper;
import practicum.dto.mappers.MappingConfig;
import practicum.model.Category;

@Mapper(config = MappingConfig.class)
public interface CategoryMapper extends BaseMapper<CategoryDto, Category> {
}
//public static Category toCategory(CategoryDto categoryDto) {
//    return new Category(categoryDto.getId(), categoryDto.getName());
//}

//public static CategoryDto toCategoryDto(Category category) {
//    return new CategoryDto(category.getId(), category.getName());
//}
