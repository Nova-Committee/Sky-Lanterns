package committee.nova.skylanterns.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import committee.nova.skylanterns.SkyLanterns;
import committee.nova.skylanterns.common.entities.SkyLanternEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/2/12 9:05
 * Version: 1.0
 */
public class PaperLanternPinkModel extends EntityModel<SkyLanternEntity> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            new ResourceLocation(SkyLanterns.MOD_ID, "paper_lantern_pink"), "main");

    private final ModelPart bottom;
    private final ModelPart top2;
    private final ModelPart top3;
    private final ModelPart top;
    private final ModelPart top21;
    private final ModelPart top31;
    private final ModelPart front;
    private final ModelPart left;
    private final ModelPart right;
    private final ModelPart back;

    public PaperLanternPinkModel(ModelPart root) {
        this.bottom = root.getChild("bottom");
        this.top2 = root.getChild("top2");
        this.top3 = root.getChild("top3");
        this.top = root.getChild("top");
        this.top21 = root.getChild("top21");
        this.top31 = root.getChild("top31");
        this.front = root.getChild("front");
        this.left = root.getChild("left");
        this.right = root.getChild("right");
        this.back = root.getChild("back");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild("bottom",
                CubeListBuilder.create()
                        .texOffs(57, 67)
                        .addBox(-24.0F, -0.5F, -24.0F, 48.0F, 1.0F, 48.0F),
                PartPose.offset(0.0F, 21.0F, 0.0F));

        partdefinition.addOrReplaceChild("top2",
                CubeListBuilder.create()
                        .texOffs(65, 116)
                        .addBox(-22.0F, -0.5F, -22.0F, 44.0F, 1.0F, 44.0F),
                PartPose.offset(0.0F, 22.0F, 0.0F));

        partdefinition.addOrReplaceChild("top3",
                CubeListBuilder.create()
                        .texOffs(73, 161)
                        .addBox(-20.0F, -0.5F, -20.0F, 40.0F, 1.0F, 40.0F),
                PartPose.offset(0.0F, 23.0F, 0.0F));

        partdefinition.addOrReplaceChild("top",
                CubeListBuilder.create()
                        .texOffs(57, 15)
                        .addBox(-24.0F, -0.5F, -24.0F, 48.0F, 1.0F, 48.0F),
                PartPose.offset(0.0F, -36.0F, 0.0F));

        partdefinition.addOrReplaceChild("top21",
                CubeListBuilder.create()
                        .texOffs(65, 20)
                        .addBox(-22.0F, -0.5F, -22.0F, 44.0F, 1.0F, 44.0F),
                PartPose.offset(0.0F, -37.0F, 0.0F));

        partdefinition.addOrReplaceChild("top31",
                CubeListBuilder.create()
                        .texOffs(73, 23)
                        .addBox(-20.0F, -0.5F, -20.0F, 40.0F, 1.0F, 40.0F),
                PartPose.offset(0.0F, -38.0F, 0.0F));

        partdefinition.addOrReplaceChild("front",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-24.0F, -28.0F, -0.5F, 48.0F, 56.0F, 1.0F),
                PartPose.offset(0.0F, -7.5F, -24.0F));

        partdefinition.addOrReplaceChild("left",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-24.0F, -28.0F, -0.5F, 48.0F, 56.0F, 1.0F),
                PartPose.offsetAndRotation(-24.0F, -7.5F, 0.0F, 0.0F, -1.570796F, 0.0F));

        partdefinition.addOrReplaceChild("right",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-24.0F, -28.0F, -0.5F, 48.0F, 56.0F, 1.0F),
                PartPose.offsetAndRotation(24.0F, -7.5F, 0.0F, 0.0F, -1.570796F, 0.0F));

        partdefinition.addOrReplaceChild("back",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-24.0F, -28.0F, -0.5F, 48.0F, 56.0F, 1.0F),
                PartPose.offset(0.0F, -7.5F, 24.0F));

        return LayerDefinition.create(meshdefinition, 256, 256);
    }

    @Override
    public void setupAnim(SkyLanternEntity pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
    }

    @Override
    public void renderToBuffer(PoseStack pPoseStack, VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha) {
        bottom.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
        top2.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
        top3.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
        top.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
        top21.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
        top31.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
        front.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
        left.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
        right.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
        back.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
    }
}