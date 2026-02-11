package committee.nova.skylanterns.utils;


import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/2/14 10:53
 * Version: 1.0
 */
public class LevelUtils {


    //@Contract("null, _ -> false")
    public static boolean isBlockLoaded(@Nullable BlockGetter level, @Nonnull BlockPos pos) {
        if (level == null || !Level.isInSpawnableBounds(pos)) {
            return false;
        } else if (level instanceof LevelReader) {
            //Note: We don't bother checking if it is a world and then isBlockPresent because
            // all that does is also validate the y value is in bounds, and we already check to make
            // sure the position is valid both in the y and xz directions
            return ((LevelReader) level).hasChunkAt(pos);
        }
        return true;
    }

    /**
     * Gets a blockstate if the location is loaded
     *
     * @param level level
     * @param pos   position
     * @return optional containing the blockstate if found, empty optional if not loaded
     */
    @Nonnull
    public static Optional<BlockState> getBlockState(@Nullable BlockGetter level, @Nonnull BlockPos pos) {
        if (!isBlockLoaded(level, pos)) {
            //If the world is null, or it is a world reader and the block is not loaded, return empty
            return Optional.empty();
        }
        return Optional.of(level.getBlockState(pos));
    }
}
