package com.Jwt.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.util.HashSet;
import java.util.Set;

/**
 * Entity class for storing user information.
 */
@Entity
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "USERS")
public class UserInfo {

    /**
     * Primary key for the User entity.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private long id;

    /**
     * Username of the user.
     */
    private String username;

    /**
     * User's password, not included in JSON serialization for security reasons.
     */
    @JsonIgnore
    private String password;

    /**
     * Roles assigned to the user, loaded eagerly.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<UserRole> roles = new HashSet<>();
}
