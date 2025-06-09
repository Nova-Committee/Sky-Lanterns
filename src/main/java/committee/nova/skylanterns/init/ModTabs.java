package committee.nova.skylanterns.init;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/1/24 9:49
 * Version: 1.0
 */
public class ModTabs {

    public static final CreativeModeTab TAB = new CreativeModeTab("tab_sky_lanterns") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.SKY_LANTERN_ORANGE.get());
        }
    };
}