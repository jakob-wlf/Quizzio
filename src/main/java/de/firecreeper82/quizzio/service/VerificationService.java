package de.firecreeper82.quizzio.service;

import de.firecreeper82.quizzio.data.AccountStatus;
import de.firecreeper82.quizzio.entity.AccountEntity;
import de.firecreeper82.quizzio.entity.SessionEntity;
import de.firecreeper82.quizzio.entity.VerificationEntity;
import de.firecreeper82.quizzio.exception.QuizzioException;
import de.firecreeper82.quizzio.repository.AccountRepository;
import de.firecreeper82.quizzio.repository.SessionRepository;
import de.firecreeper82.quizzio.repository.VerificationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Random;

@Service
public class VerificationService {

    private final VerificationRepository verificationRepository;
    private final SessionRepository sessionRepository;
    private final AccountRepository accountRepository;

    public VerificationService(VerificationRepository verificationRepository, SessionRepository sessionRepository, AccountRepository accountRepository) {
        this.verificationRepository = verificationRepository;
        this.sessionRepository = sessionRepository;
        this.accountRepository = accountRepository;
    }

    public boolean isValidVerification(String accountId, String verificationCode) throws QuizzioException {
        VerificationEntity entity = verificationRepository
                .findById(accountId)
                .orElseThrow(() ->
                        new QuizzioException("Verification for account with username " + accountId + " not found.", HttpStatus.NOT_FOUND));

        return entity.getVerificationCode().equals(verificationCode);
    }

    public AccountEntity verifyAccount(String sessionId, String verificationCode) throws QuizzioException {
        SessionEntity session = sessionRepository
                .findById(sessionId)
                .orElseThrow(() ->
                        new QuizzioException("Session with id " + sessionId + " not found.", HttpStatus.NOT_FOUND));

        String accountId = session.getAccountId();

        AccountEntity entity = accountRepository
                .findById(accountId)
                .orElseThrow(() ->
                        new QuizzioException("User with username " + accountId + " not found.", HttpStatus.NOT_FOUND));

        if(entity.getStatus() == AccountStatus.VERIFIED)
            throw new QuizzioException("Account is already verified.", HttpStatus.BAD_REQUEST);

        if(!isValidVerification(accountId, verificationCode))
            throw new QuizzioException("Invalid verification code.", HttpStatus.BAD_REQUEST);

        entity.setStatus(AccountStatus.VERIFIED);
        accountRepository.save(entity);

        verificationRepository.deleteById(accountId);

        return entity;
    }

    public void createVerification(String userName) {
        VerificationEntity entity = new VerificationEntity();
        entity.setUserId(userName);
        entity.setVerificationCode(createVerificationCode());
        verificationRepository.save(entity);
    }

    private String createVerificationCode() {
        byte[] array = new byte[7];
        new Random().nextBytes(array);

        return new String(array, StandardCharsets.UTF_8);
    }

}
