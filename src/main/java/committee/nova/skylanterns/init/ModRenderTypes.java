package committee.nova.skylanterns.init;

import committee.nova.skylanterns.Skylanterns;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;



/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/2/16 13:05
 * Version: 1.0
 */
public class ModRenderTypes {

    protected static final RenderState.ShadeModelState SMOOTH_SHADE = new RenderState.ShadeModelState(true);
    protected static final RenderState.LightmapState LIGHTMAP = new RenderState.LightmapState(true);
    protected static final RenderState.TextureState Light_Tex = new RenderState.TextureState(new ResourceLocation(Skylanterns.MOD_ID + ":textures/entities/radiant_light.png"), false, false);

    public static final RenderType SOLID = RenderType.create("radiant_light", DefaultVertexFormats.POSITION_TEX_COLOR, 7, 256, false, true, RenderType.State.builder().setShadeModelState(SMOOTH_SHADE).setLightmapState(LIGHTMAP).setTextureState(Light_Tex).createCompositeState(false));

}
