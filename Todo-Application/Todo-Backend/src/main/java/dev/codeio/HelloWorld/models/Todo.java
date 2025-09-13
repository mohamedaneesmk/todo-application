package dev.codeio.HelloWorld.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * Todo Entity (Database table representation).
 * Lombok @Data generates getters, setters, equals, hashCode, and toString.
 */
@Entity
@Data
public class Todo {

    @Id
    @GeneratedValue // Auto-increment ID
    Long id;

    @NotNull
    @NotBlank
    @Schema(name = "title",example = "Complete Spring Boot")
    String title;

    @NotNull
    @NotBlank
    @Size(min = 5, max = 100, message = "Description must be between 5 and 100 characters")
    String description;

    @NotNull
    Boolean isCompleted;

    @Email(message = "Please provide a valid email")
    @NotBlank
    String email;
}
