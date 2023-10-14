package com.r2s.mobilestore.product.entities;

import jakarta.persistence.*;
import lombok.*;

/**
 * Represents a product
 *
 * @author xuanmai
 * @since 2023-10-12
 */
@Data
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "manufacturers")
public class Manufacturer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Represents the manufacturer’s name.
     */
    @Column(name = "name", nullable = false, unique = true)
    private String manufacturerName;
}
