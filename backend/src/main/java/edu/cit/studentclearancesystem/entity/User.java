package edu.cit.studentclearancesystem.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String googleId;

    @Column(nullable = false, unique = true)
    private String email;

    private String fullName;

    @Enumerated(EnumType.STRING)
    private Role role; // Enum: STUDENT, DEPT_HEAD, REGISTRAR

    private String profileUrl;
}
