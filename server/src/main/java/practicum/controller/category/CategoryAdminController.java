package practicum.controller.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import practicum.dto.categories.CategoryDto;
import practicum.service.category.CategoryServiceImpl;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/admin/categories")
public class CategoryAdminController {
    private final CategoryServiceImpl service;

    @ResponseBody
    @PostMapping
    public CategoryDto createCategory(@RequestBody @Valid CategoryDto categoryDto) {
        log.info("Запрос на создание категории - {}", categoryDto);
        return service.createCategory(categoryDto);
    }

    @ResponseBody
    @DeleteMapping("/{catId}")
    public void deleteCategory(@PathVariable Long catId) {
        log.info("Запрос на удаление категории. ID = {}", catId);
        service.deleteCategory(catId);
    }

    @ResponseBody
    @PatchMapping("/{catId}")
    public CategoryDto changingCategory(@PathVariable Long catId,
                                        @RequestBody @Valid CategoryDto categoryDto) {
        log.info("Запрос на изменение категории. ID = {}, новые данные - {}", catId, categoryDto);
        return service.changingCategory(catId, categoryDto);
    }
}
