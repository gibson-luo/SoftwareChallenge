package framework.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import framework.security.exception.IllegalPermissionException;
import framework.security.exception.InvalidTokenException;
import io.jsonwebtoken.lang.Assert;
import org.apache.commons.lang3.StringUtils;
import play.mvc.Http.Headers;
import play.mvc.Http.Request;

/**
 * Created by gibson.luo on 2018-09-20.
 */
public class JwtVerifier {

    private JwtEntry jwt;

    private String token;

    public JwtVerifier getToken(Request request) throws InvalidTokenException {
        try {
            Headers headers = request.getHeaders();
            String authorization = headers.get("Authorization").orElse("");
            Assert.isTrue(authorization.startsWith("Bearer "), "it accepted 'Bearer' type token");
            String token = authorization.substring(7);
            Assert.isTrue(StringUtils.isNotEmpty(token), "the token value cannot be empty");
            this.token = token;
            return this;
        } catch (Exception e) {
            throw new InvalidTokenException(e.getMessage());
        }
    }

    public JwtVerifier verify() throws JWTVerificationException {
        Assert.notNull(token, "token must not be null");
        DecodedJWT decodedJWT = JWT.decode(token);
        String issuer = decodedJWT.getIssuer();
        long issuedAt = decodedJWT.getIssuedAt().getTime() / 1000;
        String secret = JwtTool.genSecret(issuer, issuedAt);
        Algorithm algorithm = Algorithm.HMAC256(secret);
        JWTVerifier verifier = JWT.require(algorithm)
            .withIssuer(issuer)
            .acceptIssuedAt(issuedAt)
            .build();
        verifier.verify(token);
        this.jwt = new JwtEntry(issuer, issuedAt, token);
        return this;
    }

    public JwtVerifier checkPermission() throws IllegalPermissionException {
        Assert.notNull(jwt, "jwt must not be null");
        //TODO check the application permissions
        return this;
    }

}
