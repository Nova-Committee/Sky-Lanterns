package committee.nova.skylanterns.client.resource;

import net.minecraft.client.renderer.RenderState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;

import java.util.Objects;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/2/14 15:17
 * Version: 1.0
 */
public abstract class AbstractRenderableTexture {
    private final ResourceLocation key;

    protected AbstractRenderableTexture(ResourceLocation key) {
        this.key = key;
    }

    public final ResourceLocation getKey() {
        return key;
    }

    public abstract void bindTexture();

    public abstract RenderState.TextureState asState();

    public abstract Tuple<Float, Float> getUVOffset();

    public abstract float getUWidth();

    public abstract float getVWidth();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractRenderableTexture that = (AbstractRenderableTexture) o;
        return Objects.equals(this.getKey(), that.getKey());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getKey());
    }

    public static abstract class Full extends AbstractRenderableTexture {

        public Full(ResourceLocation key) {
            super(key);
        }

        @Override
        public Tuple<Float, Float> getUVOffset() {
            return new Tuple<>(0F, 0F);
        }

        @Override
        public final float getUWidth() {
            return 1.0F;
        }

        @Override
        public final float getVWidth() {
            return 1.0F;
        }
    }
}
