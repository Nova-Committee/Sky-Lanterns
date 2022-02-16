package committee.nova.skylanterns.common.blocks;

import net.minecraft.block.AirBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/2/12 12:26
 * Version: 1.0
 */
public class LitBlock extends AirBlock {
    public LitBlock() {
        super(Properties.of(Material.AIR).noCollission().air().lightLevel((p_235470_0_) -> {
            return 1;
        }));
        this.setRegistryName("air_lit");
    }

    @Override
    public void animateTick(BlockState pState, World pLevel, BlockPos pPos, Random pRand) {
        super.animateTick(pState, pLevel, pPos, pRand);
    }
}
