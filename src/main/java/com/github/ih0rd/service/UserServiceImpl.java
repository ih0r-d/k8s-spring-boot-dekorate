package com.github.ih0rd.service;

import com.github.ih0rd.entity.User;
import com.github.ih0rd.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserById(UUID id) {
        return userRepository.findById(id);
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User updateUser(UUID id, User userDetails) {
        return userRepository.findById(id)
                .map(user -> {
                    user = new User(
                            id,
                            userDetails.getFirstName(),
                            userDetails.getLastName(),
                            userDetails.getAge(),
                            userDetails.getEmail(),
                            userDetails.getRegistrationDate()
                    );
                    return userRepository.save(user);
                })
                .orElseGet(() -> userRepository.save(userDetails));
    }

    @Override
    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }
}
