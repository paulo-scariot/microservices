package br.com.microservices.productapi.modules.product.service;

import br.com.microservices.productapi.config.exception.ValidationException;
import br.com.microservices.productapi.config.messages.SuccessResponse;
import br.com.microservices.productapi.modules.category.service.CategoryService;
import br.com.microservices.productapi.modules.product.dto.ProductCheckStockRequest;
import br.com.microservices.productapi.modules.product.dto.ProductRequest;
import br.com.microservices.productapi.modules.product.dto.ProductResponse;
import br.com.microservices.productapi.modules.product.model.Product;
import br.com.microservices.productapi.modules.product.repository.ProductRepository;
import br.com.microservices.productapi.modules.sale.client.SaleClient;
import br.com.microservices.productapi.modules.sale.dto.Sale;
import br.com.microservices.productapi.modules.sale.dto.SaleConfirmation;
import br.com.microservices.productapi.modules.sale.dto.SaleProduct;
import br.com.microservices.productapi.modules.sale.dto.SalesByProductResponse;
import br.com.microservices.productapi.modules.sale.enums.SaleStatus;
import br.com.microservices.productapi.modules.sale.rabbitmq.SaleConfirmationSender;
import br.com.microservices.productapi.modules.supplier.service.SupplierService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
@AllArgsConstructor(onConstructor_ = { @Lazy})
public class ProductService {

    private final static Integer ZERO = 0;

    private final ProductRepository productRepository;

    private final SupplierService supplierService;

    private final CategoryService categoryService;

    private final SaleConfirmationSender saleConfirmationSender;

    private final SaleClient saleClient;

    public Product findById(Integer id) {
        return productRepository
                .findById(id)
                .orElseThrow(() -> new ValidationException("The product with id " + id + " not found"));
    }

    public Boolean existsByCategoryId(Integer id) {
        return productRepository.existsByCategoryId(id);
    }

    public Boolean existsBySupplierId(Integer id) {
        return productRepository.existsBySupplierId(id);
    }

    public ProductResponse save(ProductRequest request) {
        validateProductData(request);
        validateCategoryAndSupplier(request);
        var supplier = supplierService.findById(request.getSupplierId());
        var category = categoryService.findById(request.getCategoryId());
        var product = productRepository.save(Product.of(request, supplier, category));
        return ProductResponse.of(product);
    }

    public ProductResponse update(ProductRequest request,
                                  Integer id) {
        validateProductData(request);
        validateId(id);
        validateCategoryAndSupplier(request);
        var supplier = supplierService.findById(request.getSupplierId());
        var category = categoryService.findById(request.getCategoryId());
        Product product = Product.of(request, supplier, category);
        product.setId(id);
        Product save = productRepository.save(product);
        return ProductResponse.of(save);
    }

    private void validateProductData(ProductRequest request) {
        if (isEmpty(request.getName())){
            throw new ValidationException("The product name cannot be empty");
        }
        if (isEmpty(request.getQuantityAvailable())){
            throw new ValidationException("The product quantity available cannot be empty");
        }
        if (request.getQuantityAvailable() < ZERO){
            throw new ValidationException("The product quantity available should be greater than zero");
        }
    }

    private void validateCategoryAndSupplier(ProductRequest request) {
        if (isEmpty(request.getCategoryId())){
            throw new ValidationException("The category id cannot be empty");
        }
        if (isEmpty(request.getSupplierId())){
            throw new ValidationException("The supplier id cannot be empty");
        }
    }

    public List<ProductResponse> findByName(String name) {
        if (isEmpty(name)){
            throw new ValidationException("The product name cannot be empty");
        }
        return productRepository
                .findByNameContainingIgnoreCase(name)
                .stream()
                .map(ProductResponse::of)
                .toList();
    }

    public List<ProductResponse> findAll() {
        return productRepository
                .findAll()
                .stream()
                .map(ProductResponse::of)
                .toList();
    }

    public ProductResponse findByIdResponse(Integer id) {
        validateId(id);
        return ProductResponse.of(findById(id));
    }

    public List<ProductResponse> findBySupplierId (Integer id) {
        if (isEmpty(id)){
            throw new ValidationException("The supplier id cannot be empty");
        }
        return productRepository
                .findBySupplierId(id)
                .stream()
                .map(ProductResponse::of)
                .toList();
    }

    public List<ProductResponse> findByCategoryId (Integer id) {
        if (isEmpty(id)){
            throw new ValidationException("The category id cannot be empty");
        }
        return productRepository
                .findByCategoryId(id)
                .stream()
                .map(ProductResponse::of)
                .toList();
    }

    public SuccessResponse delete(@PathVariable Integer id) {
        validateId(id);
        productRepository.deleteById(id);
        return SuccessResponse.create("The product with id " + id + " has been deleted");
    }

    private void validateId(Integer id) {
        if (isEmpty(id)){
            throw new ValidationException("The product id cannot be empty");
        }
    }

    @Transactional
    public void updateProductStock(Sale sale){
        validateStockUpdateData(sale);
        try {
            updateStock(sale);
        } catch (Exception e){
            var salesConfirmation = new SaleConfirmation(sale.getSaleId(), SaleStatus.REJECTED);
            saleConfirmationSender.sendSalesConfirmationMessage(salesConfirmation);
        }
    }

    @Transactional
    public void updateStock(Sale sale){
        var productsFormUpdate = new ArrayList<Product>();
        sale
                .getProducts()
                .forEach(saleProduct -> {
                    var stockProduct = findById(saleProduct.getProductId());
                    validateQuantityStock(saleProduct, stockProduct);
                    stockProduct.updateQuantityAvailable(saleProduct.getQuantity());
                    productsFormUpdate.add(stockProduct);
                });
        if (!isEmpty(productsFormUpdate)){
            productRepository.saveAll(productsFormUpdate);
            var salesConfirmation = new SaleConfirmation(sale.getSaleId(), SaleStatus.APPROVED);
            saleConfirmationSender.sendSalesConfirmationMessage(salesConfirmation);
        }
    }

    private void validateStockUpdateData(Sale sale) {
        if (isEmpty(sale) || isEmpty(sale.getSaleId())){
            throw new ValidationException("The product data or sales cannot be empty");
        }
        if (isEmpty(sale.getProducts())){
            throw new ValidationException("The sale products cannot be empty");
        }
        sale
                .getProducts()
                .forEach(saleProduct -> {
                    if (isEmpty(saleProduct.getQuantity()) || isEmpty(saleProduct.getProductId())) {
                        throw new ValidationException("The product ID and quantity cannot be empty");
                    }
                });
    }

    private void validateQuantityStock(SaleProduct saleProduct, Product stockProduct){
        if (saleProduct.getQuantity() > stockProduct.getQuantityAvailable()) {
            throw new ValidationException(
                    String.format("The product %s is out of stock", stockProduct.getId())
            );
        }
    }

    public SalesByProductResponse findSalesByProductId(Integer id){
        var product = findById(id);
        try {
            var sales = saleClient
                    .findSalesByProductId(product.getId())
                    .orElseThrow(() -> new ValidationException("The sales was not found by this product"));
            return SalesByProductResponse.of(product, sales.getSalesIds());
        } catch (Exception e) {
            throw new ValidationException("Error trying to get the product's sales");
        }
    }

    public SuccessResponse checkStock(ProductCheckStockRequest request){
        if (isEmpty(request) || isEmpty(request.getProducts())){
            throw new ValidationException("The request data and sales products cannot be empty");
        }
        request
                .getProducts()
                .forEach(this::validateStock);
        return SuccessResponse.create("The stock is ok");

    }

    private void validateStock(SaleProduct saleProduct){
        if (isEmpty(saleProduct.getQuantity()) || isEmpty(saleProduct.getProductId())){
            throw new ValidationException("The product ID and quantity cannot be empty");
        }
        var product = findById(saleProduct.getProductId());
        if (product.getQuantityAvailable() < saleProduct.getQuantity()){
            throw new ValidationException(String.format("The product %s is out of stock", product.getId()));
        }
    }

}
