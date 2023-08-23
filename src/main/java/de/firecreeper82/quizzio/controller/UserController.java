package de.firecreeper82.quizzio.controller;

import de.firecreeper82.quizzio.entity.UserEntity;
import de.firecreeper82.quizzio.exception.QuizzioException;
import de.firecreeper82.quizzio.model.UserResponse;
import de.firecreeper82.quizzio.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping(path="/users/add")
    public @ResponseBody UserResponse createUser (@RequestParam String userId, @RequestParam String userName, @RequestParam String email) throws QuizzioException {

        if(userRepository.findById(userId).isPresent()) {
            throw new QuizzioException("This userid already exists.", HttpStatus.BAD_REQUEST);
        }

        UserEntity user = new UserEntity();
        user.setUserId(userId);
        user.setUsername(userName);
        user.setEmail(email);
        userRepository.save(user);

        return new UserResponse(
                user.getUserId(),
                user.getUsername(),
                user.getEmail()
        );
    }

    @GetMapping(path="/users/all")
    public @ResponseBody List<UserResponse> getAllUsers() {
        Iterable<UserEntity> userEntities = userRepository.findAll();
        return StreamSupport
                .stream(userEntities.spliterator(), false)
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private UserResponse mapToResponse(UserEntity entity) {
        return new UserResponse(
                entity.getUserId(),
                entity.getUsername(),
                entity.getEmail()
        );
    }
}
