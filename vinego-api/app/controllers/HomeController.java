package controllers;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.typesafe.config.Config;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import play.libs.ws.WSBodyReadables;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;
import play.mvc.Controller;
import play.mvc.Result;
import tools.JsonTool;
import tools.RedisTool;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller implements WSBodyReadables {

    @Inject
    WSClient ws;

    @Inject
    Config config;

    private final String DEFAULT_PER_PAGE = "10";

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result index() {
        return ok(views.html.index.render());
    }

    /**
     * try to get the product list from LCBO API
     *
     * @return
     */
    public CompletionStage<Result> products() {
        final String ACCESS_KEY = config.getString("LCBOAPI_ACCESS_KEY");
        final String REDIS_URL = config.getString("REDIS_URL");

        String query = request().getQueryString("query");
        String page = request().getQueryString("page");

        RedisClient redisClient = RedisClient.create(REDIS_URL);
        StatefulRedisConnection<String, String> connection = redisClient.connect();
        RedisAsyncCommands<String, String> commands = connection.async();

        WSRequest request = ws.url("http://lcboapi.com/products");
        request.addQueryParameter("access_key", ACCESS_KEY)
            .addQueryParameter("q", Optional.ofNullable(query).orElse(""))
            .addQueryParameter("page", Optional.ofNullable(page).orElse("1"))
            .addQueryParameter("per_page", DEFAULT_PER_PAGE);

        String key = RedisTool.convertUrl2Key(request);

        /**
         * 1. check the cache
         * 2. if it has the key then return the value of cache
         * 3. else request the LCBO api to get the result, and put the result to cache
         */
        return commands.exists(key).thenCompose(exists -> {
            if (exists == 1) {
                return commands.get(key).thenApply(value -> ok(JsonTool.toJsonNode(value)));
            } else {
                return request.get()
                    .thenApply(response -> {
                            JsonNode json = response.asJson();
                            commands.set(key, json.toString());
                            return ok(json);
                        }
                    );
            }
        });

    }

}
