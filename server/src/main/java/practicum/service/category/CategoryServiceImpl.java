package practicum.service.category;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import practicum.dto.categories.CategoryDto;
import practicum.dto.mappers.category.CategoryMapper;
import practicum.exceptions.ConflictException;
import practicum.exceptions.NotFoundException;
import practicum.model.Category;
import practicum.repository.CategoryRepository;
import practicum.repository.EventRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository repository;
    private final EventRepository eventRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        if (repository.existsByName(categoryDto.getName())) {
            throw new ConflictException("Название категории занято");
        }
        Category category = repository.save(categoryMapper.fromDto(categoryDto));
        return categoryMapper.toDto(category);
    }

    @Override
    public void deleteCategory(long catId) {
        if (eventRepository.existsByCategoryId(catId)) {
            throw new ConflictException("Категория используется в событии");
        }
        if (repository.existsById(catId)) {
            repository.deleteById(catId);
        } else {
            throw new NotFoundException("Категория не найдена или недоступна");
        }
    }

    @Override
    public CategoryDto changingCategory(long catId, CategoryDto categoryDto) {
        Category category = repository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Категория не найдена или недоступна"));
        if (!category.getName().equals(categoryDto.getName()) && repository.existsByName(categoryDto.getName())) {
            throw new ConflictException("Название категории занято");
        }
        category.setName(categoryDto.getName());
        return categoryMapper.toDto(repository.save(category));
    }

    @Override
    public List<CategoryDto> getAllCategories(int from, int size) {
        Pageable pageable = PageRequest.of(from, size);
        List<Category> categories = repository.findAll(pageable).toList();
        return categories.stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategoryById(long catId) {
        Category category = repository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Категория не найдена или недоступна"));
        return categoryMapper.toDto(category);
    }
}
