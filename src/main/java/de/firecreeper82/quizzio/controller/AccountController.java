package de.firecreeper82.quizzio.controller;

import de.firecreeper82.quizzio.entity.AccountEntity;
import de.firecreeper82.quizzio.entity.SessionEntity;
import de.firecreeper82.quizzio.exception.QuizzioException;
import de.firecreeper82.quizzio.model.AccountResponse;
import de.firecreeper82.quizzio.model.SessionResponse;
import de.firecreeper82.quizzio.repository.AccountRepository;
import de.firecreeper82.quizzio.repository.SessionRepository;
import de.firecreeper82.quizzio.repository.SetRepository;
import de.firecreeper82.quizzio.request.AccountCreateRequest;
import de.firecreeper82.quizzio.service.AccountService;
import de.firecreeper82.quizzio.service.SessionService;
import de.firecreeper82.quizzio.service.SetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@Controller
public class AccountController {

    private final AccountService accountService;
    private final SessionService sessionService;
    private final AccountRepository accountRepository;
    private final SessionRepository sessionRepository;
    private final SetRepository setRepository;
    private final SetService setService;

    public AccountController(AccountService accountService, SessionService sessionService, AccountRepository accountRepository, SessionRepository sessionRepository, SetRepository setRepository, SetService setService) {
        this.accountService = accountService;
        this.sessionService = sessionService;
        this.accountRepository = accountRepository;
        this.sessionRepository = sessionRepository;
        this.setRepository = setRepository;
        this.setService = setService;
    }

    @PostMapping("/accounts/create")
    public ResponseEntity<String> createAccount(
            @RequestParam String userName,
            @RequestParam String email,
            @RequestParam String displayName,
            @RequestParam String password
    ) throws QuizzioException {

        accountService.createAccount(
                new AccountCreateRequest(
                        userName,
                        password,
                        displayName,
                        email
                ));


        return ResponseEntity.status(HttpStatus.CREATED).body("Account created successfully.");
    }

    @PostMapping("/accounts/login")
    public @ResponseBody SessionResponse login(@RequestParam String userName, @RequestParam String password) throws QuizzioException {
        return sessionService.login(userName, password);
    }

    @PostMapping("/accounts/logout/{sessionId}")
    public ResponseEntity<String> logout(@PathVariable String sessionId) throws QuizzioException {
        sessionService.logout(sessionId);

        return ResponseEntity.status(HttpStatus.CREATED).body("Logged out successfully.");
    }

    @GetMapping("/sessions/{sessionId}")
    public @ResponseBody AccountResponse getAccount(@PathVariable String sessionId) throws QuizzioException {
        SessionEntity session = sessionRepository
                .findById(sessionId)
                .orElseThrow(() ->
                        new QuizzioException("Session with id " + sessionId + " not found.", HttpStatus.NOT_FOUND));

        String accountId = session.getAccountId();

        AccountEntity entity = accountRepository
                .findById(accountId)
                .orElseThrow(() ->
                        new QuizzioException("User with username " + accountId + " not found.", HttpStatus.NOT_FOUND));

        return mapToResponse(entity);
    }

    private AccountResponse mapToResponse(AccountEntity entity) {
        return new AccountResponse(
                entity.getUserName(),
                entity.getDisplayName(),
                entity.getEmail(),
                setRepository.findAllByUserId(entity.getUserName())
                        .stream()
                        .map(setService::createSetResponse)
                        .collect(Collectors.toList())
        );
    }

}
