package com.r2s.mobilestore.manufacturer.entities;

import jakarta.persistence.*;
import lombok.*;

/**
 * Represents a manufacturer
 *
 * @author xuanmai
 * @since 2023-11-07
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
    @Column(name = "manufacturer_name", nullable = false, unique = true)
    private String manufacturerName;

}
