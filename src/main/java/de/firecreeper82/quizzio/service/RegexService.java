package de.firecreeper82.quizzio.service;

import de.firecreeper82.quizzio.exception.QuizzioException;
import org.springframework.http.HttpStatus;
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

    public boolean isInvalidUsername(String username) {
        return !username.matches(USERNAME_REGEX);
    }

    public boolean isInvalidPassword(String password) {
        return !password.matches(PASSWORD_REGEX);
    }

    public boolean isInvalidEmail(String email) {
        return !email.matches(EMAIL_REGEX);
    }

    public void checkForValidDisplayName(String displayName) throws QuizzioException {
        if(isInvalidUsername(displayName))
            throw new QuizzioException("Invalid display name.", HttpStatus.BAD_REQUEST);
    }
    public void checkForValidUsername(String userName) throws QuizzioException {
        if(isInvalidUsername(userName))
            throw new QuizzioException("Invalid username.", HttpStatus.BAD_REQUEST);
    }

    public void checkForValidPassword(String password) throws QuizzioException {
        if(isInvalidPassword(password))
            throw new QuizzioException("Invalid password.", HttpStatus.BAD_REQUEST);
    }

    public void checkForValidEmail(String email) throws QuizzioException {
        if(isInvalidEmail(email))
            throw new QuizzioException("Invalid email.", HttpStatus.BAD_REQUEST);    }


}
