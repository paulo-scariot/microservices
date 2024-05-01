package br.com.microservices.productapi.modules.sale.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sale {

    @JsonProperty("saleId")
    private String saleId;

    @JsonProperty("products")
    private List<SaleProduct> products;
}
