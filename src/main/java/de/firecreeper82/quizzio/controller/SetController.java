package de.firecreeper82.quizzio.controller;

import de.firecreeper82.quizzio.entity.SetEntity;
import de.firecreeper82.quizzio.exception.QuizzioException;
import de.firecreeper82.quizzio.model.SetResponse;
import de.firecreeper82.quizzio.repository.AccountRepository;
import de.firecreeper82.quizzio.repository.FlashcardRepository;
import de.firecreeper82.quizzio.repository.SetRepository;
import de.firecreeper82.quizzio.service.SetService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.UUID;

@RestController
public class SetController {

    private final SetRepository setRepository;
    private final FlashcardRepository flashcardRepository;
    private final AccountRepository accountRepository;
    private final SetService setService;

    public SetController(SetRepository setRepository, FlashcardRepository flashcardRepository, AccountRepository accountRepository, SetService setService) {
        this.setRepository = setRepository;
        this.flashcardRepository = flashcardRepository;
        this.accountRepository = accountRepository;
        this.setService = setService;
    }

    @PostMapping("/sets/add")
    public @ResponseBody SetResponse createSet(@RequestParam String name, @RequestParam String userId) throws QuizzioException {
        SetEntity entity = new SetEntity();
        entity.setName(name);
        entity.setId(UUID.randomUUID().toString());
        entity.setUserId(userId);

        accountRepository
                .findById(userId)
                .orElseThrow(() ->
                        new QuizzioException("User with username " + userId + " not found.", HttpStatus.BAD_REQUEST));

        setRepository.save(entity);

        return new SetResponse(
                entity.getUserId(),
                entity.getId(),
                entity.getName(),
                new ArrayList<>()
        );
    }

    @PutMapping("/sets/{id}")
    public @ResponseBody SetResponse changeSet(@PathVariable String id, @RequestParam(required = false) String name) throws QuizzioException {
        SetEntity entity = setRepository
                .findById(id)
                .orElseThrow(() ->
                        new QuizzioException("Set with id " + id + " not found..", HttpStatus.BAD_REQUEST));

        if(name != null)
            entity.setName(name);
        setRepository.save(entity);
        return setService.createSetResponse(entity);
    }

    @DeleteMapping("/sets/{id}")
    public @ResponseBody HttpStatus deleteSet(@PathVariable String id) throws QuizzioException {
        SetEntity entity = setRepository
                .findById(id)
                .orElseThrow(() ->
                        new QuizzioException("Set with id " + id + " not found.", HttpStatus.BAD_REQUEST));

        flashcardRepository
                .deleteAll(flashcardRepository
                .findAllBySetId(entity.getId()));

        setRepository.delete(entity);
        return HttpStatus.OK;
    }

    @GetMapping("/sets/{id}")
    public @ResponseBody SetResponse getSetById(@PathVariable String id) throws QuizzioException {
        SetEntity entity = setRepository
                .findById(id)
                .orElseThrow(() ->
                        new QuizzioException("Set with id " + id + " not found.", HttpStatus.BAD_REQUEST));
        return setService.createSetResponse(entity);
    }
}
