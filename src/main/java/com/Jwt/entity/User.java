package com.Jwt.entity;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Entity representing a user.
 */
@Entity
@Data
@ToString(exclude = "password")  // Avoid logging sensitive details.
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "USERS")
public class User {

    /**
     * Primary key.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private long id;

    /**
     * Username field.
     */
    private String username;

    /**
     * User password, excluded from JSON.
     */
    @JsonIgnore
    private String password;

    /**
     * Roles associated with the user.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<UserRole> roles = new HashSet<>();
}
