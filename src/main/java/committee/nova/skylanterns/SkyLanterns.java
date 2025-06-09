package committee.nova.skylanterns;

import committee.nova.skylanterns.common.entities.SkyLanternEntity;
import committee.nova.skylanterns.init.ModBlocks;
import committee.nova.skylanterns.init.ModEntities;
import committee.nova.skylanterns.init.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(SkyLanterns.MOD_ID)
public class SkyLanterns {

    public static final String MOD_ID = "skylanterns";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public SkyLanterns() {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModEntities.register(modEventBus);

        modEventBus.addListener(this::addAttributes);

        MinecraftForge.EVENT_BUS.register(this);
    }

    public static ResourceLocation rl(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    private void addAttributes(final EntityAttributeCreationEvent event) {
        event.put(ModEntities.SKY_LANTERN.get(), SkyLanternEntity.setAttributes().build());
    }
}