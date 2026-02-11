package committee.nova.skylanterns.common.blocks;

import net.minecraft.world.level.block.AirBlock;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/2/12 12:26
 * Version: 1.0
 */
public class LitBlock extends AirBlock {
    public LitBlock() {
        super(Properties.of().noCollission().air().lightLevel((state) -> 15));
    }
}
