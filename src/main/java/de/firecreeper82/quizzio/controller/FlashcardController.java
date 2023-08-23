package de.firecreeper82.quizzio.controller;

import de.firecreeper82.quizzio.entity.FlashcardEntity;
import de.firecreeper82.quizzio.entity.SetEntity;
import de.firecreeper82.quizzio.exception.QuizzioException;
import de.firecreeper82.quizzio.model.FlashcardResponse;
import de.firecreeper82.quizzio.model.SetResponse;
import de.firecreeper82.quizzio.repository.FlashcardRepository;
import de.firecreeper82.quizzio.repository.SetRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class FlashcardController {

    private final FlashcardRepository flashcardRepository;
    private final SetRepository setRepository;

    public FlashcardController(FlashcardRepository flashcardRepository, SetRepository setRepository) {
        this.flashcardRepository = flashcardRepository;
        this.setRepository = setRepository;
    }

    @PostMapping("/flashcards/add/{setId}")
    public SetResponse createFlashcard(@RequestParam String key, @RequestParam String value, @PathVariable String setId) throws QuizzioException {
        FlashcardEntity entity = new FlashcardEntity();
        entity.setId(UUID.randomUUID().toString());
        entity.setCardKey(key);
        entity.setCardValue(value);
        entity.setSetId(setId);

        SetEntity setEntity = setRepository.findById(setId).orElseThrow(() -> new QuizzioException("The set with id " + setId + " could not been found.", HttpStatus.BAD_REQUEST));
        flashcardRepository.save(entity);

        return SetController.createSetResponse(setEntity, flashcardRepository);
    }

    private FlashcardResponse mapToResponse(FlashcardEntity entity) {
        return new FlashcardResponse(
                entity.getId(),
                entity.getCardKey(),
                entity.getCardValue()
        );
    }
}
