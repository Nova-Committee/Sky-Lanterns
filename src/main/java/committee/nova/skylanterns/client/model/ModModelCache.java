package committee.nova.skylanterns.client.model;

import committee.nova.skylanterns.Skylanterns;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/2/13 14:56
 * Version: 1.0
 */
public class ModModelCache extends BaseModelCache{
    public static final ModModelCache instance = new ModModelCache();

    public final JSONModelData Lantern = registerJSON(Skylanterns.rl("item/lantern"));
    public final JSONModelData Light = registerJSON(Skylanterns.rl("item/light"));

}
