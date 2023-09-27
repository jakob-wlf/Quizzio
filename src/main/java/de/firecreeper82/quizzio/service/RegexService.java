package de.firecreeper82.quizzio.service;

import org.springframework.stereotype.Service;

@Service
public class RegexService {

    private final String USERNAME_REGEX;
    private final String PASSWORD_REGEX;
    private final String EMAIL_REGEX;

    public RegexService() {
        USERNAME_REGEX = "^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){3,18}[a-zA-Z0-9]$";
        PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)[a-zA-Z\\d\\w\\W]{8,256}$";
        EMAIL_REGEX = "^([a-zA-Z0-9_\\-.]+)@([a-zA-Z0-9_\\-]+)(\\.[a-zA-Z]{2,5}){1,2}$";
    }

    public boolean isValidUsername(String username) {
        return username.matches(USERNAME_REGEX);
    }

    public boolean isValidPassword(String password) {
        return password.matches(PASSWORD_REGEX);
    }

    public boolean isValidEmail(String email) {
        return email.matches(EMAIL_REGEX);
    }

}
