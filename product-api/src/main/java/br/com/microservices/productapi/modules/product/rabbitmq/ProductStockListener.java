package br.com.microservices.productapi.modules.product.rabbitmq;

import br.com.microservices.productapi.modules.sale.dto.Sale;
import br.com.microservices.productapi.modules.product.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProductStockListener {

    @Autowired
    private ProductService productService;

    @RabbitListener(queues = "${app-config.rabbit.queue.product-stock}")
    public void receiveSaleMessage(Sale sale) throws JsonProcessingException {
        log.info("Received product sale: {}", new ObjectMapper().writeValueAsString(sale));
        productService.updateProductStock(sale);
    }
}
