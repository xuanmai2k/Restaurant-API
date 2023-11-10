package com.r2s.mobilestore.product.entities;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "properties")
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "product_code", nullable = false)
    private String productCode;

    @Column(name = "capacity", nullable = false)
    private String capacity;

    @Column(name = "color", nullable = false)
    private String color;

    @Column(name = "configuration", nullable = false)
    private String configuration;

    @Column(name = "connection_support", nullable = false)
    private String connectionSupport;

    @Column(name = "capital_price", nullable = false)
    private String capitalPrice;

    @Column(name = "price", nullable = false)
    private String price;

    @Column(name = "preferential_price", nullable = false)
    private String preferentialPrice;

    @Column(name = "quantity", nullable = false)
    private String quantity;
}
