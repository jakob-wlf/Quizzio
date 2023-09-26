package de.firecreeper82.quizzio.controller;

import de.firecreeper82.quizzio.exception.QuizzioException;
import de.firecreeper82.quizzio.model.AccountResponse;
import de.firecreeper82.quizzio.request.AccountCreateRequest;
import de.firecreeper82.quizzio.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
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


        return null;
    }
}
