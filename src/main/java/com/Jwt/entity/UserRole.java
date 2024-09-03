package com.Jwt.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Entity representing a role in the system.
 */
@Entity
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ROLES")
public class UserRole {

    /**
     * Unique identifier for the role.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private long id;

    /**
     * Name of the role.
     */
    private String name;
}
