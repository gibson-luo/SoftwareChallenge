package framework.entries;

/**
 * This is a tool for creating the Data Wrapper
 *
 * Created by gibson.luo on 2018-09-05.
 */
public class DW {

    public static <T, Q> BaseDataWrapper<Q> dw(BaseDataWrapper<T> src) {
        if (src.hasCode()) {
            return new BaseDataWrapper(src.getCode(), src.getMsg());
        } else {
            return undefinedCode();
        }
    }

    public static <T> BaseDataWrapper<T> dw(Status status) {
        if (status.hasCode()) {
            return new BaseDataWrapper(status.getCode(), status.getMsg());
        } else {
            return undefinedCode();
        }
    }

    public static <T> BaseDataWrapper<T> ok(T result) {
        return new BaseDataWrapper(Status.OK, result);
    }

    public static <T> BaseDataWrapper<T> dw(Throwable throwable) {
        //TODO
        return null;
    }

    private static <T> BaseDataWrapper<T> undefinedCode() {
        return new BaseDataWrapper<>(Status.UNDEFINED_CODE);
    }


}
