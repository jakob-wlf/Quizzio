package de.firecreeper82.quizzio.model;

import java.util.List;

public record UserResponse(
        String userName,
        String displayName,
        List<String> sets
) {
}
