package committee.nova.skylanterns.init;

import committee.nova.skylanterns.SkyLanterns;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/2/16 13:05
 * Version: 1.0
 */
public class ModRenderTypes {

    protected static final RenderStateShard.ShaderStateShard ENTITY_SHADER = new RenderStateShard.ShaderStateShard(
            GameRenderer::getRendertypeEntityCutoutShader
    );

    protected static final RenderStateShard.LightmapStateShard LIGHTMAP = new RenderStateShard.LightmapStateShard(true);

    protected static final RenderStateShard.TextureStateShard LIGHT_TEX = new RenderStateShard.TextureStateShard(
            new ResourceLocation(SkyLanterns.MOD_ID, "textures/entities/radiant_light.png"), false, false);

    public static final RenderType SOLID = RenderType.create(
            "radiant_light",
            DefaultVertexFormat.NEW_ENTITY,
            VertexFormat.Mode.QUADS,
            256,
            false,
            true,
            RenderType.CompositeState.builder()
                    .setShaderState(ENTITY_SHADER)
                    .setLightmapState(LIGHTMAP)
                    .setTextureState(LIGHT_TEX)
                    .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                    .setCullState(RenderStateShard.NO_CULL)
                    .createCompositeState(false)
    );

    public static RenderType glowing(ResourceLocation texture) {
        return RenderType.create(
                "sky_lantern_glow",
                DefaultVertexFormat.NEW_ENTITY,
                VertexFormat.Mode.QUADS,
                256,
                false,
                true,
                RenderType.CompositeState.builder()
                        .setShaderState(new RenderStateShard.ShaderStateShard(GameRenderer::getPositionColorTexLightmapShader))
                        .setLightmapState(new RenderStateShard.LightmapStateShard(false))
                        .setTextureState(new RenderStateShard.TextureStateShard(texture, false, false))
                        .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                        .setCullState(RenderStateShard.NO_CULL)
                        .createCompositeState(false)
        );
    }
}