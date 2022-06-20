package blog.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.text.Normalizer;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringUtils {

    /**
     * Strips non english alphabet characters from string.
     *
     * @param toBeNormalized String to normalize.
     * @return string containing only english alphabet characters.
     */
    public static String normalize(String toBeNormalized) {
        return Normalizer
                .normalize(toBeNormalized, Normalizer.Form.NFKD)
                .replaceAll("[^\\p{ASCII}]", "");
    }

    public static boolean falsy(String str) {
        return str == null || str.isEmpty();
    }
}
