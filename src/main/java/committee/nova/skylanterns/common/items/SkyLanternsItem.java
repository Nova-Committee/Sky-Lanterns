package committee.nova.skylanterns.common.items;

import committee.nova.skylanterns.common.entities.SkyLanternEntity;
import committee.nova.skylanterns.init.ModTabs;
import committee.nova.skylanterns.utils.EnumColor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/2/12 7:47
 * Version: 1.0
 */
public class SkyLanternsItem extends Item {


    private final EnumColor color = EnumColor.ORANGE;

    public SkyLanternsItem(EnumColor color) {
        super(new Properties().stacksTo(16).tab(ModTabs.tab));
    }




    @Override
    public ActionResultType useOn(ItemUseContext pContext) {
        final World world = pContext.getLevel();
        final BlockPos pos = pContext.getClickedPos();
        if (!world.isClientSide) {
            final ItemStack stack = pContext.getItemInHand();
            if (!stack.isEmpty()) {
                pContext.getPlayer().swing(pContext.getHand());

                final SkyLanternEntity entity = SkyLanternEntity.create(world, new BlockPos(pos.getX(), pos.getY() + 0.5, pos.getZ()), color);
                if (entity == null) {
                    return ActionResultType.FAIL;
                }
                world.addFreshEntity(entity);
                stack.shrink(1);
            }
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }

    @Nonnull
    @Override
    public ActionResultType interactLivingEntity(@Nonnull ItemStack stack, PlayerEntity player, @Nonnull LivingEntity entity, @Nonnull Hand hand) {
        if (player.isShiftKeyDown()) {
            if (!player.level.isClientSide) {
                final AxisAlignedBB bound = new AxisAlignedBB(entity.getX() - 0.2, entity.getY() - 0.5, entity.getZ() - 0.2,
                        entity.getX() + 0.2, entity.getY() + entity.getDimensions(entity.getPose()).height + 4, entity.getZ() + 0.2);
                final List<SkyLanternEntity> balloonsNear = player.level.getEntitiesOfClass(SkyLanternEntity.class, bound);
                for (SkyLanternEntity balloon : balloonsNear) {
                    if (balloon.latchedEntity == entity) {
                        return ActionResultType.SUCCESS;
                    }
                }
                final SkyLanternEntity balloon = SkyLanternEntity.create(entity, color);
                if (balloon == null) {
                    return ActionResultType.FAIL;
                }
                player.level.addFreshEntity(balloon);
                stack.shrink(1);
            }
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }

}
