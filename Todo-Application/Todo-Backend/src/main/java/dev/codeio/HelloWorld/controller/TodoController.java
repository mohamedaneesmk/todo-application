package dev.codeio.HelloWorld.controller;

import dev.codeio.HelloWorld.service.TodoService;
import dev.codeio.HelloWorld.models.Todo;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for managing Todos.
 * Base path: /api/v1/todo
 */
@RestController
@RequestMapping("/api/v1/todo")
public class TodoController {

    @Autowired
    private TodoService todoService;

    /**
     * Get a Todo by ID
     * Example: GET /api/v1/todo/1
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Todo Retrieved Successfully"),
            @ApiResponse(responseCode = "404", description = "Todo was not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Todo> getTodoByID(@PathVariable long id) {
        try {
            Todo todo = todoService.getTodoById(id);
            return new ResponseEntity<>(todo, HttpStatus.OK);
        } catch (RuntimeException exception) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Get all Todos (⚠️ shows all users’ todos, usually for admin/debug)
     */
    @GetMapping
    public ResponseEntity<List<Todo>> getAllTodos() {
        return new ResponseEntity<>(todoService.getAllTodos(), HttpStatus.OK);
    }

    /**
     * Get Todos by email (recommended for user-specific access)
     * Example: GET /api/v1/todo/user/test@email.com
     */
    @GetMapping("/user/{email}")
    public ResponseEntity<List<Todo>> getTodosByUser(@PathVariable String email) {
        return new ResponseEntity<>(todoService.getTodosByEmail(email), HttpStatus.OK);
    }

    /**
     * Get paginated Todos
     * Example: GET /api/v1/todo/page?page=0&size=5
     */
    @GetMapping("/page")
    public ResponseEntity<Page<Todo>> getTodosPaged(@RequestParam int page, @RequestParam int size) {
        return new ResponseEntity<>(todoService.getAllTodos(page, size), HttpStatus.OK);
    }

    /**
     * Create a new Todo
     * Example: POST /api/v1/todo/create
     * Body: { "title": "Buy milk", "description": "2 liters", "isCompleted": false, "email": "user@email.com" }
     */
    @PostMapping("/create")
    public ResponseEntity<Todo> createTodo(@RequestBody Todo todo) {
        Todo createdTodo = todoService.createTodo(todo);
        return new ResponseEntity<>(createdTodo, HttpStatus.CREATED);
    }

    /**
     * Update an existing Todo
     * Example: PUT /api/v1/todo
     * Body: { "id": 1, "title": "Updated", "description": "Updated", "isCompleted": true, "email": "user@email.com" }
     */
    @PutMapping
    public ResponseEntity<Todo> updateTodoById(@RequestBody Todo todo) {
        try {
            Todo updatedTodo = todoService.updateTodoById(todo);
            return new ResponseEntity<>(updatedTodo, HttpStatus.OK);
        } catch (RuntimeException exception) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Delete a Todo by ID
     * Example: DELETE /api/v1/todo/1
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodoByID(@PathVariable long id) {
        try {
            todoService.deleteTodoById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException exception) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
