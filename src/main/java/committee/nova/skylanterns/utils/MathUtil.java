package committee.nova.skylanterns.utils;

import java.util.List;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/2/13 15:09
 * Version: 1.0
 */
public class MathUtil {

    public static <TYPE> TYPE getByIndexMod(List<TYPE> elements, int index) {
        if (index < 0) {
            return elements.get(Math.floorMod(index, elements.size()));
        }
        return elements.get(index % elements.size());
    }

    public static <TYPE> TYPE getByIndexMod(TYPE[] elements, int index) {
        if (index < 0) {
            return elements[Math.floorMod(index, elements.length)];
        }
        return elements[index % elements.length];
    }
}
