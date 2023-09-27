package de.firecreeper82.quizzio.controller;

import de.firecreeper82.quizzio.data.ResponseStatus;
import de.firecreeper82.quizzio.entity.AccountEntity;
import de.firecreeper82.quizzio.exception.QuizzioException;
import de.firecreeper82.quizzio.model.AccountResponse;
import de.firecreeper82.quizzio.model.SessionResponse;
import de.firecreeper82.quizzio.repository.AccountRepository;
import de.firecreeper82.quizzio.request.AccountCreateRequest;
import de.firecreeper82.quizzio.service.AccountService;
import de.firecreeper82.quizzio.service.ResponseService;
import de.firecreeper82.quizzio.service.SessionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class AccountController {

    private final AccountService accountService;
    private final ResponseService responseService;
    private final SessionService sessionService;
    private final AccountRepository accountRepository;

    public AccountController(AccountService accountService, ResponseService responseService, SessionService sessionService, AccountRepository accountRepository) {
        this.accountService = accountService;
        this.responseService = responseService;
        this.sessionService = sessionService;
        this.accountRepository = accountRepository;
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


        String response = responseService.createJsonResponse(ResponseStatus.SUCCESS, "Account created successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/accounts/login")
    public @ResponseBody SessionResponse login(@RequestParam String userName, @RequestParam String password) throws QuizzioException {
        return sessionService.login(userName, password);
    }

    @GetMapping("/accounts/{username}")
    public @ResponseBody AccountResponse getAccount(@PathVariable String username) throws QuizzioException {
        AccountEntity entity = accountRepository
                .findById(username)
                .orElseThrow(() ->
                        new QuizzioException("User with username " + username + " could not been found.", HttpStatus.NOT_FOUND));

        return mapToResponse(entity);
    }

    private AccountResponse mapToResponse(AccountEntity entity) {
        return new AccountResponse(
                entity.getUserName(),
                entity.getDisplayName(),
                entity.getEmail(),
                List.of()
        );
    }

}
