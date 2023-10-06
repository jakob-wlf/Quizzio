package de.firecreeper82.quizzio.service;

import com.mailjet.client.errors.MailjetException;
import de.firecreeper82.quizzio.data.AccountStatus;
import de.firecreeper82.quizzio.entity.AccountEntity;
import de.firecreeper82.quizzio.exception.QuizzioException;
import de.firecreeper82.quizzio.model.AccountResponse;
import de.firecreeper82.quizzio.model.CredentialsResponse;
import de.firecreeper82.quizzio.repository.AccountRepository;
import de.firecreeper82.quizzio.repository.SetRepository;
import de.firecreeper82.quizzio.request.AccountCreateRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final RegexService regexService;
    private final SecurityService securityService;
    private final VerificationService verificationService;
    private final SetRepository setRepository;
    private final SetService setService;

    public AccountService(AccountRepository accountRepository, RegexService regexService, SecurityService securityService, VerificationService verificationService, SetRepository setRepository, SetService setService) {
        this.accountRepository = accountRepository;
        this.regexService = regexService;
        this.securityService = securityService;
        this.verificationService = verificationService;
        this.setRepository = setRepository;
        this.setService = setService;
    }

    public void createAccount(AccountCreateRequest request) throws QuizzioException, MailjetException {
        if(accountRepository.findById(request.userName()).isPresent())
            throw new QuizzioException("This userid already exists.", HttpStatus.CONFLICT);

        regexService.checkForValidEmail(request.email());

        regexService.checkForValidPassword(request.password());

        regexService.checkForValidUsername(request.userName());

        regexService.checkForValidDisplayName(request.displayName());


        CredentialsResponse credentials = securityService.createCredentials(request.password());

        AccountEntity entity = new AccountEntity();
        entity.setUserName(request.userName());
        entity.setPassword(credentials.hashedPassword());
        entity.setDisplayName(request.displayName());
        entity.setEmail(request.email());
        entity.setStatus(AccountStatus.UNVERIFIED);
        entity.setSalt(credentials.salt());

        accountRepository.save(entity);

        verificationService.createVerification(
                entity.getUserName(),
                entity.getEmail()
        );
    }

    public AccountResponse getAccountById(String accountId) throws QuizzioException {
        return mapToResponse(
                accountRepository
                .findById(accountId)
                .orElseThrow(() ->
                        new QuizzioException("User with username " + accountId + " not found.", HttpStatus.NOT_FOUND))
        );
    }

    public AccountEntity getAccountEntityById(String accountId) throws QuizzioException {
        return accountRepository
                .findById(accountId)
                .orElseThrow(() ->
                        new QuizzioException("User with username " + accountId + " not found.", HttpStatus.NOT_FOUND));
    }

    public AccountResponse mapToResponse(AccountEntity entity) {
        return new AccountResponse(
                entity.getUserName(),
                entity.getDisplayName(),
                entity.getEmail(),
                entity.getStatus(),
                setRepository.findAllByUserId(entity.getUserName())
                        .stream()
                        .map(setService::createSetResponse)
                        .collect(Collectors.toList())
        );
    }
}
