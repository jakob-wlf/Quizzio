package de.firecreeper82.quizzio.model;

public record UserResponse(
        String userId,
        String userName,
        String email
) {}
