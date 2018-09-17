package controllers;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import framework.cache.RedisTool;
import framework.entries.JsonTool;
import framework.security.JwtSecurityAction;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import play.libs.ws.WSBodyReadables;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
@With(JwtSecurityAction.class)
public class ApplicationController extends Controller implements WSBodyReadables {

    private final String accessKey;

    private final int defaultPerPage;

    private final String lcboRk;

    private final WSClient ws;

    private final RedissonClient redissonClient;

    @Inject
    public ApplicationController(@Named("lcboapi.accessKey") String accessKey,
                                 @Named("lcboapi.defaultPerPage") int defaultPerPage,
                                 @Named("redis.key.lcboapi") String lcboRk,
                                 WSClient ws,
                                 RedissonClient redissonClient) {
        this.accessKey = accessKey;
        this.defaultPerPage = defaultPerPage;
        this.lcboRk = lcboRk;
        this.ws = ws;
        this.redissonClient = redissonClient;
    }

    /**
     * search the product list from LCBO API
     *
     * @return
     */
    public CompletionStage<Result> products() {
        String query = request().getQueryString("query");
        String page = request().getQueryString("page");

        WSRequest request = ws.url("http://lcboapi.com/products");
        request.addQueryParameter("access_key", accessKey)
            .addQueryParameter("q", Optional.ofNullable(query).orElse(""))
            .addQueryParameter("page", Optional.ofNullable(page).orElse("1"))
            .addQueryParameter("per_page", String.valueOf(defaultPerPage));

        /**
         * 1. check the cache
         * 2. if it has the key then return the value of cache
         * 3. else request the LCBO api to get the result, and put the result to cache
         */
        String key = RedisTool.convertUrl2Key(request);
        RMap<String, String> rMap = redissonClient.getMap(lcboRk);
        return redissonClient.getMap(lcboRk).containsKeyAsync(key).thenCompose(exists -> {
            if (exists) {
                return rMap.getAsync(key).thenApply(value -> ok(JsonTool.toJsonNode(value)));
            } else {
                return request.get()
                    .thenApply(response -> {
                            JsonNode json = response.asJson();
                            rMap.putAsync(key, json.toString());
                            return ok(json);
                        }
                    );
            }
        });
    }

    public CompletionStage<Result> lucky() {
        return null;
    }

}
