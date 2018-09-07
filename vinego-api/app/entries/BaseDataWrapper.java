package entries;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * This is a wrapper for REST API response result.
 * It provides code, msg and data
 *
 * -- code:
 * It is the status code which defined by the application.
 * It can tell the API users whether the result is ok or not.
 * The succeed status code is 200.
 *
 * -- msg:
 * It is the status message that tells the API users the detail information about the result.
 * It will appear when the code is not 200, it means something wrong happened.
 *
 * -- data:
 * It is the result object.
 * When the code is 200, it will appear.
 *
 * Created by gibson.luo on 2018-09-05.
 */
@JsonPropertyOrder(alphabetic = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseDataWrapper<T> implements Serializable {

    private static final long serialVersionUID = -7661471414132455170L;

    private int code;

    private String msg;

    private T data;

    public BaseDataWrapper() {

    }

    public BaseDataWrapper(AppStatus status, T data) {
        this.code = status.getCode();
        this.data = data;
    }

    public BaseDataWrapper(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public BaseDataWrapper(AppStatus status) {
        this.code = status.getCode();
        this.msg = status.getMsg();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    /**
     * this check the code whether it has defined code or not
     *
     * @return
     */
    public boolean hasCode() {
        return this.code != AppStatus._UNDEFINED_CODE;
    }

    public boolean withSuccedCode() {
        return this.code == AppStatus._SUCCEED_CODE;
    }

}
