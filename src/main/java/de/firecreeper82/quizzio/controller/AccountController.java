package de.firecreeper82.quizzio.controller;

import com.mailjet.client.errors.MailjetException;
import de.firecreeper82.quizzio.entity.AccountEntity;
import de.firecreeper82.quizzio.exception.QuizzioException;
import de.firecreeper82.quizzio.model.AccountResponse;
import de.firecreeper82.quizzio.model.CredentialsResponse;
import de.firecreeper82.quizzio.model.SessionResponse;
import de.firecreeper82.quizzio.repository.AccountRepository;
import de.firecreeper82.quizzio.request.AccountCreateRequest;
import de.firecreeper82.quizzio.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class AccountController {

    private final AccountService accountService;
    private final SessionService sessionService;
    private final AccountRepository accountRepository;
    private final VerificationService verificationService;
    private final RegexService regexService;
    private final SecurityService securityService;

    public AccountController(AccountService accountService, SessionService sessionService, AccountRepository accountRepository, VerificationService verificationService, RegexService regexService, SecurityService securityService) {
        this.accountService = accountService;
        this.sessionService = sessionService;
        this.accountRepository = accountRepository;
        this.verificationService = verificationService;
        this.regexService = regexService;
        this.securityService = securityService;
    }

    @PostMapping("/accounts/create")
    public ResponseEntity<String> createAccount(
            @RequestParam String userName,
            @RequestParam String email,
            @RequestParam String displayName,
            @RequestParam String password
    ) throws QuizzioException, MailjetException {

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

    @PostMapping("/sessions/{sessionId}/verify/{verificationCode}")
    public @ResponseBody AccountResponse verifyAccount(@PathVariable String sessionId, @PathVariable String verificationCode) throws QuizzioException {
        return accountService.mapToResponse(verificationService.verifyAccount(sessionId, verificationCode));
    }

    @GetMapping("/accounts/{accountId}")
    public @ResponseBody AccountResponse getAccountInformation(@PathVariable String accountId) throws QuizzioException {
        return accountService.getAccountById(accountId);
    }

    @GetMapping("/sessions/{sessionId}")
    public @ResponseBody AccountResponse getSessionInformation(@PathVariable String sessionId) throws QuizzioException {
        SessionResponse session = sessionService.verifySession(sessionId);

        String accountId = session.accountId();

        return accountService.getAccountById(accountId);
    }


    @PutMapping("/sessions/{sessionId}/changeName")
    public @ResponseBody AccountResponse changeDisplayName(@PathVariable String sessionId, @RequestParam String displayName) throws QuizzioException {
        SessionResponse session = sessionService.verifySession(sessionId);

        AccountEntity entity = accountService.getAccountEntityById(session.accountId());

        regexService.checkForValidDisplayName(displayName);

        entity.setDisplayName(displayName);
        accountRepository.save(entity);

        return accountService.mapToResponse(entity);
    }

    @PutMapping("/sessions/{sessionId}/changePassword")
    public ResponseEntity<String> changePassword(@PathVariable String sessionId, @RequestParam String oldPassword, @RequestParam String newPassword) throws QuizzioException {
        SessionResponse session = sessionService.verifySession(sessionId);
        AccountEntity entity = accountService.getAccountEntityById(session.accountId());

        securityService.verifyCredentials(entity, oldPassword);

        CredentialsResponse credentials = securityService.createCredentials(newPassword);
        entity.setPassword(credentials.hashedPassword());
        entity.setSalt(credentials.salt());

        accountRepository.save(entity);

        return ResponseEntity.status(HttpStatus.OK).body("Successfully changed the password");

    }

}
