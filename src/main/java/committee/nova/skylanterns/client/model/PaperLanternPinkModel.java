package committee.nova.skylanterns.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import committee.nova.skylanterns.common.entities.SkyLanternEntity;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/2/12 9:05
 * Version: 1.0
 */
public class PaperLanternPinkModel extends EntityModel<SkyLanternEntity> {

    ModelRenderer Bottom;
    ModelRenderer Top2;
    ModelRenderer Top3;
    ModelRenderer Top;
    ModelRenderer Top21;
    ModelRenderer Top31;
    ModelRenderer Front;
    ModelRenderer Left;
    ModelRenderer Right;
    ModelRenderer Back;

    public PaperLanternPinkModel() {
        Bottom = new ModelRenderer(this, 57, 67);
        Bottom.setTexSize(256, 256);
        Bottom.addBox(-24F, -0.5F, -24F, 48, 1, 48);
        Bottom.setPos(0F, 21F, 0F);
        setRotation(Bottom, 0, 0, 0);
        Top2 = new ModelRenderer(this, 65, 116);
        Top2.setTexSize(256, 256);
        Top2.addBox(-22F, -0.5F, -22F, 44, 1, 44);
        Top2.setPos(0F, 22F, 0F);
        setRotation(Top2, 0, 0, 0);
        Top3 = new ModelRenderer(this, 73, 161);
        Top3.setTexSize(256, 256);
        Top3.addBox(-20F, -0.5F, -20F, 40, 1, 40);
        Top3.setPos(0F, 23F, 0F);
        setRotation(Top3, 0, 0, 0);
        Top = new ModelRenderer(this, 57, 15);
        Top.setTexSize(256, 256);
        Top.addBox(-24F, -0.5F, -24F, 48, 1, 48);
        Top.setPos(0F, -36F, 0F);
        setRotation(Top, 0, 0, 0);
        Top21 = new ModelRenderer(this, 65, 20);
        Top21.setTexSize(256, 256);
        Top21.addBox(-22F, -0.5F, -22F, 44, 1, 44);
        Top21.setPos(0F, -37F, 0F);
        setRotation(Top21, 0, 0, 0);
        Top31 = new ModelRenderer(this, 73, 23);
        Top31.setTexSize(256, 256);
        Top31.addBox(-20F, -0.5F, -20F, 40, 1, 40);
        Top31.setPos(0F, -38F, 0F);
        setRotation(Top31, 0, 0, 0);
        Front = new ModelRenderer(this, 0, 0);
        Front.setTexSize(256, 256);
        Front.addBox(-24F, -28F, -0.5F, 48, 56, 1);
        Front.setPos(0F, -7.5F, -24F);
        setRotation(Front, 0, 0, 0);
        Left = new ModelRenderer(this, 0, 0);
        Left.setTexSize(256, 256);
        Left.addBox(-24F, -28F, -0.5F, 48, 56, 1);
        Left.setPos(-24F, -7.5F, 0F);
        setRotation(Left, 0, -1.570796F, 0);
        Right = new ModelRenderer(this, 0, 0);
        Right.setTexSize(256, 256);
        Right.addBox(-24F, -28F, -0.5F, 48, 56, 1);
        Right.setPos(24F, -7.5F, 0F);
        setRotation(Right, 0, -1.570796F, 0);
        Back = new ModelRenderer(this, 0, 0);
        Back.setTexSize(256, 256);
        Back.addBox(-24F, -28F, -0.5F, 48, 56, 1);
        Back.setPos(0F, -7.5F, 24F);
        setRotation(Back, 0, 0, 0);
    }


    @Override
    public void setupAnim(SkyLanternEntity pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {


    }


    @Override
    public void renderToBuffer(MatrixStack pMatrixStack, IVertexBuilder pBuffer, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha) {
        Bottom.render(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
        Top2.render(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
        Top3.render(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
        Top.render(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
        Top21.render(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
        Top31.render(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
        Front.render(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
        Left.render(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
        Right.render(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
        Back.render(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);

    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.xRot = x;
        model.yRot = y;
        model.zRot = z;
    }
}
