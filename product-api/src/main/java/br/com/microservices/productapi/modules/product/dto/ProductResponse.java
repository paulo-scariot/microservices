package br.com.microservices.productapi.modules.product.dto;

import br.com.microservices.productapi.modules.category.dto.CategoryResponse;
import br.com.microservices.productapi.modules.product.model.Product;
import br.com.microservices.productapi.modules.supplier.dto.SupplierResponse;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class ProductResponse {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("createdAt")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonProperty("name")
    private String name;

    @JsonProperty("quantityAvailable")
    private Integer quantityAvailable;

    @JsonProperty("supplier")
    private SupplierResponse supplier;

    @JsonProperty("category")
    private CategoryResponse category;

    public static ProductResponse of(Product product) {
        return ProductResponse
                .builder()
                .id(product.getId())
                .createdAt(product.getCreatedAt())
                .name(product.getName())
                .quantityAvailable(product.getQuantityAvailable())
                .supplier(SupplierResponse.of(product.getSupplier()))
                .category(CategoryResponse.of(product.getCategory()))
                .build();
    }
}
