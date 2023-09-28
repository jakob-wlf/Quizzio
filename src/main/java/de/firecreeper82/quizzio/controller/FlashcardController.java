package de.firecreeper82.quizzio.controller;

import de.firecreeper82.quizzio.entity.FlashcardEntity;
import de.firecreeper82.quizzio.entity.SetEntity;
import de.firecreeper82.quizzio.exception.QuizzioException;
import de.firecreeper82.quizzio.model.SetResponse;
import de.firecreeper82.quizzio.repository.FlashcardRepository;
import de.firecreeper82.quizzio.repository.SetRepository;
import de.firecreeper82.quizzio.service.SetService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class FlashcardController {

    private final FlashcardRepository flashcardRepository;
    private final SetRepository setRepository;
    private final SetService setService;

    public FlashcardController(FlashcardRepository flashcardRepository, SetRepository setRepository, SetService setService) {
        this.flashcardRepository = flashcardRepository;
        this.setRepository = setRepository;
        this.setService = setService;
    }

    //TODO: require session to change Flashcards

    @PostMapping("/flashcards/add/{setId}")
    public @ResponseBody SetResponse createFlashcard(@RequestParam String key, @RequestParam String value, @PathVariable String setId) throws QuizzioException {
        FlashcardEntity entity = new FlashcardEntity();
        entity.setId(UUID.randomUUID().toString());
        entity.setCardKey(key);
        entity.setCardValue(value);
        entity.setSetId(setId);

        SetEntity setEntity = setRepository.findById(setId).orElseThrow(() -> new QuizzioException("Set with id " + setId + " not found.", HttpStatus.BAD_REQUEST));
        flashcardRepository.save(entity);

        return setService.createSetResponse(setEntity);
    }

    @PutMapping("/flashcards/{id}")
    public @ResponseBody SetResponse changeFlashcard(@PathVariable String id, @RequestParam(required = false) String key, @RequestParam(required = false) String value) throws QuizzioException {
        FlashcardEntity flashcard = flashcardRepository.findById(id).orElseThrow(() -> new QuizzioException("Flashcard with id " + id + " not found", HttpStatus.BAD_REQUEST));
        if (key != null)
            flashcard.setCardKey(key);
        if (value != null)
            flashcard.setCardValue(value);

        SetEntity setEntity = setRepository.findById(flashcard.getSetId()).orElseThrow(() -> new QuizzioException("Set with id " + flashcard.getSetId() + " not found.", HttpStatus.BAD_REQUEST));
        flashcardRepository.save(flashcard);

        return setService.createSetResponse(setEntity);
    }

    @DeleteMapping("/flashcards/{id}")
    public @ResponseBody SetResponse deleteFlashCard(@PathVariable String id) throws QuizzioException {
        FlashcardEntity flashcard = flashcardRepository.findById(id).orElseThrow(() -> new QuizzioException("Flashcard with id " + id + " not found", HttpStatus.BAD_REQUEST));

        SetEntity setEntity = setRepository.findById(flashcard.getSetId()).orElseThrow(() -> new QuizzioException("Set with id " + flashcard.getSetId() + " not found.", HttpStatus.BAD_REQUEST));
        flashcardRepository.delete(flashcard);

        return setService.createSetResponse(setEntity);
    }
}
