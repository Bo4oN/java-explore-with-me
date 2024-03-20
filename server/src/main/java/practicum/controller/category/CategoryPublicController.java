package practicum.controller.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import practicum.dto.categories.CategoryDto;
import practicum.service.category.CategoryServiceImpl;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/categories")
public class CategoryPublicController {
    private final CategoryServiceImpl service;

    @ResponseBody
    @GetMapping
    public List<CategoryDto> getCategories(@RequestParam(value = "from", defaultValue = "0") int from,
                                           @RequestParam(value = "size", defaultValue = "10") int size) {
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
