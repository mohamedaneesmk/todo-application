package dev.codeio.HelloWorld.service;

import dev.codeio.HelloWorld.models.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class TodoService {

    private final Map<Long, Todo> todos = new HashMap<>(); // store todos in memory
    private final AtomicLong idCounter = new AtomicLong(1);

    // Create Todo
    public Todo createTodo(Todo todo) {
        long id = idCounter.getAndIncrement();
        todo.setId(id);
        todos.put(id, todo);
        return todo;
    }

    // Get Todo by ID
    public Todo getTodoById(long id) {
        Todo todo = todos.get(id);
        if (todo == null) {
            throw new RuntimeException("Todo not found with id " + id);
        }
        return todo;
    }

    // Get all Todos
    public List<Todo> getAllTodos() {
        return new ArrayList<>(todos.values());
    }

    // Get all Todos by email
    public List<Todo> getTodosByEmail(String email) {
        return todos.values().stream()
                .filter(t -> t.getEmail().equalsIgnoreCase(email))
                .collect(Collectors.toList());
    }

    // Get Todos with pagination
    public Page<Todo> getAllTodos(int page, int size) {
        List<Todo> all = new ArrayList<>(todos.values());
        int start = page * size;
        int end = Math.min(start + size, all.size());
        if (start > all.size()) {
            return Page.empty();
        }
        return new PageImpl<>(all.subList(start, end), PageRequest.of(page, size), all.size());
    }

    // Update Todo
    public Todo updateTodoById(Todo updatedTodo) {
        if (!todos.containsKey(updatedTodo.getId())) {
            throw new RuntimeException("Todo not found with id " + updatedTodo.getId());
        }
        todos.put(updatedTodo.getId(), updatedTodo);
        return updatedTodo;
    }

    // Delete Todo
    public void deleteTodoById(long id) {
        if (!todos.containsKey(id)) {
            throw new RuntimeException("Todo not found with id " + id);
        }
        todos.remove(id);
    }
}
