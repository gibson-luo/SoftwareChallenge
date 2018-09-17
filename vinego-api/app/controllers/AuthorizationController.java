package controllers;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import com.google.inject.Inject;

import com.google.inject.name.Named;
import framework.entries.DwTool;
import framework.entries.JsonTool;
import framework.security.JwtEntry;
import framework.security.JwtTool;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * Created by gibson.luo on 2018-09-15.
 */
public class AuthorizationController extends Controller {

    private final String jwtRk;

    private final RedissonClient redissonClient;

    @Inject
    public AuthorizationController(@Named("redis.key.jwt") String jwtRk,
                                   RedissonClient redissonClient) {
        this.jwtRk = jwtRk;
        this.redissonClient = redissonClient;
    }

    public CompletionStage<Result> register() {
        //TODO store the user info with db
        return null;
    }

    public CompletionStage<Result> login() {
        //TODO store the user info with db
        String issuer = "Gibson";
        JwtEntry entry = JwtTool.create(issuer);
        return redissonClient.getMap(jwtRk)
            .putAsync(issuer, JsonTool.toJsonString(entry))
            .thenApply(item -> ok(JsonTool.toJsonNode(DwTool.ok(entry))));
    }

    public CompletionStage<Result> logout() {
        String issuer = request().getQueryString("issuer");
        RMap<String, String> rMap = redissonClient.getMap(jwtRk);

        return rMap.containsKeyAsync(issuer).thenCompose(hasKey -> {
            if (hasKey) {
                return rMap.removeAsync(issuer);
            }
            return CompletableFuture.completedFuture(issuer);
        }).thenApply(result -> ok(JsonTool.toJsonNode(DwTool.ok(null))));
    }
}
