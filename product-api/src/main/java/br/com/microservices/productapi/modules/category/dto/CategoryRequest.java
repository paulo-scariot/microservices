package br.com.microservices.productapi.modules.category.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CategoryRequest {

    @JsonProperty("description")
    private String description;
}
