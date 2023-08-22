package de.firecreeper82.quizzio.model;

public record UserResponse(
        Integer id,
        String firstName,
        String lastName,
        String email
) {}
