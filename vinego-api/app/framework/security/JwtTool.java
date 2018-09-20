package framework.security;

import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;

/**
 * Created by gibson.luo on 2018-09-14.
 */
public final class JwtTool {

    public static JwtEntry create(String issuer) throws JWTCreationException {
        Date now = new Date();
        long issuedAt = now.getTime() / 1000;
        String secret = genSecret(issuer, issuedAt);
        Algorithm algorithm = Algorithm.HMAC256(secret);
        String token = JWT.create()
            .withIssuer(issuer)
            .withIssuedAt(now)
            .sign(algorithm);
        return new JwtEntry(issuer, issuedAt, token);
    }

    public static JwtVerifier verifier(){
        return new JwtVerifier();
    }

    public static String genSecret(String issuer, Long issuedAt) {
        return issuer.concat(issuedAt.toString());
    }

}
