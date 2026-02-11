package committee.nova.skylanterns.init.handler;

import committee.nova.skylanterns.SkyLanterns;
import committee.nova.skylanterns.client.model.ModModelCache;
import committee.nova.skylanterns.client.model.PaperLanternPinkModel;
import committee.nova.skylanterns.client.render.SkyLanternRender;
import committee.nova.skylanterns.init.ModEntities;
import committee.nova.skylanterns.init.ModRenderTypes;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.io.IOException;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/2/12 12:11
 * Version: 1.0
 */
@Mod.EventBusSubscriber(modid = SkyLanterns.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventHandler {

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(PaperLanternPinkModel.LAYER_LOCATION, PaperLanternPinkModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void onRegisterAdditional(ModelEvent.RegisterAdditional event) {
        ModModelCache.instance.setup();
    }

    @SubscribeEvent
    public static void onModelBake(ModelEvent.BakingCompleted event) {
        ModModelCache.instance.onBake(event);
    }

    @SubscribeEvent
    public static void onRegisterShaders(RegisterShadersEvent event) throws IOException {
        event.registerShader(new ShaderInstance(event.getResourceProvider(), SkyLanterns.rl("lantern_glow"), DefaultVertexFormat.POSITION_COLOR_TEX), shader -> ModRenderTypes.LANTERN_SHADER = shader);
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            EntityRenderers.register(ModEntities.SKY_LANTERN.get(), SkyLanternRender::new);
        });
    }
}