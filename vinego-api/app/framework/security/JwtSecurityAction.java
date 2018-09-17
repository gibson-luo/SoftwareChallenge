package framework.security;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import org.apache.commons.lang3.StringUtils;
import play.Logger;
import play.mvc.Http.Context;
import play.mvc.Http.Request;
import play.mvc.Result;

/**
 *
 * Created by gibson.luo on 2018-09-15.
 */
public class JwtSecurityAction extends play.mvc.Action.Simple {

    @Override
    public CompletionStage<Result> call(Context ctx) {
        Logger.info("Calling action: {}", this.getClass().getName());
        Request request = ctx.request();
        String issuer = request.getQueryString("issuer");
        if (StringUtils.isBlank(issuer)) { return CompletableFuture.supplyAsync(() -> forbidden()); }

        long issuedAt = Long.valueOf(Optional.of(request.getQueryString("issuedAt")).orElse("0"));
        String token = "";
        if(!JwtTool.verify(token, issuer, issuedAt)){
            return CompletableFuture.supplyAsync(() -> forbidden());
        }
        return delegate.call(ctx);
    }
}
