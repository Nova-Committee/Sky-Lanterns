package committee.nova.skylanterns.utils;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.vertex.VertexFormat;

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

    public static void draw(int drawMode, VertexFormat format, Consumer<BufferBuilder> fn) {
        draw(drawMode, format, bufferBuilder -> {
            fn.accept(bufferBuilder);
            return null;
        });
    }

    public static <R> R draw(int drawMode, VertexFormat format, Function<BufferBuilder, R> fn) {
        BufferBuilder buf = Tessellator.getInstance().getBuilder();
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
                WorldVertexBufferUploader.end(buf);
            }
        }
    }

    public static void refreshDrawing(IVertexBuilder vb, RenderType type) {
        if (vb instanceof BufferBuilder) {
            type.end((BufferBuilder) vb, 0, 0, 0);
            ((BufferBuilder) vb).begin(type.mode(), type.format());
        }
    }
}
