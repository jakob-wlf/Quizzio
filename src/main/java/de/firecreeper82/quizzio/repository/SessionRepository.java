package de.firecreeper82.quizzio.repository;

import de.firecreeper82.quizzio.entity.SessionEntity;
import org.springframework.data.repository.CrudRepository;

public interface SessionRepository extends CrudRepository<SessionEntity, String> {
}
