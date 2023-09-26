package de.firecreeper82.quizzio.repository;

import de.firecreeper82.quizzio.entity.AccountEntity;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<AccountEntity, String> {
}
