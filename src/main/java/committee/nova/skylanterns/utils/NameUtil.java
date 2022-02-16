package committee.nova.skylanterns.utils;

import net.minecraft.util.ResourceLocation;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/2/14 15:18
 * Version: 1.0
 */
public class NameUtil {
    public static ResourceLocation suffixPath(ResourceLocation key, String suffix) {
        return new ResourceLocation(key.getNamespace(), key.getPath() + suffix);
    }

}
