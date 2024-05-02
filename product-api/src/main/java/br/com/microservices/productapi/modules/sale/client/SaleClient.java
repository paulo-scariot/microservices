package br.com.microservices.productapi.modules.sale.client;

import br.com.microservices.productapi.modules.sale.dto.SaleProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(
        name = "salesClient",
        contextId = "salesClient",
        url = "${app-config.services.sales}"
)
public interface SaleClient {

    @GetMapping("api/sale/product/{productId}")
    Optional<SaleProductResponse> findSalesByProductId(@PathVariable("productId") Integer productId);


}
