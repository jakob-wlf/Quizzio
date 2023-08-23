package de.firecreeper82.quizzio.repository;

import de.firecreeper82.quizzio.entity.FlashcardEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FlashcardRepository extends CrudRepository<FlashcardEntity, String> {

    @Query("SELECT e FROM FlashcardEntity e WHERE e.setId = :setId")
    public List<FlashcardEntity> findAllBySetId(String setId);
}
