package de.firecreeper82.quizzio.service;

import de.firecreeper82.quizzio.entity.AccountEntity;
import de.firecreeper82.quizzio.exception.QuizzioException;
import de.firecreeper82.quizzio.model.CredentialsResponse;
import org.apache.commons.codec.binary.Hex;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.UUID;

@Service
public class SecurityService {

    private static final int SALT_LENGTH = 16;

    private static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return salt;
    }

    private String hashPassword(String password, byte[] salt) throws QuizzioException {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] hashedPassword = md.digest(password.getBytes());
            return Hex.encodeHexString(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            throw new QuizzioException("No such algorithm", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public boolean verifyCredentials(AccountEntity entity, String password) throws QuizzioException {
        byte[] salt = entity.getSalt();
        String hashedPassword = hashPassword(password, salt);

        return (hashedPassword.equals(entity.getPassword()));
    }

    public CredentialsResponse createCredentials(String password) throws QuizzioException {
        try {
            byte[] salt = generateSalt();
            String hashedPassword = hashPassword(password, salt);

            return new CredentialsResponse(
                    hashedPassword,
                    salt
            );

        } catch(QuizzioException e) {
            throw new QuizzioException("Failed to create credentials", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public String generateId() {
        return UUID.randomUUID().toString();
    }
}
