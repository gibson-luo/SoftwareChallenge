package framework.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import javax.inject.Inject;

import play.inject.Injector;
import play.mvc.Action;
import play.mvc.Http.Context;
import play.mvc.Http.Request;
import play.mvc.Result;
import play.mvc.Results;
import play.mvc.With;

import static play.mvc.Security.USERNAME;

/**
 * Created by gibson.luo on 2018-09-15.
 */
public class JwtSecurity {

    @With(AuthenticatedAction.class)
    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface JwtAuthenticated {
        Class<? extends Authenticator> value() default Authenticator.class;
    }

    public static class AuthenticatedAction extends Action<JwtAuthenticated> {

        private final Function<JwtAuthenticated, JwtSecurity.Authenticator> configurator;

        @Inject
        public AuthenticatedAction(Injector injector) {
            this(authenticated -> injector.instanceOf(authenticated.value()));
        }

        public AuthenticatedAction(JwtSecurity.Authenticator authenticator) {
            this(authenticated -> authenticator);
        }

        public AuthenticatedAction(Function<JwtAuthenticated, JwtSecurity.Authenticator> configurator) {
            this.configurator = configurator;
        }

        public CompletionStage<Result> call(final Context ctx) {
            JwtSecurity.Authenticator authenticator = configurator.apply(configuration);
            String username = authenticator.getUsername(ctx);
            if (username == null) {
                Result unauthorized = authenticator.onUnauthorized(ctx);
                return CompletableFuture.completedFuture(unauthorized);
            } else {
                Request usernameReq = ctx.request().addAttr(USERNAME, username);
                Context usernameCtx = ctx.withRequest(usernameReq);
                return delegate.call(usernameCtx);
            }
        }

    }

    /**
     * Handles authentication.
     */
    public static class Authenticator extends Results {

        public String getUsername(Context ctx) {
            return ctx.session().get("username");
        }

        public Result onUnauthorized(Context ctx) {
            return unauthorized(views.html.defaultpages.unauthorized.render());
        }

    }
}
