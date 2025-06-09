package committee.nova.skylanterns.utils;


import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import it.unimi.dsi.fastutil.bytes.ByteConsumer;
import it.unimi.dsi.fastutil.floats.FloatConsumer;
import it.unimi.dsi.fastutil.ints.Int2ObjectFunction;
import it.unimi.dsi.fastutil.shorts.ShortConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/2/14 10:44
 * Version: 1.0
 */
public class TagUtils {
    private TagUtils () {
    }

    public static void setByteIfPresent(CompoundTag tag, String key, ByteConsumer setter) {
        if (tag.contains(key, Tag.TAG_BYTE)) {
            setter.accept(tag.getByte(key));
        }
    }

    public static void setBooleanIfPresent(CompoundTag tag, String key, BooleanConsumer setter) {
        if (tag.contains(key, Tag.TAG_BYTE)) {
            setter.accept(tag.getBoolean(key));
        }
    }

    public static void setShortIfPresent(CompoundTag tag, String key, ShortConsumer setter) {
        if (tag.contains(key, Tag.TAG_SHORT)) {
            setter.accept(tag.getShort(key));
        }
    }

    public static void setIntIfPresent(CompoundTag tag, String key, IntConsumer setter) {
        if (tag.contains(key, Tag.TAG_INT)) {
            setter.accept(tag.getInt(key));
        }
    }

    public static void setLongIfPresent(CompoundTag tag, String key, LongConsumer setter) {
        if (tag.contains(key, Tag.TAG_LONG)) {
            setter.accept(tag.getLong(key));
        }
    }

    public static void setFloatIfPresent(CompoundTag tag, String key, FloatConsumer setter) {
        if (tag.contains(key, Tag.TAG_FLOAT)) {
            setter.accept(tag.getFloat(key));
        }
    }

    public static void setDoubleIfPresent(CompoundTag tag, String key, DoubleConsumer setter) {
        if (tag.contains(key, Tag.TAG_DOUBLE)) {
            setter.accept(tag.getDouble(key));
        }
    }

    public static void setByteArrayIfPresent(CompoundTag tag, String key, Consumer<byte[]> setter) {
        if (tag.contains(key, Tag.TAG_BYTE_ARRAY)) {
            setter.accept(tag.getByteArray(key));
        }
    }

    public static void setStringIfPresent(CompoundTag tag, String key, Consumer<String> setter) {
        if (tag.contains(key, Tag.TAG_STRING)) {
            setter.accept(tag.getString(key));
        }
    }

    public static void setListIfPresent(CompoundTag tag, String key, int type, Consumer<ListTag> setter) {
        if (tag.contains(key, Tag.TAG_LIST)) {
            setter.accept(tag.getList(key, type));
        }
    }

    public static void setCompoundIfPresent(CompoundTag tag, String key, Consumer<CompoundTag> setter) {
        if (tag.contains(key, Tag.TAG_COMPOUND)) {
            setter.accept(tag.getCompound(key));
        }
    }

    public static void setIntArrayIfPresent(CompoundTag tag, String key, Consumer<int[]> setter) {
        if (tag.contains(key, Tag.TAG_INT_ARRAY)) {
            setter.accept(tag.getIntArray(key));
        }
    }

    public static void setLongArrayIfPresent(CompoundTag tag, String key, Consumer<long[]> setter) {
        if (tag.contains(key, Tag.TAG_LONG_ARRAY)) {
            setter.accept(tag.getLongArray(key));
        }
    }

    public static boolean hasOldUUID(CompoundTag tag, String key) {
        return tag.contains(key + "Most", Tag.TAG_ANY_NUMERIC) && tag.contains(key + "Least", Tag.TAG_ANY_NUMERIC);
    }

    public static UUID getOldUUID(CompoundTag tag, String key) {
        return new UUID(tag.getLong(key + "Most"), tag.getLong(key + "Least"));
    }

    public static void setUUIDIfPresent(CompoundTag tag, String key, Consumer<UUID> setter) {
        if (tag.hasUUID(key)) {
            setter.accept(tag.getUUID(key));
        } else if (hasOldUUID(tag, key)) {
            setter.accept(getOldUUID(tag, key));
        }
    }

    public static void setUUIDIfPresentElse(CompoundTag tag, String key, Consumer<UUID> setter, Runnable notPresent) {
        if (tag.hasUUID(key)) {
            setter.accept(tag.getUUID(key));
        } else if (hasOldUUID(tag, key)) {
            setter.accept(getOldUUID(tag, key));
        } else {
            notPresent.run();
        }
    }

    public static void setBlockPosIfPresent(CompoundTag tag, String key, Consumer<BlockPos> setter) {
        if (tag.contains(key, Tag.TAG_COMPOUND)) {
            setter.accept(NbtUtils.readBlockPos(tag.getCompound(key)));
        }
    }


    public static void setFluidStackIfPresent(CompoundTag tag, String key, Consumer<FluidStack> setter) {
        if (tag.contains(key, Tag.TAG_COMPOUND)) {
            setter.accept(FluidStack.loadFluidStackFromNBT(tag.getCompound(key)));
        }
    }


    public static void setItemStackIfPresent(CompoundTag tag, String key, Consumer<ItemStack> setter) {
        if (tag.contains(key, Tag.TAG_COMPOUND)) {
            setter.accept(ItemStack.of(tag.getCompound(key)));
        }
    }

    public static void setResourceLocationIfPresent(CompoundTag tag, String key, Consumer<ResourceLocation> setter) {
        if (tag.contains(key, Tag.TAG_STRING)) {
            ResourceLocation value = ResourceLocation.tryParse(tag.getString(key));
            if (value != null) {
                setter.accept(value);
            }
        }
    }

    public static void setResourceLocationIfPresentElse(CompoundTag tag, String key, Consumer<ResourceLocation> setter, Runnable notPresent) {
        if (tag.contains(key, Tag.TAG_STRING)) {
            ResourceLocation value = ResourceLocation.tryParse(tag.getString(key));
            if (value == null) {
                notPresent.run();
            } else {
                setter.accept(value);
            }
        }
    }

    public static <REG> void setRegistryEntryIfPresentElse(CompoundTag tag, String key, Registry<REG> registry,
                                                           Consumer<REG> setter, Runnable notPresent) {
        setResourceLocationIfPresentElse(tag, key, rl -> {
            REG reg = registry.get(rl);
            if (reg == null) {
                notPresent.run();
            } else {
                setter.accept(reg);
            }
        }, notPresent);
    }

    public static <ENUM extends Enum<ENUM>> void setEnumIfPresent(CompoundTag tag, String key, Int2ObjectFunction<ENUM> indexLookup, Consumer<ENUM> setter) {
        if (tag.contains(key, Tag.TAG_INT)) {
            setter.accept(indexLookup.apply(tag.getInt(key)));
        }
    }

    public static <V> V readRegistryEntry(CompoundTag tag, String key, Registry<V> registry, V fallback) {
        if (tag.contains(key, Tag.TAG_STRING)) {
            ResourceLocation rl = ResourceLocation.tryParse(tag.getString(key));
            if (rl != null) {
                V result = registry.get(rl);
                if (result != null) {
                    return result;
                }
            }
        }
        return fallback;
    }

}
