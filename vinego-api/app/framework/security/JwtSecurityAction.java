package framework.security;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import play.Logger;
import play.mvc.Http.Context;
import play.mvc.Result;

/**
 * This action verify the JWT access tokens
 * The verification steps are below:
 * 1. Extract token from the header and check that the JWT is well formed
 * 2. Verify the signature
 * 3. Check the Application permissions
 *
 * Created by gibson.luo on 2018-09-15.
 */
public class JwtSecurityAction extends play.mvc.Action.Simple {

    @Override
    public CompletionStage<Result> call(Context ctx) {
        Logger.info("Calling action: {}", this.getClass().getName());
        try {
            JwtTool.verifier()
                .getToken(ctx.request())
                .verify()
                .checkPermission();

        } catch (Throwable t) {
            Logger.warn("access token verification is failed. exception: {}", t);
            return CompletableFuture.supplyAsync(() -> forbidden());
        }
        return delegate.call(ctx);
    }

}
