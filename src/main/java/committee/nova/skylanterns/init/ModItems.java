package committee.nova.skylanterns.init;

import committee.nova.skylanterns.SkyLanterns;
import committee.nova.skylanterns.common.items.SkyLanternsItem;
import committee.nova.skylanterns.utils.EnumColor;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/1/20 20:04
 * Version: 1.0
 */
@Mod.EventBusSubscriber(modid = SkyLanterns.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModItems {


    public static Item SkyLanterns_orange;
    public static Item SkyLanterns_pink;

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        final IForgeRegistry<Item> registry = event.getRegistry();

        registry.registerAll(
                SkyLanterns_orange = new SkyLanternsItem(EnumColor.ORANGE).setRegistryName("sky_lantern_orange"),
                SkyLanterns_pink = new SkyLanternsItem(EnumColor.ORANGE).setRegistryName("sky_lantern_pink")
        );

    }



}
