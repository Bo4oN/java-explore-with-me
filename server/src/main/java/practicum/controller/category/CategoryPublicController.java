package practicum.controller.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import practicum.dto.categories.CategoryDto;
import practicum.service.category.CategoryServiceImpl;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/categories")
@Validated
public class CategoryPublicController {
    private final CategoryServiceImpl service;

    @ResponseBody
    @GetMapping
    public List<CategoryDto> getCategories(@RequestParam(value = "from", defaultValue = "0") @PositiveOrZero int from,
                                           @RequestParam(value = "size", defaultValue = "10") @Positive int size) {
        log.info("Запрос на получение категорий, from = {}, size = {}", from, size);
        return service.getAllCategories(from, size);
    }

    @ResponseBody
    @GetMapping("/{catId}")
    public CategoryDto getCategoryById(@PathVariable Long catId) {
        log.info("Запрос на получение категории. ID = {}", catId);
        return service.getCategoryById(catId);
    }
}
