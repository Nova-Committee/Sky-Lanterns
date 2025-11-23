package committee.nova.skylanterns.init;

import committee.nova.skylanterns.SkyLanterns;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/1/24 9:49
 * Version: 1.0
 */
public class ModTabs {

    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, SkyLanterns.MOD_ID);

    public static final Supplier<CreativeModeTab> SKY_LANTERN_TAB =
            TABS.register("sky_lantern_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.tab_sky_lanterns"))
                    .icon(() -> new ItemStack(ModItems.SKY_LANTERN_ORANGE.get()))
                    .displayItems((params, output) -> {
                        output.accept(ModItems.SKY_LANTERN_ORANGE.get());
                        output.accept(ModItems.SKY_LANTERN_PINK.get());
                    })
                    .build());
}
