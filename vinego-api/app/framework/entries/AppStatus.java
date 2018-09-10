package framework.entries;

/**
 * This enum defines all the application's status code and msg.
 *
 * Created by gibson.luo on 2018-09-05.
 */
public enum AppStatus {

    OK(200, null),

    INTERNAL_SERVER_ERROR(500, "internal server error"),
    UNDEFINED_CODE(501, "undefined status code");

    static final int _SUCCEED_CODE = 200;
    static final int _UNDEFINED_CODE = 0;

    private int code;

    private String msg;

    AppStatus(int code, String msg) {
        this.code = code;
        this.msg = msg;
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

    public static final AppStatus getByCode(int code) {
        for (AppStatus e : AppStatus.values()) {
            if (e.getCode() == code) {
                return e;
            }
        }
        return null;
    }

    public boolean hasCode() {
        return this.code != _UNDEFINED_CODE;
    }

}
