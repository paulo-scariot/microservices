package br.com.microservices.productapi.modules.product.dto;

import br.com.microservices.productapi.modules.sale.dto.SaleProduct;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductCheckStockRequest {

    @JsonProperty("products")
    private List<SaleProduct> products;
}
