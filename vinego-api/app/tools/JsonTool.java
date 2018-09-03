package tools;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * A json tool base on the Jackson json libs
 *
 * Created by gibson.luo on 2018-09-03.
 */
public class JsonTool {

    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }

    public static JsonNode toJsonNode(String json) {
        if (json != null) {
            try {
                JsonNode node = objectMapper.readTree(json);
                return node;
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        return null;
    }

}
