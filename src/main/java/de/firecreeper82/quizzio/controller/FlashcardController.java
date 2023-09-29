package de.firecreeper82.quizzio.controller;

import de.firecreeper82.quizzio.entity.FlashcardEntity;
import de.firecreeper82.quizzio.exception.QuizzioException;
import de.firecreeper82.quizzio.model.SessionResponse;
import de.firecreeper82.quizzio.model.SetResponse;
import de.firecreeper82.quizzio.repository.FlashcardRepository;
import de.firecreeper82.quizzio.service.SessionService;
import de.firecreeper82.quizzio.service.SetService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class FlashcardController {

    private final FlashcardRepository flashcardRepository;
    private final SetService setService;
    private final SessionService sessionService;

    public FlashcardController(FlashcardRepository flashcardRepository, SetService setService, SessionService sessionService) {
        this.flashcardRepository = flashcardRepository;
        this.setService = setService;
        this.sessionService = sessionService;
    }

    @PostMapping("/flashcards/add/{setId}")
    public @ResponseBody SetResponse createFlashcard(@RequestParam String sessionId, @RequestParam String key, @RequestParam String value, @PathVariable String setId) throws QuizzioException {
        SessionResponse session = sessionService.verifySession(sessionId);
        SetResponse set = setService.verifySet(setId);

        setService.verifyIfAuthorizedToChangeSet(session.accountId(), set);

        FlashcardEntity entity = new FlashcardEntity();
        entity.setId(UUID.randomUUID().toString());
        entity.setCardKey(key);
        entity.setCardValue(value);
        entity.setSetId(setId);

        flashcardRepository.save(entity);

        return setService.verifySet(setId);
    }

    @PutMapping("/flashcards/{id}")
    public @ResponseBody SetResponse changeFlashcard(@RequestParam String sessionId, @PathVariable String id, @RequestParam(required = false) String key, @RequestParam(required = false) String value) throws QuizzioException {
        SessionResponse session = sessionService.verifySession(sessionId);

        FlashcardEntity flashcard = verifyFlashcard(id);
        if (key != null)
            flashcard.setCardKey(key);
        if (value != null)
            flashcard.setCardValue(value);

        SetResponse set = setService.verifySet(flashcard.getSetId());
        setService.verifyIfAuthorizedToChangeSet(session.accountId(), set);

        flashcardRepository.save(flashcard);

        return setService.verifySet(flashcard.getSetId());
    }

    private FlashcardEntity verifyFlashcard(String id) throws QuizzioException {
        return flashcardRepository.findById(id).orElseThrow(() -> new QuizzioException("Flashcard with id " + id + " not found", HttpStatus.BAD_REQUEST));
    }

    @DeleteMapping("/flashcards/{id}")
    public @ResponseBody SetResponse deleteFlashCard(@RequestParam String sessionId, @PathVariable String id) throws QuizzioException {
        FlashcardEntity flashcard = verifyFlashcard(id);

        SessionResponse session = sessionService.verifySession(sessionId);
        SetResponse set = setService.verifySet(flashcard.getSetId());

        setService.verifyIfAuthorizedToChangeSet(session.accountId(), set);

        flashcardRepository.delete(flashcard);

        return setService.verifySet(flashcard.getSetId());
    }
}
