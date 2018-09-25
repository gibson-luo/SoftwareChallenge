package controllers;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import framework.entries.DW;
import framework.entries.Json;
import framework.entries.Resp;
import framework.entries.Status;
import framework.http.RequestTool;
import framework.security.CryptoTool;
import framework.security.JwtEntry;
import framework.security.JwtTool;
import model.User;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import services.UserMapper;

/**
 * Created by gibson.luo on 2018-09-15.
 */
public class AuthorizationController extends Controller {

    private final String jwtRk;

    private final RedissonClient redissonClient;

    private final UserMapper userService;

    @Inject
    public AuthorizationController(@Named("redis.key.jwt") String jwtRk,
                                   RedissonClient redissonClient,
                                   UserMapper userMapper) {
        this.jwtRk = jwtRk;
        this.redissonClient = redissonClient;
        this.userService = userMapper;
    }

    public CompletionStage<Result> register() {
        Map<String, String> params = RequestTool.allParamMap(request());

        String username = params.get("username");
        if (StringUtils.isBlank(username)) {
            return Resp.future(Status.USERNAME_REQUIRED);
        }
        User user = new User(username);

        return CompletableFuture.supplyAsync(() -> userService.isExistUser(username))
            .thenCompose(isExist -> {
                if (!isExist) {
                    String password = params.get("password");
                    if (StringUtils.isBlank(password)) { return Resp.future(Status.PASSWORD_REQUIRED); }
                    user.setPassword(CryptoTool.sha256(password));

                    return CompletableFuture.supplyAsync(() -> userService.add(user))
                        .thenApply(Resp::ok);
                } else {
                    return CompletableFuture.supplyAsync(Resp::ok);
                }
            }).exceptionally(throwable -> {
                Logger.error("register - error: {}", throwable);
                return Resp.status(Status.INTERNAL_SERVER_ERROR);
            });
    }

    public CompletionStage<Result> login() {
        //TODO store the user info with db
        Map<String, String> params = RequestTool.allParamMap(request());
        String username = params.get("username");
        JwtEntry entry = JwtTool.create(username);
        return redissonClient.getMap(jwtRk)
            .putAsync(username, Json.toJsonString(entry))
            .thenApply(item -> ok(Json.toJsonNode(DW.ok(entry))));
    }

    public CompletionStage<Result> logout() {
        String issuer = request().getQueryString("issuer");
        RMap<String, String> rMap = redissonClient.getMap(jwtRk);

        return rMap.containsKeyAsync(issuer).thenCompose(hasKey -> {
            if (hasKey) {
                return rMap.removeAsync(issuer);
            }
            return CompletableFuture.completedFuture(issuer);
        }).thenApply(Resp::ok);
    }
}
