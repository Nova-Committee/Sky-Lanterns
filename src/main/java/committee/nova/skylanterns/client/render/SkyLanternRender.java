package committee.nova.skylanterns.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import committee.nova.skylanterns.SkyLanterns;
import committee.nova.skylanterns.client.model.PaperLanternPinkModel;
import committee.nova.skylanterns.common.entities.SkyLanternEntity;
import committee.nova.skylanterns.init.ModRenderTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/2/12 9:04
 * Version: 1.0
 */
public class SkyLanternRender extends EntityRenderer<SkyLanternEntity> {

    private final PaperLanternPinkModel model;
    private final Minecraft mc = Minecraft.getInstance();
    public HashMap<Integer, ResourceLocation> TEXTURES = new HashMap<>();

    public SkyLanternRender(EntityRendererProvider.Context context) {
        super(context);
        this.model = new PaperLanternPinkModel(context.bakeLayer(PaperLanternPinkModel.LAYER_LOCATION));

        TEXTURES.put(DyeColor.ORANGE.getId(), new ResourceLocation(SkyLanterns.MOD_ID + ":textures/entities/sky_lantern_orange.png"));
        TEXTURES.put(DyeColor.PINK.getId(), new ResourceLocation(SkyLanterns.MOD_ID + ":textures/entities/sky_lantern_pink.png"));
    }

    @Override
    public ResourceLocation getTextureLocation(SkyLanternEntity entity) {
        return new ResourceLocation(SkyLanterns.MOD_ID + ":textures/entities/sky_lantern_" + entity.getColor().getRegistryPrefix() + ".png");
    }

    @Override
    public void render(SkyLanternEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        pPoseStack.pushPose();

        // 位置和缩放调整
        pPoseStack.translate(0, 0.25, 0);
        float scale = 0.25F;
        pPoseStack.scale(scale, scale, scale);
        pPoseStack.mulPose(Vector3f.ZP.rotationDegrees(180));

        // 动画计算
        long time = pEntity.level.getGameTime();
        long timeBase = time + (pEntity.getId() * 10L);
        float rate = 5;

        float tiltMax = (float) Math.sin(Math.toRadians(((timeBase) * 1F) % 360)) * 5F;
        float tiltCurX = (float) Math.sin(Math.toRadians(((timeBase) * rate) % 360)) * tiltMax;
        float tiltCurY = (float) Math.sin(Math.toRadians(((timeBase + 45) * rate) % 360)) * tiltMax;
        float tiltCurZ = (float) Math.sin(Math.toRadians(((timeBase + 90) * rate) % 360)) * tiltMax;
        float rotateY = (((float) timeBase * 0.1F) % 360);

        // 应用旋转
        pPoseStack.mulPose(Vector3f.XP.rotationDegrees(tiltCurX));
        pPoseStack.mulPose(Vector3f.YP.rotationDegrees(tiltCurY + rotateY));
        pPoseStack.mulPose(Vector3f.ZP.rotationDegrees(tiltCurZ));

        this.setupRotations(pEntity, pPoseStack, pEntityYaw, pPartialTicks);
        this.model.setupAnim(pEntity, pPartialTicks, 0.0F, -0.1F, pEntityYaw, 0.0F);

        // 渲染模型
        ResourceLocation texture = this.getTextureLocation(pEntity);
        VertexConsumer vertexconsumer = pBuffer.getBuffer(ModRenderTypes.glowing(texture));
        this.model.renderToBuffer(pPoseStack, vertexconsumer, 15728880, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        pPoseStack.popPose();
        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);

        // 渲染绳索
        Entity entity = pEntity.getLeashHolder();
        if (entity != null) {
            this.renderLeash(pEntity, pPartialTicks, pPoseStack, pBuffer, entity);
        }
    }

    private <E extends Entity> void renderLeash(SkyLanternEntity pEntityLiving, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, E pLeashHolder) {
        pPoseStack.pushPose();
        Vec3 vec3 = pLeashHolder.getRopeHoldPosition(pPartialTicks);
        double d0 = (double) (Mth.lerp(pPartialTicks, pEntityLiving.yBodyRot, pEntityLiving.yBodyRotO) * ((float) Math.PI / 180F)) + (Math.PI / 2D);
        Vec3 vec31 = pEntityLiving.getLeashOffset();
        double d1 = Math.cos(d0) * vec31.z + Math.sin(d0) * vec31.x;
        double d2 = Math.sin(d0) * vec31.z - Math.cos(d0) * vec31.x;
        double d3 = Mth.lerp(pPartialTicks, pEntityLiving.xo, pEntityLiving.getX()) + d1;
        double d4 = Mth.lerp(pPartialTicks, pEntityLiving.yo, pEntityLiving.getY()) + vec31.y;
        double d5 = Mth.lerp(pPartialTicks, pEntityLiving.zo, pEntityLiving.getZ()) + d2;
        pPoseStack.translate(d1, vec31.y - pEntityLiving.getEyeHeight(), d2);

        float f = (float) (vec3.x - d3);
        float f1 = (float) (vec3.y - d4);
        float f2 = (float) (vec3.z - d5);

        VertexConsumer vertexconsumer = pBuffer.getBuffer(RenderType.leash());
        Matrix4f matrix4f = pPoseStack.last().pose();
        float f4 = Mth.fastInvSqrt(f * f + f2 * f2) * 0.025F / 2.0F;
        float f5 = f2 * f4;
        float f6 = f * f4;

        BlockPos blockpos = new BlockPos(pEntityLiving.getEyePosition(pPartialTicks));
        BlockPos blockpos1 = new BlockPos(pLeashHolder.getEyePosition(pPartialTicks));
        int i = this.getBlockLightLevel(pEntityLiving, blockpos);
        int k = pEntityLiving.level.getBrightness(LightLayer.SKY, blockpos);
        int l = pEntityLiving.level.getBrightness(LightLayer.SKY, blockpos1);

        renderSide(vertexconsumer, matrix4f, f, f1, f2, i, 0, k, l, 0.025F, 0.025F, f5, f6);
        renderSide(vertexconsumer, matrix4f, f, f1, f2, i, 0, k, l, 0.025F, 0.0F, f5, f6);
        pPoseStack.popPose();
    }

    private static void renderSide(VertexConsumer pConsumer, Matrix4f pMatrix, float pX, float pY, float pZ, int pBlockLight, int pSkyLight, int pSkyLight2, int pSkyLight3, float pMinU, float pMaxU, float pMinV, float pMaxV) {
        int i = (pBlockLight + pSkyLight) / 2;
        int j = (pSkyLight2 + pSkyLight3) / 2;

        pConsumer.vertex(pMatrix, pX - pMinV, pY + pMaxU, pZ + pMinV)
            .color(255, 255, 255, 255)
            .uv(0.0F, 0.0F)
            .uv2(i)
            .endVertex();
        pConsumer.vertex(pMatrix, pX + pMinV, pY + pMinU, pZ - pMinV)
            .color(255, 255, 255, 255)
            .uv(1.0F, 0.0F)
            .uv2(j)
            .endVertex();
        pConsumer.vertex(pMatrix, pX + pMinV, pY + pMinU, pZ - pMinV)
            .color(255, 255, 255, 255)
            .uv(1.0F, 1.0F)
            .uv2(j)
            .endVertex();
        pConsumer.vertex(pMatrix, pX - pMinV, pY + pMaxU, pZ + pMinV)
            .color(255, 255, 255, 255)
            .uv(0.0F, 1.0F)
            .uv2(i)
            .endVertex();
    }

    protected void setupRotations(SkyLanternEntity entity, PoseStack pPoseStack, float pRotationYaw, float pPartialTicks) {
        pPoseStack.mulPose(Vector3f.YP.rotationDegrees(180.0F - pRotationYaw));

        if (entity.deathTime > 0) {
            float f = ((float) entity.deathTime + pPartialTicks - 1.0F) / 20.0F * 1.6F;
            f = Mth.sqrt(f);

            if (f > 1.0F) {
                f = 1.0F;
            }
            pPoseStack.mulPose(Vector3f.YP.rotationDegrees(f * 700F));
        }
    }
}