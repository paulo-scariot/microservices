package br.com.microservices.productapi.modules.supplier.dto;

import br.com.microservices.productapi.modules.supplier.model.Supplier;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class SupplierResponse {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("name")
    private String name;

    public static SupplierResponse of(Supplier supplier) {
        var response = new SupplierResponse();
        BeanUtils.copyProperties(supplier, response);
        return response;
    }
}
