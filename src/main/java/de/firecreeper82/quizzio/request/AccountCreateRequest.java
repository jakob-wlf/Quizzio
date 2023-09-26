package de.firecreeper82.quizzio.request;

public record AccountCreateRequest(
        String userName,
        String password,
        String displayName,
        String email
) { }
