package br.com.microservices.productapi.modules.product.model;

import br.com.microservices.productapi.modules.category.model.Category;
import br.com.microservices.productapi.modules.product.dto.ProductRequest;
import br.com.microservices.productapi.modules.supplier.model.Supplier;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "quantity_available", nullable = false)
    private Integer quantityAvailable;

    @ManyToOne
    @JoinColumn(name = "fk_category", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "fk_supplier", nullable = false)
    private Supplier supplier;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public static Product of(ProductRequest request,
                             Supplier supplier,
                             Category category) {
        return Product
                .builder()
                .name(request.getName())
                .quantityAvailable(request.getQuantityAvailable())
                .supplier(supplier)
                .category(category)
                .build();
    }

    public void updateQuantityAvailable(Integer quantity) {
        quantityAvailable = quantityAvailable - quantity;
    }
}
