package committee.nova.skylanterns.init;

import committee.nova.skylanterns.SkyLanterns;
import committee.nova.skylanterns.common.entities.SkyLanternEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/2/12 8:45
 * Version: 1.0
 */
public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITIE_TYPE = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, SkyLanterns.MOD_ID);

    public static final RegistryObject<EntityType<SkyLanternEntity>> SKY_LANTERN = ENTITIES.register("skylantern",
            () -> EntityType.Builder.of(SkyLanternEntity::new, MobCategory.CREATURE)
                    .sized(1f, 1f)
                    .build(new ResourceLocation(SkyLanterns.MOD_ID, "skylantern").toString()));

    public static void register(IEventBus modEventBus) {
        ENTITIES.register(modEventBus);
    }
}