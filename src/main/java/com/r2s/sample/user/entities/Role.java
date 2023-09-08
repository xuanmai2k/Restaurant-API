package com.r2s.sample.user.entities;

import com.r2s.sample.user.models.ERole;
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
@NoArgsConstructor
@RequiredArgsConstructor
public class Role {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Enumerated(EnumType.STRING)
  @Column(length = 20)
  @NonNull
  private ERole name;
}