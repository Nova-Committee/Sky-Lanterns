package committee.nova.skylanterns.common.configs;

import committee.nova.skylanterns.SkyLanterns;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/2/12 12:22
 * Version: 1.0
 */
@Mod.EventBusSubscriber(modid = SkyLanterns.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModConfig {
    public static final Common COMMON;
    public static final ForgeConfigSpec CONFIG_SPEC;

    static {
        final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON = specPair.getLeft();
        CONFIG_SPEC = specPair.getRight();
    }

    public static class Common {
        public final ForgeConfigSpec.ConfigValue<Integer> lightUpdateRate, lightUpdateDistanceAccuracy, lightUpdateDistanceToGround;


        public Common(ForgeConfigSpec.Builder builder) {

            lightUpdateRate = builder.comment("检查天灯是否应该放置一个新的不可见光源的时间间隔，性能敏感，设置为-1以使其永远不会放置不可见光块").defineInRange("lightUpdateRate", 10, 0, Integer.MAX_VALUE);
            lightUpdateDistanceAccuracy = builder.comment("天灯在放置新光源之前需要与之前放置的光源相距多远，性能敏感").defineInRange("lightUpdateDistanceAccuracy", 4, 0, Integer.MAX_VALUE);
            lightUpdateDistanceToGround = builder.comment("天灯需要离地面多近才能放置光源，这样可以防止将光源放置在天空中的高处，这会降低性能，对性能非常敏感").defineInRange("lightUpdateDistanceToGround", 6, 0, Integer.MAX_VALUE);


        }
    }
}
