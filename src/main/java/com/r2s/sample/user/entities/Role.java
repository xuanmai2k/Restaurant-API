package com.r2s.sample.user.entities;

import jakarta.persistence.*;
import lombok.*;

/**
 * Models for Authorization (Role).
 *
 * @author kyle
 * @since 2023-09-08
 */
@Entity
@Table(name = "roles")
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class Role {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false, length = 50, unique = true)
  @NonNull
  private String name;
}