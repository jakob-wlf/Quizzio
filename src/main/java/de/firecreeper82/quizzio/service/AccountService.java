package de.firecreeper82.quizzio.service;

import de.firecreeper82.quizzio.entity.AccountEntity;
import de.firecreeper82.quizzio.exception.QuizzioException;
import de.firecreeper82.quizzio.model.CredentialsResponse;
import de.firecreeper82.quizzio.model.UserResponse;
import de.firecreeper82.quizzio.repository.AccountRepository;
import de.firecreeper82.quizzio.request.AccountCreateRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final RegexService regexService;
    private final SecurityService securityService;

    public AccountService(AccountRepository accountRepository, RegexService regexService, SecurityService securityService) {
        this.accountRepository = accountRepository;
        this.regexService = regexService;
        this.securityService = securityService;
    }

    public void createAccount(AccountCreateRequest request) throws QuizzioException {
        if(accountRepository.findById(request.userName()).isPresent())
            throw new QuizzioException("This userid already exists.", HttpStatus.CONFLICT);

        if(!regexService.isValidEmail(request.email()))
            throw new QuizzioException("Invalid email.", HttpStatus.BAD_REQUEST);

        if(!regexService.isValidPassword(request.password()))
            throw new QuizzioException("Invalid password.", HttpStatus.BAD_REQUEST);

        if(!regexService.isValidUsername(request.userName()))
            throw new QuizzioException("Invalid username.", HttpStatus.BAD_REQUEST);

        if(!regexService.isValidUsername(request.displayName()))
            throw new QuizzioException("Invalid display name.", HttpStatus.BAD_REQUEST);

        CredentialsResponse credentials = securityService.createCredentials(request.password());

        AccountEntity entity = new AccountEntity();
        entity.setUserName(request.userName());
        entity.setPassword(credentials.hashedPassword());
        entity.setDisplayName(request.displayName());
        entity.setEmail(request.email());
        entity.setSalt(credentials.salt());

        accountRepository.save(entity);
    }
}
