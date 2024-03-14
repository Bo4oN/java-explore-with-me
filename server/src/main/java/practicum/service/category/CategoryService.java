package practicum.service.category;

import practicum.dto.categories.CategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDto createCategory(CategoryDto categoryDto);

    void deleteCategory(long catId);

    CategoryDto changingCategory(long catId, CategoryDto categoryDto);

    List<CategoryDto> getAllCategories(int from, int size);

    CategoryDto getCategoryById(long catId);
}
