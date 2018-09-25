package framework.security;

import java.nio.charset.StandardCharsets;

import com.google.common.hash.Hashing;

/**
 * Created by gibson.luo on 2018-09-21.
 */
public class CryptoTool {

    /**
     * crypto by sha256
     *
     * @param originalString
     * @return
     */
    public static String sha256(String originalString) {
        return Hashing.sha256()
            .hashString(originalString, StandardCharsets.UTF_8)
            .toString();
    }

}
