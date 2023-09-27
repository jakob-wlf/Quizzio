package de.firecreeper82.quizzio.service;

import de.firecreeper82.quizzio.entity.FlashcardEntity;
import de.firecreeper82.quizzio.entity.SetEntity;
import de.firecreeper82.quizzio.model.FlashcardResponse;
import de.firecreeper82.quizzio.model.SetResponse;
import de.firecreeper82.quizzio.repository.FlashcardRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SetService {

    private final FlashcardRepository flashcardRepository;

    public SetService(FlashcardRepository flashcardRepository) {
        this.flashcardRepository = flashcardRepository;
    }

    public SetResponse createSetResponse(SetEntity entity) {
        List<FlashcardResponse> flashcards = flashcardRepository
                .findAllBySetId(entity.getId())
                .stream()
                .map(this::mapToResponse)
                .toList();

        return new SetResponse(
                entity.getUserId(),
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
