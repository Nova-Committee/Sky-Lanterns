package committee.nova.skylanterns.tools.player;

import committee.nova.skylanterns.common.entities.SkyLanternEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber
public class PlayerHandler {
    @SubscribeEvent
    public static void onPlayerInteractEntity(PlayerInteractEvent.EntityInteract event) {
        final Entity target = event.getTarget();
        if (!(target instanceof SkyLanternEntity)) {
            return;
        }
        final PlayerEntity player = event.getPlayer();
        if (!player.isCrouching()) {
            return;
        }
        final SkyLanternEntity lantern = (SkyLanternEntity) target;
        if (lantern.isLatched()) {
            lantern.setUnlatched();
        }
    }
}
