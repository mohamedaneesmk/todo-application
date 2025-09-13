package dev.codeio.HelloWorld.service;

import dev.codeio.HelloWorld.models.User;
import dev.codeio.HelloWorld.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service layer: Handles business logic for Todos.
 * Works between Controller and Repository.
 */
@Service
public class UserService {

    // Autowired = Spring injects UserRepository automatically
    @Autowired
    private UserRepository userRepository;

    /** Create a new Todo */
    public User createUser(User user) {
        return userRepository.save(user);
    }

    /** Get a Todo by ID */
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }


}
