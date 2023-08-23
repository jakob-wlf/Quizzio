package de.firecreeper82.quizzio.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class FlashcardEntity {

    @Id
    private String id;
    private String cardKey;
    private String cardValue;
    private String setId;

    public String getId() {
        return id;
    }

    public String getCardKey() {
        return cardKey;
    }

    public String getCardValue() {
        return cardValue;
    }

    public String getSetId() {
        return setId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCardKey(String key) {
        this.cardKey = key;
    }

    public void setCardValue(String value) {
        this.cardValue = value;
    }

    public void setSetId(String setId) {
        this.setId = setId;
    }
}
