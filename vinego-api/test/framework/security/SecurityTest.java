package framework.security;

import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.Assert;
import org.junit.Test;
import play.Logger;

/**
 * Created by gibson.luo on 2018-09-14.
 */
public class SecurityTest {

    @Test
    public void testJavaJwt() {

        try {
            Algorithm algorithm = Algorithm.HMAC256("secret");
            String token = JWT.create()
                .withClaim("name", "Gibson")
                .sign(algorithm);

            Logger.info("token: {}", token);
        } catch (JWTCreationException exception) {
            //Invalid Signing configuration / Couldn't convert Claims.
        }

    }

    @Test
    public void testJWTBuilder(){
        String salt = "jwt";
        String iss = "auth0";
        Long iat = 1536945213308L;
        String secret = iss.concat(iat.toString()).concat(salt);
        Algorithm algorithm = Algorithm.HMAC256(secret);
        try {
            String token = JWT.create()
                .withIssuer(iss)
                .withIssuedAt(new Date(iat))
                .sign(algorithm);

            Logger.info("token: {}", token);
        }catch (JWTCreationException exception){

        }

    }

    @Test
    public void testJWTVerifierNotEquals(){
        String salt = "jwt";
        String iss = "auth0";
        Long iat = 1536945213309L;
        String secret = iss.concat(iat.toString()).concat(salt);


        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJhdXRoMCIsImlhdCI6MTUzNjk0NTIxM30.H15CzwOI7OF4aJdQFt4cHhGVrrtKZCKtv4ux29so0tM";
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer("auth0")
                .withClaim("iat", 1536945213308L)
                .build(); //Reusable verifier instance
            DecodedJWT jwt = verifier.verify(token);

            Logger.info("payload: {}", jwt);
            Assert.assertTrue(false);
        } catch (JWTVerificationException exception){
            //Invalid signature/claims

            Assert.assertTrue(true);
        }
    }

}
