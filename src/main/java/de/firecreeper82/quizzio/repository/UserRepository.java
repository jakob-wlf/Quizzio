package de.firecreeper82.quizzio.repository;

import de.firecreeper82.quizzio.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, Integer> {

}
