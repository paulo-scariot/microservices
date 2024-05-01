package br.com.microservices.productapi.modules.category.service;

import br.com.microservices.productapi.config.exception.ValidationException;
import br.com.microservices.productapi.config.messages.SuccessResponse;
import br.com.microservices.productapi.modules.category.dto.CategoryRequest;
import br.com.microservices.productapi.modules.category.dto.CategoryResponse;
import br.com.microservices.productapi.modules.category.model.Category;
import br.com.microservices.productapi.modules.category.repository.CategoryRepository;
import br.com.microservices.productapi.modules.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

//    @Autowired
//    private ProductService productService;

    private final ProductService productService;

    @Autowired
    public CategoryService(@Lazy final ProductService productService) {
        this.productService = productService;
    }

    private void validateCategoryName(CategoryRequest request) {
        if (isEmpty(request.getDescription())){
            throw new ValidationException("The category name cannot be empty");
        }
    }

    public Category findById(Integer id) {
        validateId(id);
        return categoryRepository
                .findById(id)
                .orElseThrow(() -> new ValidationException("The category with id " + id + " not found"));
    }

    public CategoryResponse save(CategoryRequest request) {
        validateCategoryName(request);
        var category = categoryRepository.save(Category.of(request));
        return CategoryResponse.of(category);
    }

    public CategoryResponse update(CategoryRequest request,
                                   Integer id) {
        validateCategoryName(request);
        validateId(id);
        var category = Category.of(request);
        category.setId(id);
        categoryRepository.save(category);
        return CategoryResponse.of(category);
    }

    public List<CategoryResponse> findByDescription(String description) {
        if (isEmpty(description)){
            throw new ValidationException("The description cannot be empty");
        }
        return categoryRepository
                .findByDescriptionContainingIgnoreCase(description)
                .stream()
                .map(CategoryResponse::of)
                .toList();
    }

    public List<CategoryResponse> findAll() {
        return categoryRepository
                .findAll()
                .stream()
                .map(CategoryResponse::of)
                .toList();
    }

    public CategoryResponse findByIdResponse(Integer id) {
        return CategoryResponse.of(findById(id));
    }

    public SuccessResponse delete(@PathVariable Integer id) {
        validateId(id);
        if (productService.existsByCategoryId(id)){
            throw new ValidationException("The category with id " + id + " is used in a product");
        }
        categoryRepository.deleteById(id);
        return SuccessResponse.create("Category with id " + id + " has been deleted");
    }

    private void validateId(Integer id) {
        if (isEmpty(id)){
            throw new ValidationException("The category id cannot be empty");
        }
    }
}
