package framework.http;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import io.jsonwebtoken.lang.Assert;
import play.mvc.Http;
import play.mvc.Http.MultipartFormData;

/**
 * Created by gibson.luo on 2018-09-15.
 */
public class RequestTool {

    /**
     * Get all params from the specified request. The params combine 3 parts including
     * 1. url params (e.g. product_name=beer&product_id=12332)
     * 2. form params - the post request with form
     * 3. multipart body params
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

        Map<String, String[]> urlParams = request.queryString();
        Map<String, String[]> bodyParams = Optional.fromNullable(request.body().asFormUrlEncoded())
            .or(Maps.newHashMap());

        //multipart
        Map<String, String[]> multiFormDataBodyParams = Maps.newHashMap();
        MultipartFormData multipartFormData = request.body().asMultipartFormData();
        if (multipartFormData != null) {
            multiFormDataBodyParams = Optional.fromNullable(multipartFormData.asFormUrlEncoded())
                .or(multiFormDataBodyParams);
        }

        Map<String, String> map = Maps.newHashMap();

        Map<String, String[]> mergeMap = Stream.of(urlParams, bodyParams, multiFormDataBodyParams)
            .map(Map::entrySet)
            .flatMap(Collection::stream)
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        for (Entry<String, String[]> item : mergeMap.entrySet()) {
            String key = item.getKey();
            String[] valueArray = item.getValue();
            if (valueArray.length > 0) {
                String value = valueArray[0];
                map.put(key, value);
            }
        }

        return map;
    }

}
