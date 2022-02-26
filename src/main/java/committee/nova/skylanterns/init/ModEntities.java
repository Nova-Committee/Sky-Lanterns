package committee.nova.skylanterns.init;

import committee.nova.skylanterns.SkyLanterns;
import committee.nova.skylanterns.common.entities.SkyLanternEntity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/2/12 8:45
 * Version: 1.0
 */
public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, SkyLanterns.MOD_ID);


    public static final RegistryObject<EntityType<SkyLanternEntity>> SkyLantern = ENTITIES.register("skylantern",
            () -> EntityType.Builder.of(SkyLanternEntity::new, EntityClassification.CREATURE)
                    .sized(1f, 1f)
                    .build(new ResourceLocation(SkyLanterns.MOD_ID, "skylantern").toString()));


}
