package service;

import entity.User;
import exception.*;

import java.util.*;

public class UserService {
    private Map<String, User> users = new HashMap<>();

    public void registerUser(String name, String email, String location) throws InvalidInputException {
        if (name == null || name.trim().isEmpty() || email == null || email.trim().isEmpty() || location == null || location.trim().isEmpty()) {
            throw new InvalidInputException("Name, email, and location cannot be empty.");
        }
        if (users.containsKey(email)) {
            System.out.println("User with email " + email + " already registered.");
            return;
        }
        users.put(email, new User(name, email, location));
        System.out.println("User registered successfully: " + name);
    }

    public User findUserByEmail(String email) throws UserNotFoundException {
        User user = users.get(email);
        if (user == null) {
            throw new UserNotFoundException("User not found with email: " + email);
        }
        return user;
    }
}
