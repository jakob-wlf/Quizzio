package de.firecreeper82.quizzio.controller;

import de.firecreeper82.quizzio.entity.UserEntity;
import de.firecreeper82.quizzio.exception.QuizzioException;
import de.firecreeper82.quizzio.model.UserResponse;
import de.firecreeper82.quizzio.repository.SetRepository;
import de.firecreeper82.quizzio.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
public class UserController {

    private final UserRepository userRepository;
    private final SetRepository setRepository;
    private final SetController setController;

    public UserController(UserRepository userRepository, SetRepository setRepository, SetController setController) {
        this.userRepository = userRepository;
        this.setRepository = setRepository;
        this.setController = setController;
    }

    @PostMapping(path = "/users/add")
    public @ResponseBody UserResponse createUser(@RequestParam String userId, @RequestParam String userName, @RequestParam String email) throws QuizzioException {

        if (userRepository.findById(userId).isPresent()) {
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
                user.getEmail(),
                new ArrayList<>()
        );
    }

    @GetMapping(path = "/users")
    public @ResponseBody List<UserResponse> getAllUsers() {
        Iterable<UserEntity> userEntities = userRepository.findAll();
        return StreamSupport
                .stream(userEntities.spliterator(), false)
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/users/{id}")
    public @ResponseBody UserResponse getUserById(@PathVariable String id) throws QuizzioException {
        UserEntity entity = userRepository.findById(id).orElseThrow(() -> new QuizzioException("The User with id " + id + " could not been found.", HttpStatus.BAD_REQUEST));
        return mapToResponse(entity);
    }

    private UserResponse mapToResponse(UserEntity entity) {
        return new UserResponse(
                entity.getUserId(),
                entity.getUsername(),
                entity.getEmail(),
                setRepository.findAllByUserId(entity.getUserId())
                        .stream()
                        .map(setController::createSetResponse)
                        .collect(Collectors.toList())
        );
    }
}
