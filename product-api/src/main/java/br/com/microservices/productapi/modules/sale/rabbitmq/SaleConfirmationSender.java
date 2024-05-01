package br.com.microservices.productapi.modules.sale.rabbitmq;

import br.com.microservices.productapi.modules.sale.dto.SaleConfirmation;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SaleConfirmationSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${app-config.rabbit.exchange.product}")
    private String productTopicExchange;

    @Value("${app-config.rabbit.routingKey.sales-confirmation}")
    private String salesConfirmationRoutingKey;

    public void sendSalesConfirmationMessage(SaleConfirmation message) {
        try {
            log.info("Sending Message: {}", new ObjectMapper().writeValueAsString(message));
            rabbitTemplate.convertAndSend(productTopicExchange, salesConfirmationRoutingKey, message);
            log.info("Message Sent");
        } catch ( Exception e ) {
            log.error("Error sending Message", e);
        }
    }
}
