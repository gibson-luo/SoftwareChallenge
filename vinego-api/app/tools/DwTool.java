package tools;

import entries.AppStatus;
import entries.BaseDataWrapper;

/**
 * This is a tool for creating the Data Wrapper
 *
 * Created by gibson.luo on 2018-09-05.
 */
public class DwTool {

    public static <T, Q> BaseDataWrapper<Q> dw(BaseDataWrapper<T> src) {
        if (src.hasCode()) {
            return new BaseDataWrapper(src.getCode(), src.getMsg());
        } else {
            return undefinedCode();
        }
    }

    public static <T> BaseDataWrapper<T> dw(AppStatus status) {
        if (status.hasCode()) {
            return new BaseDataWrapper(status.getCode(), status.getMsg());
        } else {
            return undefinedCode();
        }
    }

    public static <T> BaseDataWrapper<T> ok(T result) {
        return new BaseDataWrapper(AppStatus.OK, result);
    }

    public static <T> BaseDataWrapper<T> dw(Throwable throwable) {
        //TODO
        return null;
    }

    private static <T> BaseDataWrapper<T> undefinedCode() {
        return new BaseDataWrapper<>(AppStatus.UNDEFINED_CODE);
    }

}
