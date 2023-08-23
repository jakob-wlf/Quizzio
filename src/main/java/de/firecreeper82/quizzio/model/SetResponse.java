package de.firecreeper82.quizzio.model;

import java.util.List;

public record SetResponse(
        String id,
        String name,
        List<FlashcardResponse> cards
) { }
