package de.firecreeper82.quizzio.entity;

import de.firecreeper82.quizzio.data.AccountStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class AccountEntity {
    @Id
    String userName;
    String displayName;
    String password;
    String email;
    AccountStatus status;
    byte[ ] salt;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public byte[] getSalt() {
        return salt;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }
}
