package de.firecreeper82.quizzio.repository;

import de.firecreeper82.quizzio.entity.SetEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SetRepository extends CrudRepository<SetEntity, String> {

    @Query("SELECT e FROM SetEntity e WHERE e.userId = :userId")
    List<SetEntity> findAllByUserId(String userId);

}
