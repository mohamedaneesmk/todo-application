package dev.codeio.HelloWorld.repository;

import dev.codeio.HelloWorld.models.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for Todo entity.
 * Extends JpaRepository to provide CRUD operations:
 * save, findById, findAll, delete, etc.
 */
public interface TodoRepository extends JpaRepository<Todo, Long> {
    // No need to write any method; JpaRepository already gives CRUD.

}
