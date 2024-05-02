package br.com.microservices.productapi.config.interceptor;

import br.com.microservices.productapi.config.exception.ValidationException;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

@Component
public class FeignClientAuthInterceptor implements RequestInterceptor {

    private static final String AUTHORIZATION_HEADER = "Authorization";

    @Override
    public void apply(RequestTemplate requestTemplate) {
        var currentRequest = getHttpServletRequest();
        requestTemplate
                .header(AUTHORIZATION_HEADER, currentRequest.getHeader(AUTHORIZATION_HEADER));
    }

    private HttpServletRequest getHttpServletRequest() {
        try {
            return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder
                    .getRequestAttributes()))
                    .getRequest();
        } catch (Exception e) {
            throw new ValidationException("The current request could not be processed.");
        }
    }
}
