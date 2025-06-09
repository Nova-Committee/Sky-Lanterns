package committee.nova.skylanterns.client.model;

import committee.nova.skylanterns.SkyLanterns;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.ModelEvent;

import java.util.Map;
import java.util.function.Function;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/2/13 14:55
 * Version: 1.0
 */
public class BaseModelCache {
    private final Map<ResourceLocation, ModelData> modelMap = new Object2ObjectOpenHashMap<>();

    public static BakedModel getBakedModel(ModelEvent.BakingCompleted evt, ResourceLocation rl) {
        BakedModel bakedModel = evt.getModels().get(rl);
        if (bakedModel == null) {
            SkyLanterns.LOGGER.error("Baked model doesn't exist: {}", rl.toString());
            return evt.getModelManager().getMissingModel();
        }
        return bakedModel;
    }

    public void onBake(ModelEvent.BakingCompleted evt) {
        modelMap.values().forEach(m -> m.reload(evt));
    }

    public void setup() {
        modelMap.values().forEach(ModelData::setup);
    }

    protected JSONModelData registerJSON(ResourceLocation rl) {
        return register(rl, JSONModelData::new);
    }

    protected <DATA extends ModelData> DATA register(ResourceLocation rl, Function<ResourceLocation, DATA> creator) {
        DATA data = creator.apply(rl);
        modelMap.put(rl, data);
        return data;
    }

    public static class ModelData {
        protected final ResourceLocation rl;
        private BakedModel bakedModel;

        protected ModelData(ResourceLocation rl) {
            this.rl = rl;
        }

        protected void reload(ModelEvent.BakingCompleted evt) {
            this.bakedModel = getBakedModel(evt, rl);
        }

        protected void setup() {
        }

        public BakedModel getBakedModel(ModelEvent.BakingCompleted evt, ResourceLocation rl) {
            return bakedModel;
        }

        public ResourceLocation getResourceLocation() {
            return rl;
        }
    }

    public static class JSONModelData extends ModelData {
        private JSONModelData(ResourceLocation rl) {
            super(rl);
        }

        @Override
        protected void setup() {
        }
    }
}