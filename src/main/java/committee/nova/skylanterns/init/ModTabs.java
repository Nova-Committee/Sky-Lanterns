package committee.nova.skylanterns.init;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/1/24 9:49
 * Version: 1.0
 */
public class ModTabs {

    public static ItemGroup tab = new ItemGroup("tabSkyLanterns") {
        @Nonnull
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.SkyLanterns_orange);
        }

    };
}
