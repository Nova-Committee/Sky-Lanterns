package committee.nova.skylanterns.utils;

import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.renderer.RenderType;
import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/2/14 15:02
 * Version: 1.0
 */
public class RenderUtil {

    public static void draw(VertexFormat.Mode drawMode, VertexFormat format, Consumer<BufferBuilder> fn) {
        draw(drawMode, format, bufferBuilder -> {
            fn.accept(bufferBuilder);
            return null;
        });
    }

    public static <R> R draw(VertexFormat.Mode drawMode, VertexFormat format, Function<BufferBuilder, R> fn) {
        BufferBuilder buf = Tesselator.getInstance().getBuilder();
        buf.begin(drawMode, format);
        R result = fn.apply(buf);
        finishDrawing(buf);
        return result;
    }

    public static void finishDrawing(BufferBuilder buf) {
        finishDrawing(buf, null);
    }

    public static void finishDrawing(BufferBuilder buf, @Nullable RenderType type) {
        if (buf.building()) {
            if (type != null) {
                type.end(buf, 0, 0, 0);
            } else {
                buf.end();
                BufferUploader.end(buf);
            }
        }
    }

    public static void refreshDrawing(VertexConsumer vb, RenderType type) {
        if (vb instanceof BufferBuilder) {
            type.end((BufferBuilder) vb, 0, 0, 0);
            ((BufferBuilder) vb).begin(type.mode(), type.format());
        }
    }
}
