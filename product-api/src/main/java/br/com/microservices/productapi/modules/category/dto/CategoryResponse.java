package br.com.microservices.productapi.modules.category.dto;

import br.com.microservices.productapi.modules.category.model.Category;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class CategoryResponse {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("description")
    private String description;

    public static CategoryResponse of(Category category) {
        var response = new CategoryResponse();
        BeanUtils.copyProperties(category, response);
        return response;
    }
}
