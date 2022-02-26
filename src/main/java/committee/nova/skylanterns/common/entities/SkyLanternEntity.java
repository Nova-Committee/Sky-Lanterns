package committee.nova.skylanterns.common.entities;

import committee.nova.skylanterns.SkyLanterns;
import committee.nova.skylanterns.common.configs.ModConfig;
import committee.nova.skylanterns.init.ModBlocks;
import committee.nova.skylanterns.init.ModEntities;
import committee.nova.skylanterns.utils.EnumColor;
import committee.nova.skylanterns.utils.NBTUtils;
import committee.nova.skylanterns.utils.WorldUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/2/12 7:51
 * Version: 1.0
 */
public class SkyLanternEntity extends CreatureEntity implements IEntityAdditionalSpawnData {


    private static final DataParameter<Byte> IS_LATCHED = EntityDataManager.defineId(SkyLanternEntity.class, DataSerializers.BYTE);
    private static final DataParameter<Integer> LATCHED_X = EntityDataManager.defineId(SkyLanternEntity.class, DataSerializers.INT);
    private static final DataParameter<Integer> LATCHED_Y = EntityDataManager.defineId(SkyLanternEntity.class, DataSerializers.INT);
    private static final DataParameter<Integer> LATCHED_Z = EntityDataManager.defineId(SkyLanternEntity.class, DataSerializers.INT);
    private static final DataParameter<Integer> LATCHED_ID = EntityDataManager.defineId(SkyLanternEntity.class, DataSerializers.INT);

    public BlockPos posLight = new BlockPos(BlockPos.ZERO);
    public LivingEntity latchedEntity;
    private EnumColor color = EnumColor.DARK_BLUE;
    private BlockPos latched;
    private boolean hasCachedEntity;
    private UUID cachedEntityUUID;

    public SkyLanternEntity(EntityType<SkyLanternEntity> p_i48575_1_, World p_i48575_2_) {
        super(ModEntities.SkyLantern.get(), p_i48575_2_);
        //noCulling = true;

        setPos(getX() + 0.5F, getY() + 3F, getZ() + 0.5F);
        //setDeltaMovement(getDeltaMovement().x(), 0.04, getDeltaMovement().z());

    }


    @Nullable
    public static SkyLanternEntity create(World world, double x, double y, double z, EnumColor c) {
        final SkyLanternEntity balloon = ModEntities.SkyLantern.get().create(world);
        if (balloon == null) {
            return null;
        }
        balloon.setPos(x + 0.5F, y + 3F, z + 0.5F);
        balloon.xo = balloon.getX();
        balloon.yo = balloon.getY();
        balloon.zo = balloon.getZ();
        balloon.color = c;
        return balloon;
    }


    @Nullable
    public static SkyLanternEntity create(LivingEntity entity, EnumColor c) {
        final SkyLanternEntity balloon = ModEntities.SkyLantern.get().create(entity.level);
        if (balloon == null) {
            return null;
        }
        balloon.latchedEntity = entity;
        float height = balloon.latchedEntity.getDimensions(balloon.latchedEntity.getPose()).height;
        balloon.setPos(balloon.latchedEntity.getX(), balloon.latchedEntity.getY() + height + 1.7F, balloon.latchedEntity.getZ());

        balloon.xo = balloon.getX();
        balloon.yo = balloon.getY();
        balloon.zo = balloon.getZ();

        balloon.color = c;
        balloon.entityData.set(IS_LATCHED, (byte) 2);
        balloon.entityData.set(LATCHED_ID, entity.getId());
        return balloon;
    }

    @Nullable
    public static SkyLanternEntity create(World world, BlockPos pos, EnumColor c) {
        final SkyLanternEntity balloon = ModEntities.SkyLantern.get().create(world);
        if (balloon == null) {
            return null;
        }
        balloon.latched = pos;
        balloon.setPos(balloon.latched.getX() + 0.5F, balloon.latched.getY() + 1.8F, balloon.latched.getZ() + 0.5F);

        balloon.xo = balloon.getX();
        balloon.yo = balloon.getY();
        balloon.zo = balloon.getZ();

        balloon.color = c;
        balloon.entityData.set(IS_LATCHED, (byte) 1);
        balloon.entityData.set(LATCHED_X, balloon.latched.getX());
        balloon.entityData.set(LATCHED_Y, balloon.latched.getY());
        balloon.entityData.set(LATCHED_Z, balloon.latched.getZ());
        return balloon;
    }

    public static AttributeModifierMap.MutableAttribute setAttributes() {
        return MobEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D);
    }

    public static double processYSpeed(double y, Random random) {
        double a = 0;
        if (y == 0) return 0;
        a = Math.min(y + random.nextDouble() * 0.1, 0.2F);
        SkyLanterns.LOGGER.info(a);
        return a;
    }

    public EnumColor getColor() {
        return color;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(IS_LATCHED, (byte) 0);
        entityData.define(LATCHED_X, 0);
        entityData.define(LATCHED_Y, 0);
        entityData.define(LATCHED_Z, 0);
        entityData.define(LATCHED_ID, -1);
    }

    @Override
    public void tick() {
        super.tick();

        xo = getX();
        yo = getY();
        zo = getZ();

        this.setNoGravity(true);
        this.setPersistenceRequired();


        if (getY() >= level.getMaxBuildHeight()) {
            pop();
            return;
        } else {
            if (level.random.nextInt(5) == 0) {
                final double d0 = xo;// + 0.5D;
                final double d1 = yo + 0.15D;
                final double d2 = zo;// + 0.5D;
                level.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0D, 0.0D, 0.0D);
                level.addParticle(ParticleTypes.FLAME, d0, d1, d2, 0.0D, 0.0D, 0.0D);
            }
        }

        if (level.isClientSide) {
            if (entityData.get(IS_LATCHED) == 1) {
                latched = new BlockPos(entityData.get(LATCHED_X), entityData.get(LATCHED_Y), entityData.get(LATCHED_Z));
            } else {
                latched = null;
            }
            if (entityData.get(IS_LATCHED) == 2) {
                latchedEntity = (LivingEntity) level.getEntity(entityData.get(LATCHED_ID));
            } else {
                latchedEntity = null;
            }
        } else {
            if (hasCachedEntity) {
                if (level instanceof ServerWorld) {
                    final Entity entity = ((ServerWorld) level).getEntity(cachedEntityUUID);
                    if (entity instanceof LivingEntity) {
                        latchedEntity = (LivingEntity) entity;
                    }
                }
                cachedEntityUUID = null;
                hasCachedEntity = false;
            }
            if (tickCount == 1) {
                byte isLatched;
                if (latched != null) {
                    isLatched = (byte) 1;
                } else if (latchedEntity != null) {
                    isLatched = (byte) 2;
                } else {
                    isLatched = (byte) 0;
                }
                entityData.set(IS_LATCHED, isLatched);
                entityData.set(LATCHED_X, latched == null ? 0 : latched.getX());
                entityData.set(LATCHED_Y, latched == null ? 0 : latched.getY());
                entityData.set(LATCHED_Z, latched == null ? 0 : latched.getZ());
                entityData.set(LATCHED_ID, latchedEntity == null ? -1 : latchedEntity.getId());
            }
        }


        if (!level.isClientSide) {
            if (latched != null) {
                final Optional<BlockState> blockState = WorldUtils.getBlockState(level, latched);
                if (blockState.isPresent() && blockState.get().isAir(level, latched)) {
                    latched = null;
                    entityData.set(IS_LATCHED, (byte) 0);
                }
            }
            if (latchedEntity != null && (latchedEntity.getHealth() <= 0 || !latchedEntity.isAlive() || !level.getChunkSource().isEntityTickingChunk(latchedEntity))) {
                latchedEntity = null;
                entityData.set(IS_LATCHED, (byte) 0);
            }

        }

        if (!isLatched()) {


            final Random rnd = this.random;
            Vector3d motion;

            {
                motion = getDeltaMovement();

                if (motion.y() < 0.05D) {
                    if (tickCount >= 40) {
                        motion = new Vector3d(motion.x(), motion.y() + rnd.nextDouble() * 0.006D, motion.z());
                    } else {
                        motion = new Vector3d(motion.x(), motion.y() + rnd.nextDouble() * 0.003D, motion.z());
                    }
                }
                setDeltaMovement(motion);
                move(MoverType.SELF, getDeltaMovement());

                motion = getDeltaMovement();
                if (!level.isClientSide && !this.isLeashed() && this.getLeashHolder() == null) {
                    long time = (this.getId() * 3L) + level.getGameTime() * 3;
                    float timeClampSpeed = (((time)) % 360) - 180;

                    float tiltMax = (float) Math.toRadians(timeClampSpeed);

                    double speed = 0.006;
                    if (tickCount < 40) {
                        speed = 0.004F;
                    }
                    motion = new Vector3d(motion.x() - Math.cos(tiltMax) * speed, motion.y(), motion.z() + Math.sin(tiltMax) * speed);
                }
            }

            setDeltaMovement(motion);

            if (!this.level.noCollision(this.getBoundingBox())) {
                this.moveTowardsClosestSpace(this.getX(), (this.getBoundingBox().minY + this.getBoundingBox().maxY) / 2.0D, this.getZ());
            }

        } else if (latched != null) {
            setDeltaMovement(0, 0, 0);
        } else if (latchedEntity != null && latchedEntity.getHealth() > 0) {
            final int floor = getFloor(latchedEntity);
            final Vector3d motion = latchedEntity.getDeltaMovement();
            if (latchedEntity.getY() - (floor + 1) < -0.1) {
                latchedEntity.setDeltaMovement(motion.x(), Math.max(0.04, motion.y() * 1.015), motion.z());
            } else if (latchedEntity.getY() - (floor + 1) > 0.1) {
                latchedEntity.setDeltaMovement(motion.x(), Math.min(-0.04, motion.y() * 1.015), motion.z());
            } else {
                latchedEntity.setDeltaMovement(motion.x(), 0, motion.z());
            }
            setPos(latchedEntity.getX(), latchedEntity.getY() + getAddedHeight(), latchedEntity.getZ());
        }

        if (ModConfig.COMMON.lightUpdateRate.get() != -1) {
            if (!this.level.isClientSide && (ModConfig.COMMON.lightUpdateRate.get() <= 0 || level.getGameTime() % ModConfig.COMMON.lightUpdateRate.get() == 0)) {
                final double dist = distanceToSqr(posLight.getX(), posLight.getY(), posLight.getZ());
                if (dist >= ModConfig.COMMON.lightUpdateDistanceAccuracy.get() || level.getBlockState(posLight).getBlock() != ModBlocks.Lit) {
                    //remove old if needed
                    clearCurrentLightBlock();
                    //set new, setting light high in the sky is a lag fest, only allow it when directly above ground
                    posLight = blockPosition();
                    if (level.isEmptyBlock(posLight) && level.getBlockFloorHeight(posLight) + ModConfig.COMMON.lightUpdateDistanceToGround.get() > getY()) {
                        level.setBlockAndUpdate(posLight, ModBlocks.Lit.defaultBlockState());
                    }
                }
            }
        } else {
            //if they changed the config, make sure to remove old light
            if (!this.level.isClientSide) {
                clearCurrentLightBlock();
            }
        }


        this.fallDistance = 0;
    }

    public double getAddedHeight() {
        return latchedEntity.getDimensions(latchedEntity.getPose()).height + 0.8;
    }

    private int getFloor(LivingEntity entity) {
        final BlockPos pos = new BlockPos(entity.position());
        for (BlockPos posi = pos; posi.getY() > 0; posi = posi.below()) {
            if (posi.getY() < level.getMaxBuildHeight() && !level.isEmptyBlock(posi)) {
                return posi.getY() + 1 + (entity instanceof PlayerEntity ? 1 : 0);
            }
        }
        return -1;
    }


    public void clearCurrentLightBlock() {
        if (!posLight.equals(BlockPos.ZERO)) {
            final BlockState state = level.getBlockState(posLight);
            if (state.getBlock() == ModBlocks.Lit) {
                //System.out.println("set " + posLight + " to air");
                level.setBlockAndUpdate(posLight, Blocks.AIR.defaultBlockState());
            }
            posLight = BlockPos.ZERO;
        }
    }

    @Override
    public boolean isPushable() {
        return latched == null;
    }

    @Override
    public boolean isPickable() {
        return isAlive();
    }

    @Override
    protected boolean isMovementNoisy() {
        return false;
    }


    @Override
    public void readAdditionalSaveData(CompoundNBT compound) {
        NBTUtils.setEnumIfPresent(compound, "color", EnumColor::byIndexStatic, color -> this.color = color);
        NBTUtils.setBlockPosIfPresent(compound, "latched", pos -> latched = pos);
        NBTUtils.setUUIDIfPresent(compound, "owner", uuid -> {
            hasCachedEntity = true;
            cachedEntityUUID = uuid;
        });

        posLight = new BlockPos(compound.getInt("light_X"), compound.getInt("light_Y"), compound.getInt("light_Z"));

    }

    @Override
    public void addAdditionalSaveData(CompoundNBT compound) {
        compound.putInt("color", color.ordinal());
        if (latched != null) {
            compound.put("latched", NBTUtil.writeBlockPos(latched));
        }
        if (latchedEntity != null) {
            compound.putUUID("owner", latchedEntity.getUUID());
        }
        compound.putInt("light_X", posLight.getX());
        compound.putInt("light_Y", posLight.getY());
        compound.putInt("light_Z", posLight.getZ());

        if (this.isLeashed() && getLeashHolder() == null) {
            final CompoundNBT tag = ObfuscationReflectionHelper.getPrivateValue(MobEntity.class, this, "field_110170_bx");
            if (tag != null) {
                //System.out.println("writing leashNBTTag to disk for vanilla bug fix: " + tag);
                compound.put("Leash", tag);
            }
        }
    }

    @Override
    protected void tickLeash() {

        if (this.isLeashed() && this.getLeashHolder() != null && this.getLeashHolder().level == this.level) {
            final Entity entity = this.getLeashHolder();
            this.restrictTo(new BlockPos(entity.getX(), entity.getY(), entity.getZ()), 5);
            final float f = this.distanceTo(entity);
            this.onLeashDistance(f);
            if (f > 4.0F) {
                double d0 = (entity.getX() - this.getX()) / (double) f;
                double d1 = (entity.getY() - this.getY()) / (double) f;
                double d2 = (entity.getZ() - this.getZ()) / (double) f;
                this.setDeltaMovement(this.getDeltaMovement().add(Math.copySign(d0 * d0 * 0.1D, d0), Math.copySign(d1 * d1 * 0.03D, d1), Math.copySign(d2 * d2 * 0.1D, d2)));
            }
        }
        super.tickLeash();
    }


    @Override
    public float getBrightness() {
        return 15728880;
    }

    @Nonnull
    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }


    @Override
    public boolean skipAttackInteraction(@Nonnull Entity entity) {
        pop();
        return true;
    }

    @Override
    public void writeSpawnData(PacketBuffer data) {
        data.writeDouble(getX());
        data.writeDouble(getY());
        data.writeDouble(getZ());

        data.writeEnum(color);
        if (latched != null) {
            data.writeByte((byte) 1);
            data.writeBlockPos(latched);
        } else if (latchedEntity != null) {
            data.writeByte((byte) 2);
            data.writeVarInt(latchedEntity.getId());
        } else {
            data.writeByte((byte) 0);
        }
    }

    @Override
    public void readSpawnData(PacketBuffer data) {
        setPos(data.readDouble(), data.readDouble(), data.readDouble());
        color = data.readEnum(EnumColor.class);
        final byte type = data.readByte();
        if (type == 1) {
            latched = data.readBlockPos();
        } else if (type == 2) {
            latchedEntity = (LivingEntity) level.getEntity(data.readVarInt());
        } else {
            latched = null;
        }
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double dist) {
        return dist <= 64;
    }


    @Override
    public boolean shouldRender(double x, double y, double z) {
        return true;
    }

    @Override
    public boolean hurt(@Nonnull DamageSource dmgSource, float damage) {
        if (isInvulnerableTo(dmgSource)) {
            return false;
        }
        markHurt();
        if (dmgSource != DamageSource.MAGIC && dmgSource != DamageSource.DROWN && dmgSource != DamageSource.FALL) {
            pop();
            return true;
        }
        return false;
    }

    private void pop() {
        if (!level.isClientSide) {
            final RedstoneParticleData redstoneParticleData = new RedstoneParticleData(color.getColor(0), color.getColor(1), color.getColor(2), 1.0F);
            for (int i = 0; i < 10; i++) {
                ((ServerWorld) level).sendParticles(redstoneParticleData, getX() + 0.6 * random.nextFloat() - 0.3, getY() + 0.6 * random.nextFloat() - 0.3,
                        getZ() + 0.6 * random.nextFloat() - 0.3, 1, 0, 0, 0, 0);
            }
        }
        remove();
    }


    @Override
    protected float getStandingEyeHeight(@Nonnull Pose pose, @Nonnull EntitySize size) {
        return size.height - 0.5F;
    }

    @Nonnull
    @Override
    protected AxisAlignedBB getBoundingBoxForPose(@Nonnull Pose pose) {
        return getBoundingBox(getDimensions(pose), getX(), getY(), getZ());
    }

    @Override
    public void setPos(double x, double y, double z) {
        setPosRaw(x, y, z);
        if (isAddedToWorld() && !this.level.isClientSide && level instanceof ServerWorld) {
            ((ServerWorld) this.level).updateChunkPos(this); // Forge - Process chunk registration after moving.
        }
        setBoundingBox(getBoundingBox(getDimensions(Pose.STANDING), x, y, z));
    }

    private AxisAlignedBB getBoundingBox(EntitySize size, double x, double y, double z) {
        final float f = size.width / 2F;
        final double posY = y - 0.5F;
        return new AxisAlignedBB(new Vector3d(x - f, posY, z - f), new Vector3d(x + f, posY + size.height, z + f));
    }

    @Override
    public void refreshDimensions() {
        //NO-OP don't allow size to change
    }


    @Override
    public void setLocationFromBoundingbox() {
        final AxisAlignedBB axisalignedbb = getBoundingBox();
        //Offset the y value upwards to match where it actually should be relative to the bounding box
        setPosRaw((axisalignedbb.minX + axisalignedbb.maxX) / 2D, axisalignedbb.minY + 0.5F, (axisalignedbb.minZ + axisalignedbb.maxZ) / 2D);
        if (isAddedToWorld() && !this.level.isClientSide && level instanceof ServerWorld) {
            ((ServerWorld) this.level).updateChunkPos(this); // Forge - Process chunk registration after moving.
        }
    }


    public boolean isLatched() {
        if (level.isClientSide) {
            return entityData.get(IS_LATCHED) > 0;
        }
        return latched != null || latchedEntity != null;
    }

    public boolean isLatchedToEntity() {
        return entityData.get(IS_LATCHED) == 2 && latchedEntity != null;
    }


    @Override
    public void remove() {
        super.remove();
//        if (!level.isClientSide) {
//            clearCurrentLightBlock();
//        }
        if (latchedEntity != null) {
            latchedEntity.hasImpulse = false;
        }
    }

    public void setUnlatched() {
        latched = null;
        entityData.set(IS_LATCHED, (byte) 0);
        if (!level.isClientSide)
            level.playSound(null, this, SoundEvents.ARMOR_EQUIP_LEATHER, SoundCategory.NEUTRAL, 1F, 1F);
    }


}
