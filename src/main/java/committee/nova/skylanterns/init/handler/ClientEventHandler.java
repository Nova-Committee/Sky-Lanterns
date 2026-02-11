package committee.nova.skylanterns.init.handler;

import committee.nova.skylanterns.SkyLanterns;
import committee.nova.skylanterns.client.model.ModModelCache;
import committee.nova.skylanterns.client.model.PaperLanternPinkModel;
import committee.nova.skylanterns.client.render.SkyLanternRender;
import committee.nova.skylanterns.init.ModEntities;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/2/12 12:11
 * Version: 1.0
 */
@Mod.EventBusSubscriber(modid = SkyLanterns.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventHandler {

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void registerIconsPre(TextureStitchEvent.Pre event) {
        event.addSprite(new ResourceLocation(SkyLanterns.MOD_ID + ":entities/radiant_light"));
    }

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
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            EntityRenderers.register(ModEntities.SKY_LANTERN.get(), SkyLanternRender::new);
        });
    }
}