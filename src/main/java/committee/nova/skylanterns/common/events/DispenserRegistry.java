package committee.nova.skylanterns.common.events;

import committee.nova.skylanterns.common.entities.SkyLanternEntity;
import committee.nova.skylanterns.init.ModItems;
import committee.nova.skylanterns.utils.EnumColor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DispenserRegistry {
    @SubscribeEvent
    public static void onDispenserRegister(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            DispenserBlock.registerBehavior(ModItems.SKY_LANTERN_ORANGE.get(), new DefaultDispenseItemBehavior() {
                @Override
                @Nonnull
                public ItemStack execute(@Nonnull BlockSource source, @Nonnull ItemStack stack) {
                    return spawnSkyLantern(source, stack);
                }
            });
            DispenserBlock.registerBehavior(ModItems.SKY_LANTERN_PINK.get(), new DefaultDispenseItemBehavior() {
                @Override
                @Nonnull
                public ItemStack execute(@Nonnull BlockSource source, @Nonnull ItemStack stack) {
                    return spawnSkyLantern(source, stack);
                }
            });
        });
    }

    public static ItemStack spawnSkyLantern(@Nonnull BlockSource source, @Nonnull ItemStack stack) {
        Level level = source.getLevel();
        BlockPos blockpos = source.getPos().relative(source.getBlockState().getValue(DispenserBlock.FACING));

        final SkyLanternEntity lanternEntity = SkyLanternEntity.create(level, blockpos, EnumColor.ORANGE);
        if (lanternEntity == null) {
            return stack;
        }
        level.addFreshEntity(lanternEntity);

        final int count = stack.getCount() - 1;
        if (count > 0) {
            stack.setCount(count);
            return stack;
        } else {
            return ItemStack.EMPTY;
        }
    }
}