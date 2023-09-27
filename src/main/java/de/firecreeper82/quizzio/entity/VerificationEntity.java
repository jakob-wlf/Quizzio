package de.firecreeper82.quizzio.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class VerificationEntity {
    @Id
    String userId;
    String verificationCode;

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }
}
