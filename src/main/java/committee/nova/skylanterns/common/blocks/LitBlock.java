package committee.nova.skylanterns.common.blocks;


import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;

import java.util.Random;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/2/12 12:26
 * Version: 1.0
 */
public class LitBlock extends AirBlock {
    public LitBlock() {
        super(Properties.of(Material.AIR).noCollission().air().lightLevel((p_235470_0_) -> 1));
    }


    @Override
    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRand) {
        super.animateTick(pState, pLevel, pPos, pRand);
    }
}
