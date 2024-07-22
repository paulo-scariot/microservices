package br.com.microservices.productapi.modules.product.controller;

import br.com.microservices.productapi.config.messages.SuccessResponse;
import br.com.microservices.productapi.modules.product.dto.ProductCheckStockRequest;
import br.com.microservices.productapi.modules.product.dto.ProductRequest;
import br.com.microservices.productapi.modules.product.dto.ProductResponse;
import br.com.microservices.productapi.modules.product.service.ProductService;
import br.com.microservices.productapi.modules.sale.dto.SalesByProductResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ProductResponse save(@RequestBody ProductRequest request) {
        return productService.save(request);
    }

    @GetMapping("{id}")
    public ProductResponse findById(@PathVariable Integer id) {
        return productService.findByIdResponse(id);
    }

    @GetMapping
    public List<ProductResponse> findAll() {
        return productService.findAll();
    }

    @GetMapping("name/{name}")
    public List<ProductResponse> findByDescription(@PathVariable String name) {
        return productService.findByName(name);
    }

    @GetMapping("supplier/{supplierId}")
    public List<ProductResponse> findBySupplierId(@PathVariable Integer supplierId) {
        return productService.findBySupplierId(supplierId);
    }

    @GetMapping("category/{categoryId}")
    public List<ProductResponse> findByCategoryId(@PathVariable Integer categoryId) {
        return productService.findByCategoryId(categoryId);
    }

    @PutMapping("{id}")
    public ProductResponse update(@RequestBody ProductRequest request,
                                  @PathVariable Integer id) {
        return productService.update(request, id);
    }

    @DeleteMapping("{id}")
    public SuccessResponse delete(@PathVariable Integer id) {
        return productService.delete(id);
    }

    @PostMapping("checkstock")
    public SuccessResponse checkStock(@RequestBody ProductCheckStockRequest request) {
        return productService.checkStock(request);
    }

    @GetMapping("{id}/sales")
    public SalesByProductResponse findSalesByProductId(@PathVariable Integer id) {
        return productService.findSalesByProductId(id);
    }
}
