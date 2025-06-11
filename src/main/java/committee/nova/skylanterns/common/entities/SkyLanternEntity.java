package committee.nova.skylanterns.common.entities;

import com.mojang.math.Vector3f;
import committee.nova.skylanterns.SkyLanterns;
import committee.nova.skylanterns.common.configs.ModConfig;
import committee.nova.skylanterns.init.ModBlocks;
import committee.nova.skylanterns.init.ModEntities;
import committee.nova.skylanterns.utils.EnumColor;
import committee.nova.skylanterns.utils.TagUtils;
import committee.nova.skylanterns.utils.LevelUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

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
public class SkyLanternEntity extends PathfinderMob implements IEntityAdditionalSpawnData {


    private static final EntityDataAccessor<Byte> IS_LATCHED = SynchedEntityData.defineId(SkyLanternEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Integer> LATCHED_X = SynchedEntityData.defineId(SkyLanternEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> LATCHED_Y = SynchedEntityData.defineId(SkyLanternEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> LATCHED_Z = SynchedEntityData.defineId(SkyLanternEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> LATCHED_ID = SynchedEntityData.defineId(SkyLanternEntity.class, EntityDataSerializers.INT);

    private Vec3 windDirection = Vec3.ZERO;
    private int windChangeTimer = 0;
    private double windStrength = 0.0;
    private Vec3 driftVelocity = Vec3.ZERO;
    private int driftChangeTimer = 0;

    public BlockPos posLight = new BlockPos(BlockPos.ZERO);
    public LivingEntity latchedEntity;
    private EnumColor color = EnumColor.DARK_BLUE;
    private BlockPos latched;
    private boolean hasCachedEntity;
    private UUID cachedEntityUUID;

    public SkyLanternEntity(EntityType<SkyLanternEntity> type, Level level) {
        super(ModEntities.SKY_LANTERN.get(), level);
        //noCulling = true;

        setPos(getX() + 0.5F, getY() + 3F, getZ() + 0.5F);
        //setDeltaMovement(getDeltaMovement().x(), 0.04, getDeltaMovement().z());

    }


    @Nullable
    public static SkyLanternEntity create(Level world, double x, double y, double z, EnumColor c) {
        final SkyLanternEntity balloon = ModEntities.SKY_LANTERN.get().create(world);
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
        final SkyLanternEntity balloon = ModEntities.SKY_LANTERN.get().create(entity.level);
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
    public static SkyLanternEntity create(Level world, BlockPos pos, EnumColor c) {
        final SkyLanternEntity balloon = ModEntities.SKY_LANTERN.get().create(world);
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

    public static AttributeSupplier.Builder setAttributes() {
        return Mob.createMobAttributes()
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
                final double d0 = xo;
                final double d1 = yo + 0.15D;
                final double d2 = zo;
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
                if (level instanceof ServerLevel) {
                    final Entity entity = ((ServerLevel) level).getEntity(cachedEntityUUID);
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
                final Optional<BlockState> blockState = LevelUtils.getBlockState(level, latched);
                if (blockState.isPresent() && blockState.get().isAir()) {
                    latched = null;
                    entityData.set(IS_LATCHED, (byte) 0);
                }
            }
            if (latchedEntity != null && (latchedEntity.getHealth() <= 0 || !latchedEntity.isAlive() || latchedEntity.isRemoved())) {
                latchedEntity = null;
                entityData.set(IS_LATCHED, (byte) 0);
            }

        }

        if (!isLatched()) {
            final RandomSource rnd = this.random;
            Vec3 motion = getDeltaMovement();

            // 基础上升力
            if (motion.y() < 0.05D) {
                if (tickCount >= 40) {
                    motion = new Vec3(motion.x(), motion.y() + rnd.nextDouble() * 0.006D, motion.z());
                } else {
                    motion = new Vec3(motion.x(), motion.y() + rnd.nextDouble() * 0.003D, motion.z());
                }
            }

            if (!level.isClientSide && !this.isLeashed() && this.getLeashHolder() == null) {
                // 更新风向和强度（每60-120 tick变化一次）
                windChangeTimer--;
                if (windChangeTimer <= 0) {
                    windChangeTimer = 60 + rnd.nextInt(60); // 3-6秒变化一次

                    // 生成新的风向（0-360度）
                    double windAngle = rnd.nextDouble() * Math.PI * 2;
                    windStrength = 0.002 + rnd.nextDouble() * 0.008; // 0.002-0.01的风力强度

                    windDirection = new Vec3(
                            Math.cos(windAngle) * windStrength,
                            (rnd.nextDouble() - 0.5) * 0.002, // 轻微的垂直风力
                            Math.sin(windAngle) * windStrength
                    );
                }

                // 随机飘动（更频繁的小幅度变化）
                driftChangeTimer--;
                if (driftChangeTimer <= 0) {
                    driftChangeTimer = 20 + rnd.nextInt(40); // 1-3秒变化一次

                    // 生成随机飘动向量
                    driftVelocity = new Vec3(
                            (rnd.nextDouble() - 0.5) * 0.004,
                            (rnd.nextDouble() - 0.5) * 0.001,
                            (rnd.nextDouble() - 0.5) * 0.004
                    );
                }

                // 应用风力和飘动效果
                motion = motion.add(windDirection).add(driftVelocity);

                // 添加轻微的阻尼效果，防止速度过快
                double dampening = 0.98;
                motion = new Vec3(
                        motion.x() * dampening,
                        motion.y(), // Y轴不应用阻尼，保持上升力
                        motion.z() * dampening
                );

                // 限制最大水平速度
                double maxHorizontalSpeed = 0.15;
                double horizontalSpeed = Math.sqrt(motion.x() * motion.x() + motion.z() * motion.z());
                if (horizontalSpeed > maxHorizontalSpeed) {
                    double scale = maxHorizontalSpeed / horizontalSpeed;
                    motion = new Vec3(motion.x() * scale, motion.y(), motion.z() * scale);
                }
            }

            setDeltaMovement(motion);
            move(MoverType.SELF, getDeltaMovement());

            if (!this.level.noCollision(this.getBoundingBox())) {
                this.moveTowardsClosestSpace(this.getX(), (this.getBoundingBox().minY + this.getBoundingBox().maxY) / 2.0D, this.getZ());
            }

        } else if (latched != null) {
            setDeltaMovement(0, 0, 0);
        } else if (latchedEntity != null && latchedEntity.getHealth() > 0) {
            final int floor = getFloor(latchedEntity);
            final Vec3 motion = latchedEntity.getDeltaMovement();
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
                if (dist >= ModConfig.COMMON.lightUpdateDistanceAccuracy.get() || level.getBlockState(posLight).getBlock() != ModBlocks.AIR_LIT.get()) {
                    //remove old if needed
                    clearCurrentLightBlock();
                    //set new, setting light high in the sky is a lag fest, only allow it when directly above ground
                    posLight = blockPosition();
                    if (level.isEmptyBlock(posLight) && level.getBlockFloorHeight(posLight) + ModConfig.COMMON.lightUpdateDistanceToGround.get() > getY()) {
                        level.setBlockAndUpdate(posLight, ModBlocks.AIR_LIT.get().defaultBlockState());
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
                return posi.getY() + 1 + (entity instanceof Player ? 1 : 0);
            }
        }
        return -1;
    }


    public void clearCurrentLightBlock() {
        if (!posLight.equals(BlockPos.ZERO)) {
            final BlockState state = level.getBlockState(posLight);
            if (state.getBlock() == ModBlocks.AIR_LIT.get()) {
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
    protected void playStepSound(BlockPos pos, BlockState state) {
    }


    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        TagUtils.setEnumIfPresent(compound, "color", EnumColor::byIndexStatic, color -> this.color = color);
        TagUtils.setBlockPosIfPresent(compound, "latched", pos -> latched = pos);
        TagUtils.setUUIDIfPresent(compound, "owner", uuid -> {
            hasCachedEntity = true;
            cachedEntityUUID = uuid;
        });

        posLight = new BlockPos(compound.getInt("light_X"), compound.getInt("light_Y"), compound.getInt("light_Z"));

    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        compound.putInt("color", color.ordinal());
        if (latched != null) {
            compound.put("latched", NbtUtils.writeBlockPos(latched));
        }
        if (latchedEntity != null) {
            compound.putUUID("owner", latchedEntity.getUUID());
        }
        compound.putInt("light_X", posLight.getX());
        compound.putInt("light_Y", posLight.getY());
        compound.putInt("light_Z", posLight.getZ());

        if (this.getLeashHolder() != null) {
            CompoundTag compoundtag2 = new CompoundTag();
            if (this.getLeashHolder() instanceof LivingEntity) {
                UUID uuid = this.getLeashHolder().getUUID();
                compoundtag2.putUUID("UUID", uuid);
            } else if (this.getLeashHolder() instanceof HangingEntity) {
                BlockPos blockpos = ((HangingEntity) this.getLeashHolder()).getPos();
                compoundtag2.putInt("X", blockpos.getX());
                compoundtag2.putInt("Y", blockpos.getY());
                compoundtag2.putInt("Z", blockpos.getZ());
            }

            compound.put("Leash", compoundtag2);
        } else if (this.leashInfoTag != null) {
            compound.put("Leash", this.leashInfoTag.copy());
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
    public float getLightLevelDependentMagicValue() {
        return 15728880;
    }

    @Nonnull
    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }


    @Override
    public boolean skipAttackInteraction(@Nonnull Entity entity) {
        pop();
        return true;
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf data) {
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
    public void readSpawnData(FriendlyByteBuf data) {
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
            Vector3f colorVec = new Vector3f(
                    color.getColor(0),
                    color.getColor(1),
                    color.getColor(2)
            );
            DustParticleOptions dustParticleOptions = new DustParticleOptions(colorVec, 1.0F);
            for (int i = 0; i < 10; i++) {
                ((ServerLevel) level).sendParticles(
                        dustParticleOptions,
                        getX() + 0.6 * random.nextFloat() - 0.3,
                        getY() + 0.6 * random.nextFloat() - 0.3,
                        getZ() + 0.6 * random.nextFloat() - 0.3,
                        1, 0, 0, 0, 0
                );
            }
        }
        remove(Entity.RemovalReason.DISCARDED);
    }


    @Override
    protected float getStandingEyeHeight(@Nonnull Pose pose, @Nonnull EntityDimensions size) {
        return size.height - 0.5F;
    }

    @Nonnull
    @Override
    protected AABB getBoundingBoxForPose(@Nonnull Pose pose) {
        return getBoundingBox(getDimensions(pose), getX(), getY(), getZ());
    }

    @Override
    public void setPos(double x, double y, double z) {
        setPosRaw(x, y, z);
        setBoundingBox(getBoundingBox(getDimensions(Pose.STANDING), x, y, z));
    }

    private AABB getBoundingBox(EntityDimensions size, double x, double y, double z) {
        final float f = size.width / 2F;
        final double posY = y - 0.5F;
        return new AABB(new Vec3(x - f, posY, z - f), new Vec3(x + f, posY + size.height, z + f));
    }

    @Override
    public void refreshDimensions() {
        //NO-OP don't allow size to change
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
    public void remove(Entity.@NotNull RemovalReason reason) {
        super.remove(reason);
        // if (!level.isClientSide) {
        //     clearCurrentLightBlock();
        // }
        if (latchedEntity != null) {
            latchedEntity.hasImpulse = false;
        }
    }


    public void setUnlatched() {
        latched = null;
        entityData.set(IS_LATCHED, (byte) 0);
        if (!level.isClientSide)
            level.playSound(null, this, SoundEvents.ARMOR_EQUIP_LEATHER, SoundSource.NEUTRAL, 1F, 1F);
    }


}
