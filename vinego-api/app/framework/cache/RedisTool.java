package framework.cache;

import java.util.stream.Collectors;

import play.libs.ws.WSRequest;

/**
 * Created by gibson.luo on 2018-09-04.
 */
public class RedisTool {

    /**
     * get the redis key from url
     *
     * @param request
     * @return
     */
    public static String convertUrl2Key(WSRequest request){
        StringBuffer sb = new StringBuffer(request.getUrl());
        sb.append("?");
        request.getQueryParameters().entrySet().stream()
            .filter(entry -> !entry.getKey().equals("access_key"))
            .collect(Collectors.toSet()).stream()
            .forEach(entry -> {
                sb.append(entry.getKey());
                sb.append("=");
                sb.append(entry.getValue().get(0));
                sb.append("&");
            });
        return sb.toString();
    }

}
