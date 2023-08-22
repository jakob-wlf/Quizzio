package de.firecreeper82.quizzio.controller;

import de.firecreeper82.quizzio.entity.UserEntity;
import de.firecreeper82.quizzio.model.UserResponse;
import de.firecreeper82.quizzio.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping(path="/users/add")
    public @ResponseBody UserResponse addNewUser (@RequestParam String firstName, @RequestParam String lastName, @RequestParam String email) {

        UserEntity user = new UserEntity();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        userRepository.save(user);

        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
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
                entity.getId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getEmail()
        );
    }
}
