package de.firecreeper82.quizzio.controller;

import de.firecreeper82.quizzio.entity.FlashcardEntity;
import de.firecreeper82.quizzio.entity.SetEntity;
import de.firecreeper82.quizzio.exception.QuizzioException;
import de.firecreeper82.quizzio.model.FlashcardResponse;
import de.firecreeper82.quizzio.model.SetResponse;
import de.firecreeper82.quizzio.model.UserResponse;
import de.firecreeper82.quizzio.repository.FlashcardRepository;
import de.firecreeper82.quizzio.repository.SetRepository;
import de.firecreeper82.quizzio.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class SetController {

    private final SetRepository setRepository;
    private final FlashcardRepository flashcardRepository;
    private final UserRepository userRepository;

    public SetController(SetRepository setRepository, FlashcardRepository flashcardRepository, UserRepository userRepository) {
        this.setRepository = setRepository;
        this.flashcardRepository = flashcardRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/sets/add")
    public @ResponseBody SetResponse createSet(@RequestParam String name, @RequestParam String userId) throws QuizzioException {
        SetEntity entity = new SetEntity();
        entity.setName(name);
        entity.setId(UUID.randomUUID().toString());
        entity.setUserId(userId);

        userRepository.findById(userId).orElseThrow(() -> new QuizzioException("The User with id " + userId + " could not been found.", HttpStatus.BAD_REQUEST));
        setRepository.save(entity);

        return new SetResponse(
                entity.getId(),
                entity.getName(),
                new ArrayList<>()
        );
    }

    @PutMapping("/sets/{id}")
    public @ResponseBody SetResponse changeSet(@PathVariable String id, @RequestParam(required = false) String name) throws QuizzioException {
        SetEntity entity = setRepository.findById(id).orElseThrow(() -> new QuizzioException("The Set with id " + id + " could not been found.", HttpStatus.BAD_REQUEST));
        if(name != null)
            entity.setName(name);
        setRepository.save(entity);
        return createSetResponse(entity);
    }

    @DeleteMapping("/sets/{id}")
    public @ResponseBody HttpStatus deleteSet(@PathVariable String id) throws QuizzioException {
        SetEntity entity = setRepository.findById(id).orElseThrow(() -> new QuizzioException("The Set with id " + id + " could not been found.", HttpStatus.BAD_REQUEST));

        flashcardRepository
                .deleteAll(flashcardRepository
                .findAllBySetId(entity.getId()));

        setRepository.delete(entity);
        return HttpStatus.OK;
    }

    @GetMapping("/sets/{id}")
    public @ResponseBody SetResponse getSetById(@PathVariable String id) throws QuizzioException {
        SetEntity entity = setRepository.findById(id).orElseThrow(() -> new QuizzioException("The Set with id " + id + " could not been found.", HttpStatus.BAD_REQUEST));
        return createSetResponse(entity);
    }

    public @ResponseBody SetResponse createSetResponse(SetEntity entity) {
        List<FlashcardResponse> flashcards = flashcardRepository
                .findAllBySetId(entity.getId())
                .stream()
                .map(this::mapToResponse)
                .toList();

        return new SetResponse(
                entity.getId(),
                entity.getName(),
                flashcards
        );
    }

    private FlashcardResponse mapToResponse(FlashcardEntity flashcardEntity) {
        return new FlashcardResponse(
                flashcardEntity.getId(),
                flashcardEntity.getCardKey(),
                flashcardEntity.getCardValue());
    }
}
