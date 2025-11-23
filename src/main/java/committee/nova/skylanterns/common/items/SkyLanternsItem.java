package committee.nova.skylanterns.common.items;

import committee.nova.skylanterns.common.entities.SkyLanternEntity;
import committee.nova.skylanterns.utils.EnumColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/2/12 7:47
 * Version: 1.0
 */
public class SkyLanternsItem extends Item {


    private final EnumColor color;

    public SkyLanternsItem(EnumColor color) {
        super(new Properties().stacksTo(16));
        this.color = color;
    }


    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        final Level world = pContext.getLevel();
        final BlockPos pos = pContext.getClickedPos();
        final ItemStack stack = pContext.getItemInHand();
        final Player player = pContext.getPlayer();
        if (!world.isClientSide) {
            if (!stack.isEmpty()) {
                player.swing(pContext.getHand());

                final SkyLanternEntity entity = SkyLanternEntity.create(world, new BlockPos(pos.getX(), (int) (pos.getY() + 0.5), pos.getZ()), color);
                if (entity == null) {
                    return InteractionResult.FAIL;
                }
                world.addFreshEntity(entity);
                stack.shrink(1);
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Nonnull
    @Override
    public InteractionResult interactLivingEntity(@Nonnull ItemStack stack, Player player, @Nonnull LivingEntity entity, @Nonnull InteractionHand hand) {
        if (player.isShiftKeyDown()) {
            if (!player.level().isClientSide) {
                final AABB bound = new AABB(entity.getX() - 0.2, entity.getY() - 0.5, entity.getZ() - 0.2,
                        entity.getX() + 0.2, entity.getY() + entity.getDimensions(entity.getPose()).height + 4, entity.getZ() + 0.2);
                final List<SkyLanternEntity> balloonsNear = player.level().getEntitiesOfClass(SkyLanternEntity.class, bound);
                for (SkyLanternEntity balloon : balloonsNear) {
                    if (balloon.latchedEntity == entity) {
                        return InteractionResult.SUCCESS;
                    }
                }
                final SkyLanternEntity balloon = SkyLanternEntity.create(entity, color);
                if (balloon == null) {
                    return InteractionResult.FAIL;
                }
                player.level().addFreshEntity(balloon);
                stack.shrink(1);
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

}
