package br.com.microservices.productapi.modules.supplier.service;

import br.com.microservices.productapi.config.exception.ValidationException;
import br.com.microservices.productapi.config.messages.SuccessResponse;
import br.com.microservices.productapi.modules.product.service.ProductService;
import br.com.microservices.productapi.modules.supplier.dto.SupplierRequest;
import br.com.microservices.productapi.modules.supplier.dto.SupplierResponse;
import br.com.microservices.productapi.modules.supplier.model.Supplier;
import br.com.microservices.productapi.modules.supplier.repository.SupplierRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
@AllArgsConstructor(onConstructor_ = { @Lazy })
public class SupplierService {

    private final SupplierRepository supplierRepository;

    @Lazy
    private final ProductService productService;

    private void validateSupplierName(SupplierRequest request) {
        if (isEmpty(request.getName())){
            throw new ValidationException("The supplier name cannot be empty");
        }
    }

    public Supplier findById(Integer id) {
        return supplierRepository
                .findById(id)
                .orElseThrow(() -> new ValidationException("The supplier with id " + id + " not found"));
    }

    public SupplierResponse save(SupplierRequest request) {
        validateSupplierName(request);
        var supplier = supplierRepository.save(Supplier.of(request));
        return SupplierResponse.of(supplier);
    }

    public SupplierResponse update(SupplierRequest request,
                                   Integer id) {
        validateSupplierName(request);
        validateId(id);
        var supplier = Supplier.of(request);
        supplier.setId(id);
        supplierRepository.save(supplier);
        return SupplierResponse.of(supplier);
    }

    public List<SupplierResponse> findByName(String name) {
        if (isEmpty(name)){
            throw new ValidationException("The supplier name cannot be empty");
        }
        return supplierRepository
                .findByNameContainingIgnoreCase(name)
                .stream()
                .map(SupplierResponse::of)
                .toList();
    }

    public List<SupplierResponse> findAll() {
        return supplierRepository
                .findAll()
                .stream()
                .map(SupplierResponse::of)
                .toList();
    }

    public SupplierResponse findByIdResponse(Integer id) {
        validateId(id);
        return SupplierResponse.of(findById(id));
    }

    public SuccessResponse delete(@PathVariable Integer id) {
        validateId(id);
        if (productService.existsBySupplierId(id)){
            throw new ValidationException("The supplier with id " + id + " is used in a product");
        }
        supplierRepository.deleteById(id);
        return SuccessResponse.create("The supplier with id " + id + " has been deleted");
    }

    private void validateId(Integer id) {
        if (isEmpty(id)){
            throw new ValidationException("The supplier id cannot be empty");
        }
    }
}

