package com.r2s.mobilestore.category.entities;

import jakarta.persistence.*;
import lombok.*;

/**
 * Represents a category
 *
 * @author kyle
 * @since 2023-08-31
 */
@Data
@Entity
@Table(name = "categories")
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Represents the category’s name.
     */
    @Column(name = "category_name", nullable = false, unique = true)
    private String categoryName;
}