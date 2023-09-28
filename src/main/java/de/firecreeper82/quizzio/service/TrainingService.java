package de.firecreeper82.quizzio.service;

import de.firecreeper82.quizzio.data.AnswerStatus;
import de.firecreeper82.quizzio.exception.QuizzioException;
import de.firecreeper82.quizzio.model.FlashcardResponse;
import de.firecreeper82.quizzio.model.SessionResponse;
import de.firecreeper82.quizzio.model.SetResponse;
import de.firecreeper82.quizzio.model.TrainingResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
public class TrainingService {
    ArrayList<TrainingResponse> trainings;

    public TrainingService() {
        trainings = new ArrayList<>();
    }

    public TrainingResponse startTraining(SessionResponse session, SetResponse set) {
        trainings.removeIf(trainingResponse -> trainingResponse.userName().equals(session.accountId()));

        List<FlashcardResponse> cards = new ArrayList<>(set.cards());
        FlashcardResponse currentCard = cards.get(new Random().nextInt(cards.size()));
        cards.remove(currentCard);

        TrainingResponse trainingResponse = new TrainingResponse(
                UUID.randomUUID().toString(),
                AnswerStatus.NONE,
                session.accountId(),
                set.id(),
                List.of(),
                currentCard,
                cards

        );

        trainings.add(trainingResponse);
        return trainingResponse;
    }

    public TrainingResponse train(SessionResponse session, String answer) throws QuizzioException {
        TrainingResponse training = trainings
                .stream()
                .filter(trainingResponse -> trainingResponse.userName().equals(session.accountId()))
                .findFirst()
                .orElseThrow(() ->
                        new QuizzioException("Set for session with id " + session.sessionId() + " not found.", HttpStatus.NOT_FOUND));

        trainings.remove(training);

        FlashcardResponse learnedCard = training.currentCard();
        List<FlashcardResponse> learned = new ArrayList<>(training.learned());
        List<FlashcardResponse> notLearned = new ArrayList<>(training.notLearned());
        AnswerStatus answerStatus;

        if(answer.equalsIgnoreCase(learnedCard.value())) {
            learned.add(learnedCard);
            answerStatus = AnswerStatus.CORRECT;
        }
        else {
            notLearned.add(learnedCard);
            answerStatus = AnswerStatus.INCORRECT;
        }

        FlashcardResponse currentCard = notLearned.get(new Random().nextInt(notLearned.size()));
        notLearned.remove(currentCard);

        training = new TrainingResponse(
                training.id(),
                answerStatus,
                training.userName(),
                training.setId(),
                learned,
                currentCard,
                notLearned
        );

        trainings.add(training);
        return training;
    }
}
