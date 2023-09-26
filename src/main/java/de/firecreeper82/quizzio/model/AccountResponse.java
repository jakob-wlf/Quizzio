package de.firecreeper82.quizzio.model;

import java.util.List;

public record AccountResponse(
        String userName,
        String displayName,
        String email,
        List<SetResponse> sets
) {
}
