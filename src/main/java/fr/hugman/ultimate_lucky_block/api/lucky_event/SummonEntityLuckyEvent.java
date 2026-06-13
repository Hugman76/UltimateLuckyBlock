package fr.hugman.ultimate_lucky_block.api.lucky_event;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.ultimate_lucky_block.impl.UltimateLuckyBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.TypedEntityData;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/**
 * Lucky event that summons an entity.
 *
 * @author Hugman
 * @since 1.0.0
 */
public record SummonEntityLuckyEvent(
        TypedEntityData<EntityType<?>> entityData,
        boolean shouldTarget,
        boolean tamed
) implements LuckyEvent {
    public static final boolean DEFAULT_SHOUlD_TARGET = false;
    public static final boolean DEFAULT_TAMED = false;

    public static final MapCodec<SummonEntityLuckyEvent> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            TypedEntityData.codec(EntityType.CODEC).fieldOf("entity_data").forGetter(SummonEntityLuckyEvent::entityData),
            Codec.BOOL.optionalFieldOf("should_target", false).forGetter(SummonEntityLuckyEvent::shouldTarget),
            Codec.BOOL.optionalFieldOf("tamed", false).forGetter(SummonEntityLuckyEvent::tamed)
    ).apply(instance, SummonEntityLuckyEvent::new));

    public SummonEntityLuckyEvent(EntityType<?> entityType) {
        this(TypedEntityData.of(entityType, new CompoundTag()), DEFAULT_SHOUlD_TARGET, DEFAULT_TAMED);
    }

    public void trigger(ServerLevel world, @Nullable Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity) {
        var entity = EntityType.loadEntityRecursive(this.entityData.type(), this.entityData.copyTagWithoutId(), world, EntitySpawnReason.MOB_SUMMONED, e -> {
            e.snapTo(pos, e.getRandom().nextFloat() * 360.0F, 0.0F);
            return e;
        });
        if (entity == null) {
            UltimateLuckyBlock.LOGGER.error("Failed to summon entity from NBT: {}", this.entityData);
            return;
        }

        if (this.tamed && entity instanceof TamableAnimal tameable && player != null) {
            tameable.tame(player);
        }
        if (entity instanceof Mob mob) {
            mob.finalizeSpawn(world, world.getCurrentDifficultyAt(pos), EntitySpawnReason.MOB_SUMMONED, null);
            if (this.shouldTarget  && player != null) {
                mob.setTarget(player);
            }
        }
        if (entity instanceof NeutralMob angerable && this.shouldTarget && player != null) {
            angerable.setTarget(player);
        }
        if (!world.tryAddFreshEntityWithPassengers(entity)) {
            UltimateLuckyBlock.LOGGER.error("Failed to spawn entity: {}", entity);
        }
    }

    @Override
    public LuckyEventType<?> getType() {
        return LuckyEventTypes.SUMMON_ENTITY;
    }

    public static Builder builder(EntityType<?> type) {
        return new Builder(type);
    }

    public static class Builder {
        private final EntityType<?> type;
        private CompoundTag data = new CompoundTag();
        private boolean target = DEFAULT_SHOUlD_TARGET;
        private boolean tamed = DEFAULT_TAMED;

        public Builder(EntityType<?> type) {
            this.type = type;
        }

        public Builder data(CompoundTag data) {
            this.data = data;
            return this;
        }

        public Builder name(String name) {
            this.data.putString("CustomName", name);
            return this;
        }

        public Builder shouldTarget() {
            this.target = true;
            return this;
        }

        public Builder tamed() {
            this.tamed = true;
            return this;
        }

        public SummonEntityLuckyEvent build() {
            return new SummonEntityLuckyEvent(TypedEntityData.of(type, data), target, tamed);
        }
    }

    private static CustomData withType(CompoundTag compound, EntityType<?> entityType) {
        compound.putString("id", BuiltInRegistries.ENTITY_TYPE.getKey(entityType).toString());
        return CustomData.of(compound);
    }
}
