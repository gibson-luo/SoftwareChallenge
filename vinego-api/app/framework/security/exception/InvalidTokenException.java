package framework.security.exception;

import com.auth0.jwt.exceptions.JWTVerificationException;

/**
 * Created by gibson.luo on 2018-09-20.
 */
public class InvalidTokenException extends JWTVerificationException {

    public InvalidTokenException(String message) {
        super(message);
    }

}
