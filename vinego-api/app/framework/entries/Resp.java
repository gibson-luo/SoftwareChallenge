package framework.entries;

import java.util.concurrent.CompletableFuture;

import play.mvc.Result;
import play.mvc.Results;

/**
 * Created by gibson.luo on 2018-09-25.
 */
public class Resp {

    public static <T> Result ok(T data) {
        return Results.ok(Json.toJsonNode(DW.ok(data)));
    }

    public static Result ok() {
        return ok(null);
    }

    public static Result status(Status status) {
        return Results.ok(Json.toJsonNode(DW.dw(status)));
    }

    public static CompletableFuture<Result> future(Status status) {
        return CompletableFuture.completedFuture(Resp.status(status));
    }
}
