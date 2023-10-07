package de.firecreeper82.quizzio.controller;

import de.firecreeper82.quizzio.entity.SetEntity;
import de.firecreeper82.quizzio.exception.QuizzioException;
import de.firecreeper82.quizzio.model.SessionResponse;
import de.firecreeper82.quizzio.model.SetResponse;
import de.firecreeper82.quizzio.model.TrainingResponse;
import de.firecreeper82.quizzio.repository.AccountRepository;
import de.firecreeper82.quizzio.repository.FlashcardRepository;
import de.firecreeper82.quizzio.repository.SetRepository;
import de.firecreeper82.quizzio.service.SessionService;
import de.firecreeper82.quizzio.service.SetService;
import de.firecreeper82.quizzio.service.TrainingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
public class SetController {

    private final SetRepository setRepository;
    private final FlashcardRepository flashcardRepository;
    private final AccountRepository accountRepository;
    private final SetService setService;
    private final TrainingService trainingService;
    private final SessionService sessionService;

    public SetController(SetRepository setRepository, FlashcardRepository flashcardRepository, AccountRepository accountRepository, SetService setService, TrainingService trainingService, SessionService sessionService) {
        this.setRepository = setRepository;
        this.flashcardRepository = flashcardRepository;
        this.accountRepository = accountRepository;
        this.setService = setService;
        this.trainingService = trainingService;
        this.sessionService = sessionService;
    }

    @PostMapping("sets/add")
    public @ResponseBody SetResponse createSet(@RequestParam String sessionId, @RequestParam String name) throws QuizzioException {
        SessionResponse session = sessionService.verifySession(sessionId);
        String userId = session.accountId();

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

    @PutMapping("sets/{id}")
    public @ResponseBody SetResponse changeSet(@RequestParam String sessionId, @PathVariable String id, @RequestParam(required = false) String name) throws QuizzioException {
        SetEntity entity = setService.getSetEntity(id);

        SessionResponse session = sessionService.verifySession(sessionId);

        setService.verifyIfAuthorizedToChangeSet(session.accountId(), entity);

        if(name != null)
            entity.setName(name);
        setRepository.save(entity);
        return setService.createSetResponse(entity);
    }

    @DeleteMapping("sets/{id}")
    public ResponseEntity<String> deleteSet(@RequestParam String sessionId, @PathVariable String id) throws QuizzioException {
        SetEntity entity = setService.getSetEntity(id);

        SessionResponse session = sessionService.verifySession(sessionId);

        setService.verifyIfAuthorizedToChangeSet(session.accountId(), entity);

        flashcardRepository
                .deleteAll(flashcardRepository
                .findAllBySetId(entity.getId()));

        setRepository.delete(entity);
        return ResponseEntity.status(HttpStatus.OK).body("Deleted set with id " + id + ".");
    }

    @GetMapping("/sets/{id}")
    public @ResponseBody SetResponse getSetById(@PathVariable String id) throws QuizzioException {
        return setService.verifySet(id);
    }

    @PostMapping("/trainings/start/{setId}")
    public @ResponseBody TrainingResponse startTraining(@RequestParam String sessionId, @PathVariable String setId) throws QuizzioException {
        SessionResponse session = sessionService.verifySession(sessionId);
        SetResponse set = setService.verifySet(setId);

        return trainingService.startTraining(session, set);
    }

    @PostMapping("/trainings/answer")
    public @ResponseBody TrainingResponse answer(@RequestParam String sessionId, @RequestParam String answer) throws QuizzioException {
        SessionResponse session = sessionService.verifySession(sessionId);

        return trainingService.train(session, answer);
    }

    @GetMapping("/sets/search/{tag}")
    public @ResponseBody List<SetResponse> searchSets(@PathVariable String tag) {
        List<SetEntity> sets = setRepository.findAllBySetName(tag);
        return sets.stream().map(setService::createSetResponse).collect(Collectors.toList());
    }
}
