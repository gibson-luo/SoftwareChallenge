package controllers;

import java.util.concurrent.CompletionStage;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import play.libs.ws.WSBodyReadables;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;
import play.mvc.Controller;
import play.mvc.Result;
import tools.JsonTool;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller implements WSBodyReadables {

    @Inject
    WSClient ws;

    private static String ACCESS_KEY
        = "MDplOTU2YjM3NC1hYzYwLTExZTgtYTQ0ZS0zYjZlMjk2ZTI4YzY6NEl2WTVsem00R0E1SDNwSnBjTUhjaW03anFRZjJwSGFYTTBh";

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

        RedisClient redisClient = RedisClient.create("redis://localhost:6379/0");
        StatefulRedisConnection<String, String> connection = redisClient.connect();
        RedisAsyncCommands<String, String> commands = connection.async();

        WSRequest request = ws.url("http://lcboapi.com/products");
        request.addQueryParameter("access_key", ACCESS_KEY)
            .addQueryParameter("page", "1")
            .addQueryParameter("per_page", "10");

        StringBuffer sb = new StringBuffer(request.getUrl());
        sb.append("?");

        request.getQueryParameters().entrySet().stream().forEach(entry -> {
            sb.append(entry.getKey());
            sb.append("=");
            sb.append(entry.getValue().get(0));
            sb.append("&");
        });

        final String key = sb.toString();

        RedisFuture<Long> existsFuture = commands.exists(key);
        return existsFuture.thenCompose(exists -> {
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
