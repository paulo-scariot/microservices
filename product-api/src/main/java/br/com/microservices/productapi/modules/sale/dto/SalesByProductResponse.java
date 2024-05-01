package br.com.microservices.productapi.modules.sale.dto;

import br.com.microservices.productapi.modules.category.dto.CategoryResponse;
import br.com.microservices.productapi.modules.product.model.Product;
import br.com.microservices.productapi.modules.supplier.dto.SupplierResponse;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesByProductResponse {

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

    @JsonProperty("sales")
    private List<String> sales;

    public static SalesByProductResponse of(Product product,
                                            List<String> sales) {
        return SalesByProductResponse
                .builder()
                .id(product.getId())
                .createdAt(product.getCreatedAt())
                .name(product.getName())
                .quantityAvailable(product.getQuantityAvailable())
                .supplier(SupplierResponse.of(product.getSupplier()))
                .category(CategoryResponse.of(product.getCategory()))
                .sales(sales)
                .build();
    }
}
