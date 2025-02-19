package br.com.microservices.productapi.modules.category.controller;

import br.com.microservices.productapi.config.messages.SuccessResponse;
import br.com.microservices.productapi.modules.category.dto.CategoryRequest;
import br.com.microservices.productapi.modules.category.dto.CategoryResponse;
import br.com.microservices.productapi.modules.category.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@AllArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public CategoryResponse save(@RequestBody CategoryRequest request) {
        return categoryService.save(request);
    }

    @GetMapping("{id}")
    public CategoryResponse findById(@PathVariable Integer id) {
        return categoryService.findByIdResponse(id);
    }

    @GetMapping
    public List<CategoryResponse> findAll() {
        return categoryService.findAll();
    }

    @GetMapping("description/{description}")
    public List<CategoryResponse> findByDescription(@PathVariable String description) {
        return categoryService.findByDescription(description);
    }

    @PutMapping("{id}")
    public CategoryResponse update(@RequestBody CategoryRequest request,
                                   @PathVariable Integer id) {
        return categoryService.update(request, id);
    }

    @DeleteMapping("{id}")
    public SuccessResponse delete(@PathVariable Integer id) {
        return categoryService.delete(id);
    }

}
