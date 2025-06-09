package committee.nova.skylanterns.init;

import committee.nova.skylanterns.SkyLanterns;
import committee.nova.skylanterns.common.items.SkyLanternsItem;
import committee.nova.skylanterns.utils.EnumColor;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/1/20 20:04
 * Version: 1.0
 */
public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SkyLanterns.MOD_ID);

    public static final RegistryObject<Item> SKY_LANTERN_ORANGE = ITEMS.register("sky_lantern_orange",
            () -> new SkyLanternsItem(EnumColor.ORANGE));
    public static final RegistryObject<Item> SKY_LANTERN_PINK = ITEMS.register("sky_lantern_pink",
            () -> new SkyLanternsItem(EnumColor.PINK));

    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }
}