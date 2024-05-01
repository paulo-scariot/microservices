package br.com.microservices.productapi.modules.sale.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleProduct {

    @JsonProperty("productId")
    private Integer productId;

    @JsonProperty("quantity")
    private Integer quantity;
}
