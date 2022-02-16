package committee.nova.skylanterns.utils;

import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.util.text.Color;
import net.minecraftforge.common.Tags;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/2/13 15:05
 * Version: 1.0
 */
public enum EnumColor implements IIncrementalEnum<EnumColor> {
    BLACK("\u00a70",  "Black", "black", new int[]{64, 64, 64}, DyeColor.BLACK),
    DARK_BLUE("\u00a71",  "Blue", "blue", new int[]{54, 107, 208}, DyeColor.BLUE),
    DARK_GREEN("\u00a72",  "Green", "green", new int[]{89, 193, 95}, DyeColor.GREEN),
    DARK_AQUA("\u00a73",  "Cyan", "cyan", new int[]{0, 243, 208}, DyeColor.CYAN),
    DARK_RED("\u00a74",  "Dark Red", "dark_red", new int[]{201, 7, 31}, MaterialColor.NETHER, Tags.Items.DYES_RED, null),
    PURPLE("\u00a75",  "Purple", "purple", new int[]{164, 96, 217}, DyeColor.PURPLE),
    ORANGE("\u00a76",  "Orange", "orange", new int[]{255, 161, 96}, DyeColor.ORANGE),
    GRAY("\u00a77",  "Light Gray", "light_gray", new int[]{207, 207, 207}, DyeColor.LIGHT_GRAY),
    DARK_GRAY("\u00a78", "Gray", "gray", new int[]{122, 122, 122}, DyeColor.GRAY),
    INDIGO("\u00a79",  "Light Blue", "light_blue", new int[]{85, 158, 255}, DyeColor.LIGHT_BLUE),
    BRIGHT_GREEN("\u00a7a", "Lime", "lime", new int[]{117, 255, 137}, DyeColor.LIME),
    AQUA("\u00a7b",  "Aqua", "aqua", new int[]{48, 255, 249}, MaterialColor.COLOR_LIGHT_BLUE, Tags.Items.DYES_LIGHT_BLUE, null),
    RED("\u00a7c", "Red", "red", new int[]{255, 56, 60}, DyeColor.RED),
    PINK("\u00a7d", "Magenta", "magenta", new int[]{213, 94, 203}, DyeColor.MAGENTA),
    YELLOW("\u00a7e",  "Yellow", "yellow", new int[]{255, 221, 79}, DyeColor.YELLOW),
    WHITE("\u00a7f", "White", "white", new int[]{255, 255, 255}, DyeColor.WHITE),
    //Extras for dye-completeness
    BROWN("\u00a76",  "Brown", "brown", new int[]{161, 118, 73}, DyeColor.BROWN),
    BRIGHT_PINK("\u00a7d", "Pink", "pink", new int[]{255, 188, 196}, DyeColor.PINK);

    private static final EnumColor[] COLORS = values();
    /**
     * The color code that will be displayed
     */
    public final String code;
    private final String englishName;
    private final String registryPrefix;
    @Nullable
    private final DyeColor dyeColor;
    private final MaterialColor mapColor;
    private final ITag<Item> dyeTag;
    private int[] rgbCode;
    private Color color;

    EnumColor(String s,  String englishName, String registryPrefix, int[] rgbCode, DyeColor dyeColor) {
        this(s,  englishName, registryPrefix, rgbCode, dyeColor.getMaterialColor(), dyeColor.getTag(), dyeColor);
    }

    EnumColor(String code,  String englishName, String registryPrefix, int[] rgbCode, MaterialColor mapColor, ITag<Item> dyeTag,
              @Nullable DyeColor dyeColor) {
        this.code = code;
        this.englishName = englishName;
        this.dyeColor = dyeColor;
        this.registryPrefix = registryPrefix;
        setColorFromAtlas(rgbCode);
        this.mapColor = mapColor;
        this.dyeTag = dyeTag;
    }

    /**
     * Gets a color by index.
     *
     * @param index Index of the color.
     */
    public static EnumColor byIndexStatic(int index) {
        return MathUtil.getByIndexMod(COLORS, index);
    }

    /**
     * Gets the prefix to use in registry names for this color.
     */
    public String getRegistryPrefix() {
        return registryPrefix;
    }

    /**
     * Gets the English name of this color.
     */
    public String getEnglishName() {
        return englishName;
    }

    /**
     * Gets the material or map color that most closely corresponds to this color.
     */
    public MaterialColor getMapColor() {
        return mapColor;
    }

    /**
     * Gets the item tag that corresponds to the dye this color corresponds to.
     */
    @Deprecated//TODO - 1.18: Remove this
    public ITag<Item> getDyeTag() {
        return dyeTag;
    }

    @Deprecated//TODO - 1.18: Remove this
    public boolean hasDyeName() {
        return dyeColor != null;
    }

    /**
     * Gets the corresponding dye color or {@code null} if there isn't one.
     */
    @Nullable
    public DyeColor getDyeColor() {
        return dyeColor;
    }

    /**
     * Gets the 0-1 of this color's RGB value by dividing by 255 (used for OpenGL coloring).
     *
     * @param index - R:0, G:1, B:2
     *
     * @return the color value
     */
    public float getColor(int index) {
        return rgbCode[index] / 255F;
    }

    /**
     * Gets the corresponding text color for this color.
     */
    public Color getColor() {
        return color;
    }

    @Override
    public String toString() {
        return code;
    }

    @Nonnull
    @Override
    public EnumColor byIndex(int index) {
        return byIndexStatic(index);
    }

    /**
     * Sets the internal color representation of this color from the color atlas.
     *
     * @param color Color data.
     *
     * @apiNote This method is mostly for <strong>INTERNAL</strong> usage.
     */
    public void setColorFromAtlas(int[] color) {
        rgbCode = color;
        this.color = Color.fromRgb(rgbCode[0] << 16 | rgbCode[1] << 8 | rgbCode[2]);
    }

    /**
     * Gets the red, green and blue color value, as an integer(range: 0 - 255).
     *
     * @return the color values.
     *
     * @apiNote Modifying the returned array will result in this color object changing the color it represents, and should not be done.
     */
    public int[] getRgbCode() {
        return rgbCode;
    }

    /**
     * Gets the red, green and blue color value, as a float(range: 0 - 1).
     *
     * @return the color values.
     */
    public float[] getRgbCodeFloat() {
        return new float[]{getColor(0), getColor(1), getColor(2)};
    }
}
