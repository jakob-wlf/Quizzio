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
    private final AccountService accountService;

    public SessionService(AccountRepository accountRepository, SessionRepository sessionRepository, SecurityService securityService, AccountService accountService) {
        this.accountRepository = accountRepository;
        this.sessionRepository = sessionRepository;
        this.securityService = securityService;
        this.accountService = accountService;
    }

    public SessionResponse login(String userName, String password) throws QuizzioException {

        AccountEntity entity = accountService.getAccountEntityById(userName);

        securityService.verifyCredentials(entity, password);

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
