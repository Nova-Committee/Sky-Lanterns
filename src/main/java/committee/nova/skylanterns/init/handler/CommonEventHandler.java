package committee.nova.skylanterns.init.handler;

import committee.nova.skylanterns.Skylanterns;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/2/12 8:04
 * Version: 1.0
 */
@Mod.EventBusSubscriber(modid = Skylanterns.MOD_ID,bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonEventHandler {

    @SubscribeEvent
    public void onCommonSetup(final FMLCommonSetupEvent event) {

    }
}
