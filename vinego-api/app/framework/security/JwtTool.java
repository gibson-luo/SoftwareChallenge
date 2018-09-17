package framework.security;

import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import play.Logger;

/**
 * Created by gibson.luo on 2018-09-14.
 */
public final class JwtTool {

    public static JwtEntry create(String issuer) throws JWTCreationException {
        Date issuedAt = new Date();
        String secret = genSecret(issuer, issuedAt.getTime());
        Algorithm algorithm = Algorithm.HMAC256(secret);
        String token = JWT.create().withIssuer(issuer)
            .withIssuedAt(issuedAt)
            .sign(algorithm);
        return new JwtEntry(issuer, issuedAt.getTime(), token);
    }

    public static boolean verify(String token, String issuer, long issuedAt) {
        String secret = genSecret(issuer, issuedAt);
        Algorithm algorithm = Algorithm.HMAC256(secret);
        try {
            JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(issuer)
                .withClaim("iat", issuedAt)
                .build();
            verifier.verify(token);
            return true;
        } catch (JWTCreationException e) {
            Logger.info("token verify failed, e:{}", e);
            return false;
        }
    }

    private static String genSecret(String issuer, Long issuedAt) {
        return issuer.concat(issuedAt.toString());
    }

}
