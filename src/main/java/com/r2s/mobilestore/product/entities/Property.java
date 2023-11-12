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

    @Column(name = "capacity")
    private String capacity;

    @Column(name = "color")
    private String color;

    @Column(name = "configuration")
    private String configuration;

    @Column(name = "connection_support")
    private String connectionSupport;

    @Column(name = "capital_price")
    private String capitalPrice;

    @Column(name = "price")
    private String price;

    @Column(name = "preferential_price")
    private String preferentialPrice;

    @Column(name = "quantity")
    private String quantity;
}
