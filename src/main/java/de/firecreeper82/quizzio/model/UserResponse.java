package de.firecreeper82.quizzio.model;

import java.util.List;

public record UserResponse(
        String userId,
        String userName,
        String email,
        List<SetResponse> sets
) {
}
