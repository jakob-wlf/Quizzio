package de.firecreeper82.quizzio.service;

import de.firecreeper82.quizzio.entity.FlashcardEntity;
import de.firecreeper82.quizzio.entity.SetEntity;
import de.firecreeper82.quizzio.exception.QuizzioException;
import de.firecreeper82.quizzio.model.FlashcardResponse;
import de.firecreeper82.quizzio.model.SetResponse;
import de.firecreeper82.quizzio.repository.FlashcardRepository;
import de.firecreeper82.quizzio.repository.SetRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SetService {

    private final FlashcardRepository flashcardRepository;
    private final SetRepository setRepository;

    public SetService(FlashcardRepository flashcardRepository, SetRepository setRepository) {
        this.flashcardRepository = flashcardRepository;
        this.setRepository = setRepository;
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

    public void verifyIfAuthorizedToChangeSet(String accountId, SetResponse set) throws QuizzioException {
        if(!accountId.equals(set.userId()))
            throw new QuizzioException("Unauthorized to change set belonging to account with user name " + set.userId() + ".", HttpStatus.UNAUTHORIZED);
    }

    public void verifyIfAuthorizedToChangeSet(String accountId, SetEntity set) throws QuizzioException {
        if(!accountId.equals(set.getUserId()))
            throw new QuizzioException("Unauthorized to change set belonging to account with user name " + set.getUserId() + ".", HttpStatus.UNAUTHORIZED);
    }

    public SetEntity getSetEntity(String id) throws QuizzioException {
        return setRepository
                .findById(id)
                .orElseThrow(() ->
                        new QuizzioException("Set with id " + id + " not found.", HttpStatus.BAD_REQUEST));
    }

    public SetResponse verifySet(String setId) throws QuizzioException {
        SetEntity entity = setRepository
                .findById(setId)
                .orElseThrow(() ->
                        new QuizzioException("Set with id " + setId + " not found.", HttpStatus.BAD_REQUEST));
        return createSetResponse(entity);
    }

    private FlashcardResponse mapToResponse(FlashcardEntity flashcardEntity) {
        return new FlashcardResponse(
                flashcardEntity.getId(),
                flashcardEntity.getCardKey(),
                flashcardEntity.getCardValue());
    }

}
