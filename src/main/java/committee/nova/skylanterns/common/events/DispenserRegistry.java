package committee.nova.skylanterns.common.events;

import committee.nova.skylanterns.common.entities.SkyLanternEntity;
import committee.nova.skylanterns.init.ModItems;
import committee.nova.skylanterns.utils.EnumColor;
import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DispenserRegistry {
    @SubscribeEvent
    public static void onDispenserRegister(FMLCommonSetupEvent event) {
        DispenserBlock.registerBehavior(ModItems.SkyLanterns_orange, new DefaultDispenseItemBehavior() {
            @Override
            @Nonnull
            public ItemStack execute(@Nonnull IBlockSource source, @Nonnull ItemStack stack) {
                return spawnSkyLantern(source, stack);
            }
        });
        DispenserBlock.registerBehavior(ModItems.SkyLanterns_pink, new DefaultDispenseItemBehavior() {
            @Override
            @Nonnull
            public ItemStack execute(@Nonnull IBlockSource source, @Nonnull ItemStack stack) {
                return spawnSkyLantern(source, stack);
            }
        });
    }

    public static ItemStack spawnSkyLantern(@Nonnull IBlockSource source, @Nonnull ItemStack stack) {
        final BlockPos blockpos = source.getPos().offset(source.getBlockState().getValue(DispenserBlock.FACING).getNormal());
        final World world = source.getLevel();
        final SkyLanternEntity lanternEntity = SkyLanternEntity.create(world, blockpos, EnumColor.ORANGE);
        if (lanternEntity == null) {
            return stack;
        }
        world.addFreshEntity(lanternEntity);
        final int count = stack.getCount() - 1;
        if (count > 0) {
            stack.setCount(count);
            return stack;
        } else {
            return ItemStack.EMPTY;
        }
    }
}
