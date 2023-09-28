package de.firecreeper82.quizzio.service;

import de.firecreeper82.quizzio.entity.AccountEntity;
import de.firecreeper82.quizzio.entity.SessionEntity;
import de.firecreeper82.quizzio.exception.QuizzioException;
import de.firecreeper82.quizzio.model.SessionResponse;
import de.firecreeper82.quizzio.repository.AccountRepository;
import de.firecreeper82.quizzio.repository.SessionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class SessionService {

    private final AccountRepository accountRepository;
    private final SessionRepository sessionRepository;
    private final SecurityService securityService;

    public SessionService(AccountRepository accountRepository, SessionRepository sessionRepository, SecurityService securityService) {
        this.accountRepository = accountRepository;
        this.sessionRepository = sessionRepository;
        this.securityService = securityService;
    }

    public SessionResponse login(String userName, String password) throws QuizzioException {

        AccountEntity entity = accountRepository
                .findById(userName)
                .orElseThrow(() ->
                new QuizzioException("User with username " + userName + " not found.", HttpStatus.NOT_FOUND));

        if(!securityService.verifyCredentials(entity, password))
            throw new QuizzioException("Invalid Credentials.", HttpStatus.UNAUTHORIZED);

        return createSession(userName);
    }

    private SessionResponse createSession(String userName) {
        String sessionId = securityService.generateId();
        SessionEntity entity = new SessionEntity();
        entity.setAccountId(userName);
        entity.setSessionId(sessionId);

        sessionRepository.save(entity);

        return mapToResponse(entity);
    }

    private SessionResponse mapToResponse(SessionEntity entity) {
        return new SessionResponse(
                entity.getAccountId(),
                entity.getSessionId()
        );
    }

    public void logout(String sessionId) throws QuizzioException{
        SessionEntity entity = sessionRepository
                .findById(sessionId)
                .orElseThrow(() ->
                        new QuizzioException("Session with id " + sessionId + " not found.", HttpStatus.NOT_FOUND));

        sessionRepository.delete(entity);
    }

    public SessionResponse verifySession(String sessionId) throws QuizzioException {
        SessionEntity entity = sessionRepository
                .findById(sessionId)
                .orElseThrow(() ->
                        new QuizzioException("Session with id " + sessionId + " not found.", HttpStatus.NOT_FOUND));
        return mapToResponse(entity);
    }
}
