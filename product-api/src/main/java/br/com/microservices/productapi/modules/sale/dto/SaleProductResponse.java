package br.com.microservices.productapi.modules.sale.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleProductResponse {

    @JsonProperty("salesIds")
    private List<String> salesIds;
}
