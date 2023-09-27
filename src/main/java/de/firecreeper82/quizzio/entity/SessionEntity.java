package de.firecreeper82.quizzio.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class SessionEntity {

    @Id
    private String accountId;
    private String sessionId;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
