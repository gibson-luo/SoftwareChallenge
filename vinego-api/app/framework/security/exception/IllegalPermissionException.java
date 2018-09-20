package framework.security.exception;

import com.auth0.jwt.exceptions.JWTVerificationException;

/**
 * Created by gibson.luo on 2018-09-20.
 */
public class IllegalPermissionException extends JWTVerificationException {

    public IllegalPermissionException(String message) {
        super(message);
    }

}
