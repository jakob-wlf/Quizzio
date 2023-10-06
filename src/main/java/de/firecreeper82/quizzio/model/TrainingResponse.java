package de.firecreeper82.quizzio.model;


import de.firecreeper82.quizzio.data.AnswerStatus;

import java.util.List;

public record TrainingResponse(
        String id,
        AnswerStatus lastAnswered,
        String userName,
        String setId,
        List<FlashcardResponse> learned,
        FlashcardResponse currentCard,
        List<FlashcardResponse> notLearned,
        int wrongAnswers,
        boolean finished
) {
}
