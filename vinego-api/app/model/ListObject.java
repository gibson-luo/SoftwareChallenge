package model;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Created by gibson.luo on 2018-09-26.
 */
public class ListObject {

    private JsonNode pager;

    private JsonNode result;

    public JsonNode getPager() {
        return pager;
    }

    public void setPager(JsonNode pager) {
        this.pager = pager;
    }

    public JsonNode getResult() {
        return result;
    }

    public void setResult(JsonNode result) {
        this.result = result;
    }
}
