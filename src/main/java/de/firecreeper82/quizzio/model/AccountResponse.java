package de.firecreeper82.quizzio.model;

import de.firecreeper82.quizzio.data.AccountStatus;

import java.util.List;

public record AccountResponse(
        String userName,
        String displayName,
        String email,
        AccountStatus status,
        List<SetResponse> sets
) {
}
