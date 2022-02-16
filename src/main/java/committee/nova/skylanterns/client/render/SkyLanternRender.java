package committee.nova.skylanterns.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import committee.nova.skylanterns.Skylanterns;
import committee.nova.skylanterns.client.model.BaseModelCache;
import committee.nova.skylanterns.client.model.ModModelCache;
import committee.nova.skylanterns.client.model.PaperLanternPinkModel;
import committee.nova.skylanterns.common.entities.SkyLanternEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.item.DyeColor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.LightType;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.List;

import static net.minecraft.client.renderer.entity.MobRenderer.renderSide;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/2/12 9:04
 * Version: 1.0
 */
public class SkyLanternRender extends EntityRenderer<SkyLanternEntity> {

    protected final  PaperLanternPinkModel model = new PaperLanternPinkModel();
    private final Minecraft mc = Minecraft.getInstance();
    //public final TextureAtlasSprite LIGHT = mc.getTextureAtlas(AtlasTexture.LOCATION_BLOCKS).apply(new ResourceLocation(Skylanterns.MOD_ID + ":entities/radiant_light.png"));
    public HashMap<Integer, ResourceLocation> TEXTURES = new HashMap<>();

    public SkyLanternRender(EntityRendererManager p_i50965_1_) {
        super(p_i50965_1_);

        TEXTURES.put(DyeColor.ORANGE.getId(), new ResourceLocation(Skylanterns.MOD_ID + ":textures/entities/sky_lantern_orange.png"));
        TEXTURES.put(DyeColor.PINK.getId(), new ResourceLocation(Skylanterns.MOD_ID + ":textures/entities/sky_lantern_pink.png"));
    }

    @Override
    public ResourceLocation getTextureLocation(SkyLanternEntity entity) {
        return new ResourceLocation(Skylanterns.MOD_ID + ":textures/entities/sky_lantern_"+ entity.getColor().getRegistryPrefix() +".png");
    }

    @Override
    public void render(SkyLanternEntity pEntity, float pEntityYaw, float pPartialTicks, MatrixStack pMatrixStack, IRenderTypeBuffer pBuffer, int pPackedLight) {


        pMatrixStack.pushPose();
        //
        pMatrixStack.translate(0, 0.25, 0);
        float scale = 0.25F;
        pMatrixStack.scale(scale, scale, scale);
        pMatrixStack.mulPose(Vector3f.ZP.rotationDegrees(180));
        //
        long time = pEntity.level.getGameTime();

        long timeBase = time + (pEntity.getId() * 10L);
        float rate = 5;

        float tiltMax = (float)Math.sin(Math.toRadians(((timeBase) * 1F) % 360)) * 5F;

        float tiltCurX = (float)Math.sin(Math.toRadians(((timeBase) * rate) % 360)) * tiltMax;
        float tiltCurY = (float)Math.sin(Math.toRadians(((timeBase + 45) * rate) % 360)) * tiltMax;
        float tiltCurZ = (float)Math.sin(Math.toRadians(((timeBase + 90) * rate) % 360)) * tiltMax;

        float rotateY = (((float)timeBase * 0.1F) % 360);

        //tiltCur = (float)Math.sin(Math.toRadians(90)) * tiltMax;
        pMatrixStack.mulPose(Vector3f.XP.rotationDegrees(tiltCurX));
        pMatrixStack.mulPose(Vector3f.YP.rotationDegrees(tiltCurY + rotateY));
        pMatrixStack.mulPose(Vector3f.ZP.rotationDegrees(tiltCurZ));

        this.setupRotations(pEntity, pMatrixStack, pEntityYaw, pPartialTicks);
        this.model.setupAnim(pEntity, pPartialTicks, 0.0F, -0.1F, pEntityYaw, 0.0F);
        //
        IVertexBuilder ivertexbuilder = pBuffer.getBuffer(this.model.renderType(this.getTextureLocation(pEntity)));
        this.model.renderToBuffer(pMatrixStack, ivertexbuilder, 15728880, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        pMatrixStack.popPose();
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
        //renderTexture( 32, 32, pEntityYaw, LIGHT);
        //renderLight(pEntity, pMatrixStack, pPackedLight, pBuffer);
        Entity entity = pEntity.getLeashHolder();
        if (entity != null) {
            this.renderLeash(pEntity, pPartialTicks, pMatrixStack, pBuffer, entity);
        }

    }

    private void renderLight(SkyLanternEntity pEntityLiving,  MatrixStack pMatrixStack, int pPackedLight, IRenderTypeBuffer pBuffer) {
        pMatrixStack.pushPose();
        pMatrixStack.translate(0, - 0.25, 0);
        float scale = 0.022F;
        pMatrixStack.scale(scale, scale, scale);
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
        pMatrixStack.mulPose(Vector3f.YP.rotationDegrees(-45));
        BaseModelCache.JSONModelData model = ModModelCache.instance.Light;
        List<BakedQuad> quads = model.getBakedModel().getQuads(null, null, pEntityLiving.level.random);
        RenderType renderType = RenderType.entityTranslucent(AtlasTexture.LOCATION_BLOCKS);
        IVertexBuilder builder = pBuffer.getBuffer(renderType);
        MatrixStack.Entry last = pMatrixStack.last();
        for (BakedQuad quad : quads) {
            builder.addVertexData(last, quad, 243, 243, 117, 1, pPackedLight, OverlayTexture.NO_OVERLAY);
        }
        ((IRenderTypeBuffer.Impl) pBuffer).endBatch(renderType);
        pMatrixStack.popPose();
    }
    private <E extends Entity> void renderLeash(SkyLanternEntity pEntityLiving, float pPartialTicks, MatrixStack pMatrixStack, IRenderTypeBuffer pBuffer, E pLeashHolder) {
        pMatrixStack.pushPose();
        Vector3d vector3d = pLeashHolder.getRopeHoldPosition(pPartialTicks);
        double d0 = (double)(MathHelper.lerp(pPartialTicks, pEntityLiving.yBodyRot, pEntityLiving.yBodyRotO) * ((float)Math.PI / 180F)) + (Math.PI / 2D);
        Vector3d vector3d1 = pEntityLiving.getLeashOffset();
        double d1 = Math.cos(d0) * vector3d1.z + Math.sin(d0) * vector3d1.x;
        double d2 = Math.sin(d0) * vector3d1.z - Math.cos(d0) * vector3d1.x;
        double d3 = MathHelper.lerp((double)pPartialTicks, pEntityLiving.xo, pEntityLiving.getX()) + d1;
        double d4 = MathHelper.lerp((double)pPartialTicks, pEntityLiving.yo, pEntityLiving.getY()) + vector3d1.y;
        double d5 = MathHelper.lerp((double)pPartialTicks, pEntityLiving.zo, pEntityLiving.getZ()) + d2;
        pMatrixStack.translate(d1, vector3d1.y - pEntityLiving.getEyeHeight(), d2);
        float f = (float)(vector3d.x - d3);
        float f1 = (float)(vector3d.y - d4);
        float f2 = (float)(vector3d.z - d5);
        float f3 = 0.025F;
        IVertexBuilder ivertexbuilder = pBuffer.getBuffer(RenderType.leash());
        Matrix4f matrix4f = pMatrixStack.last().pose();
        float f4 = MathHelper.fastInvSqrt(f * f + f2 * f2) * 0.025F / 2.0F;
        float f5 = f2 * f4;
        float f6 = f * f4;
        BlockPos blockpos = new BlockPos(pEntityLiving.getEyePosition(pPartialTicks));
        BlockPos blockpos1 = new BlockPos(pLeashHolder.getEyePosition(pPartialTicks));
        int i = this.getBlockLightLevel(pEntityLiving, blockpos);
        //int j = this.entityRenderDispatcher.getRenderer(pLeashHolder).getBlockLightLevel(pLeashHolder, blockpos1);
        int k = pEntityLiving.level.getBrightness(LightType.SKY, blockpos);
        int l = pEntityLiving.level.getBrightness(LightType.SKY, blockpos1);
        renderSide(ivertexbuilder, matrix4f, f, f1, f2, i, 0, k, l, 0.025F, 0.025F, f5, f6);
        renderSide(ivertexbuilder, matrix4f, f, f1, f2, i, 0, k, l, 0.025F, 0.0F, f5, f6);
        pMatrixStack.popPose();
    }



    protected void setupRotations(SkyLanternEntity entity ,MatrixStack pMatrixStack,float pRotationYaw, float pPartialTicks) {
        pMatrixStack.mulPose(Vector3f.YP.rotationDegrees(180.0F - pRotationYaw));

        if (entity.deathTime > 0) {
            float f = ((float) entity.deathTime + pPartialTicks - 1.0F) / 20.0F * 1.6F;
            f = MathHelper.sqrt(f);

            if (f > 1.0F) {
                f = 1.0F;
            }
            pMatrixStack.mulPose(Vector3f.YP.rotationDegrees(f * 700F));
        }

    }

    public boolean renderTexture(int width, int height, float angle, TextureAtlasSprite parIcon) {
        float f6 = parIcon.getU0();
        float f7 = parIcon.getU1();
        float f9 = parIcon.getV0();
        float f8 = parIcon.getV1();

        float scale = 0.022F;
        GlStateManager._pushMatrix();
        GlStateManager._translated(-0.5, -1, -0.5);
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
        GlStateManager._rotatef(-angle, 0.0F, 1.0F, 0.0F);
        GlStateManager._scalef(-scale, -scale, -scale);

        int borderSize = 0;

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder worldrenderer = tessellator.getBuilder();

        //GlStateManager.disableFog();

        GlStateManager._enableBlend();
        GlStateManager._blendFunc(GlStateManager.SourceFactor.SRC_ALPHA.value, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA.value);
        GlStateManager._alphaFunc(516, 0.003921569F);

        GlStateManager._disableLighting();

        //Minecraft.getMinecraft().entityRenderer.enableLightmap();

        /*int i = 15728880;
        //i = (new Random()).nextInt(99999999);
        int j = i >> 16 & 65535;
        int k = i & 65535;*/

        /*int i = 15728880;//entitylivingbaseIn.getBrightnessForRender(1);
        int j = i % 65536;
        int k = i / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j, (float)k);*/

        //this.getDispatcher().textureManager.bind(AtlasTexture.LOCATION_BLOCKS);

        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        //worldrenderer.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);

        float r = 1F;
        float g = 1F;
        float b = 1F;

        worldrenderer
                .vertex((double)(-width / 2 - borderSize), (double)(-borderSize), 0.0D)
                .uv(f6, f9)
                .color(r, g, b, 1.0F)
                //.lightmap(j, k)
                .endVertex();

        worldrenderer
                .vertex((double)(-width / 2 - borderSize), (double)(height), 0.0D)
                .uv(f6, f8)
                .color(r, g, b, 1.0F)
                //.lightmap(j, k)
                .endVertex();

        worldrenderer
                .vertex((double)(width / 2 + borderSize), (double)(height), 0.0D)
                .uv(f7, f8)
                .color(r, g, b, 1.0F)
                //.lightmap(j, k)
                .endVertex();

        worldrenderer
                .vertex((double)(width / 2 + borderSize), (double)(-borderSize), 0.0D)
                .uv(f7, f9)
                .color(r, g, b, 1.0F)
                //.lightmap(j, k)
                .endVertex();

        tessellator.end();

        GlStateManager._enableLighting();

        GlStateManager._depthMask(true);
        GlStateManager._disableBlend();
        GlStateManager._alphaFunc(516, 0.1F);

        GlStateManager._popMatrix();
        return true;
    }
}
