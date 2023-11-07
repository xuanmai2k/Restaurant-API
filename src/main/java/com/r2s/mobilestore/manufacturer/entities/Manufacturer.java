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
@Table(name = "manufacturers", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name"})
})
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
