package framework.entries;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import play.Logger;

/**
 * A json tool base on the Jackson json libs
 *
 * Created by gibson.luo on 2018-09-03.
 */
public class Json {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }

    public static JsonNode toJsonNode(String json) {
        if (json != null) {
            try {
                return objectMapper.readTree(json);
            } catch (IOException e) {
                Logger.error("json parsing error, json: {}", json, e);
            }
        }
        return null;
    }

    public static JsonNode toJsonNode(Object object) {
        if (object != null) {
            try {
                return objectMapper.convertValue(object, JsonNode.class);
            } catch (IllegalArgumentException e) {
                Logger.error("object: {}", object.toString(), e);
            }
        }
        return null;
    }

    public static String toJsonString(Object object) {
        if (object != null) {
            try {
                return objectMapper.writeValueAsString(object);
            } catch (JsonProcessingException e) {
                Logger.error("to json error", e);
            }
        }
        return "";
    }
}
