package framework.security;

/**
 * Created by gibson.luo on 2018-09-14.
 */
public class JwtEntry {

    public JwtEntry(String issuer, long issuedAt, String token) {
        this.issuer = issuer;
        this.issuedAt = issuedAt;
        this.token = token;
    }

    private String issuer;

    private String token;

    private long issuedAt;

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(long issuedAt) {
        this.issuedAt = issuedAt;
    }



}
