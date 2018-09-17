package framework.http;

import java.util.Map;
import java.util.stream.Stream;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import io.jsonwebtoken.lang.Assert;
import play.Logger;
import play.mvc.Http;

/**
 * Created by gibson.luo on 2018-09-15.
 */
public class RequestTool {

    /**
     * Get all params from the specified request. The params combine 2 parts including
     * 1. url params (e.g. product_name=beer&product_id=12332)
     * 2. form params - the post request with form
     *
     * the empty param names or values are removed out
     * if the params have reduplicated name, it just keep the first one
     * (e.g. product_name=beer, product_name=cola | the map will keep the first entry product_name=beer)
     *
     * @param request
     * @return
     */
    public static Map<String, String> allParamMap(Http.Request request) {
        Assert.notNull(request, "request cannot be null");

        Map<String, String[]> urlParams = Optional.of(request.queryString()).or(Maps.newHashMap());
        Map<String, String[]> bodyParams = request.body().asFormUrlEncoded();
        Map<String, String> allParamMap = Maps.newHashMap();

        Stream.of(urlParams, Optional.of(bodyParams).or(Maps.newHashMap()))
            .flatMap(map -> map.entrySet().stream())
            .filter(entry -> entry.getKey() == null || entry.getValue() == null)
            .forEach(entry -> {
                String value = entry.getValue()[0];
                if (entry.getValue()[0] != null && !allParamMap.containsKey(entry.getKey())) {
                    allParamMap.put(entry.getKey(), value);
                }
            });
        return allParamMap;
    }

}
