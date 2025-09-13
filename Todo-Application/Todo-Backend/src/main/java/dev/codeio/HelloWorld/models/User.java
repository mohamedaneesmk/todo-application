package dev.codeio.HelloWorld.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")  // ✅ fixes reserved keyword issue
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ✅ good practice for PostgreSQL
    private Long id;

    @Email
    @NotNull
    private String email;

    @NotBlank
    @NotNull
    private String password;
}
