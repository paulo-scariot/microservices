package br.com.microservices.productapi.modules.product.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ProductRequest {

    @JsonProperty("name")
    private String name;

    @JsonProperty("quantityAvailable")
    private Integer quantityAvailable;

    @JsonProperty("supplierId")
    private Integer supplierId;

    @JsonProperty("categoryId")
    private Integer categoryId;
}
