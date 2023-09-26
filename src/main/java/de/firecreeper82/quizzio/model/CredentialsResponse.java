package de.firecreeper82.quizzio.model;

public record CredentialsResponse(
        String hashedPassword,
        byte[] salt
) {
}
