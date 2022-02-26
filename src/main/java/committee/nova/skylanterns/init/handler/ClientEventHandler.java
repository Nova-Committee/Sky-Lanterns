package committee.nova.skylanterns.init.handler;

import committee.nova.skylanterns.SkyLanterns;
import committee.nova.skylanterns.client.model.ModModelCache;
import committee.nova.skylanterns.client.render.SkyLanternRender;
import committee.nova.skylanterns.init.ModEntities;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
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

        //optifine breaks (removes) forge added method setTextureEntry, dont use it

        event.addSprite(new ResourceLocation(SkyLanterns.MOD_ID + ":entities/radiant_light"));

    }


    @SubscribeEvent
    public static void modelRegEvent(ModelRegistryEvent event) {
        ModModelCache.instance.setup();
    }

    @SubscribeEvent
    public static void onModelBake(ModelBakeEvent event) {
        ModModelCache.instance.onBake(event);
    }


    @SubscribeEvent
    public static void onClientSetUpEvent(FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.SkyLantern.get(), SkyLanternRender::new);

    }
}
