package security;

import model.User;

import java.util.Date;

/**
 * Contains Username, role,s and Expiration date
 */
public class JWT {

    private Credentials credentials;
    private Date expirationDate;

    public JWT() {
    }

    public JWT(Credentials credentials, Date expirationDate) {
        this.credentials = credentials;
        this.expirationDate = expirationDate;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }
}


