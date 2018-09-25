package framework.entries;

/**
 * This enum defines all the application's status code and msg.
 *
 * Created by gibson.luo on 2018-09-05.
 */
public enum Status {

    OK(200, null),

    INTERNAL_SERVER_ERROR(500, "internal server error"),
    UNDEFINED_CODE(501, "undefined status code"),

    USERNAME_REQUIRED(1001,"username required"),
    PASSWORD_REQUIRED(1002,"password required"),
    USERNAME_EXIST(1003, "username exist"),
    CANNOT_LOGIN(1004, "username or password is incorrect"),

        ;

    static final int _SUCCEED_CODE = 200;
    static final int _UNDEFINED_CODE = 0;

    private int code;

    private String msg;

    Status(int code, String msg) {
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

    public static final Status getByCode(int code) {
        for (Status e : Status.values()) {
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
