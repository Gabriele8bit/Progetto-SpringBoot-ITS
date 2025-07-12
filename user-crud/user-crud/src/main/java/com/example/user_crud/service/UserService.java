package com.example.user_crud.service;

import com.example.user_crud.model.User;
import com.example.user_crud.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Page<User> getAllUsers(String name, String email, Integer age, int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        String nameFilter = name != null ? name : "";
        String emailFilter = email != null ? email : "";

        if (age != null) {
            return userRepository.findByNameContainingIgnoreCaseAndEmailContainingIgnoreCaseAndAge(
                    nameFilter, emailFilter, age, pageable
            );
        } else {
            return userRepository.findByNameContainingIgnoreCaseAndEmailContainingIgnoreCase(
                    nameFilter, emailFilter, pageable
            );
        }
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(Long id, User updatedUser) {
        return userRepository.findById(id)
                .map(existing -> {
                    existing.setName(updatedUser.getName());
                    existing.setEmail(updatedUser.getEmail());
                    existing.setAge(updatedUser.getAge());
                    return userRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }
}
