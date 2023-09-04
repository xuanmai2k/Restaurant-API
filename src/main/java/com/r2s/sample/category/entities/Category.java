package com.r2s.sample.category.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a category
 *
 * @author kyle
 * @since 2023-08-31
 */
@Data
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * Represents the category’s name.
     */
    @Column(name = "category_name", nullable = false)
    private String categoryName;
}