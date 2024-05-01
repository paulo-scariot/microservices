package br.com.microservices.productapi.modules.supplier.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SupplierRequest {

    @JsonProperty("name")
    private String name;
}
