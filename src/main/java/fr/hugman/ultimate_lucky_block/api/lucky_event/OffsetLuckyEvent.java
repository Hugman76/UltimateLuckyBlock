package fr.hugman.ultimate_lucky_block.api.lucky_event;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.IntProviders;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/**
 * Lucky event that triggers another event somewhere else.
 *
 * <p>Each axis is an int provider, so an offset can be a fixed step aside as well as a random one. Wrapping a summon
 * in a random offset and repeating it with a {@link fr.hugman.ultimate_lucky_block.api.lucky_event.selector.RepeatSelectorLuckyEvent}
 * scatters a whole swarm over a box instead of stacking everything on one block.
 *
 * @author Hugman
 * @since 1.0.0
 */
public record OffsetLuckyEvent(
        Holder<LuckyEvent> event,
        IntProvider x,
        IntProvider y,
        IntProvider z,
        EventAnchor anchor
) implements LuckyEvent {
    public static final IntProvider DEFAULT_OFFSET = ConstantInt.ZERO;

    public static final MapCodec<OffsetLuckyEvent> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            LuckyEvent.ENTRY_CODEC.fieldOf("event").forGetter(OffsetLuckyEvent::event),
            IntProviders.CODEC.optionalFieldOf("x", DEFAULT_OFFSET).forGetter(OffsetLuckyEvent::x),
            IntProviders.CODEC.optionalFieldOf("y", DEFAULT_OFFSET).forGetter(OffsetLuckyEvent::y),
            IntProviders.CODEC.optionalFieldOf("z", DEFAULT_OFFSET).forGetter(OffsetLuckyEvent::z),
            EventAnchor.CODEC.optionalFieldOf("anchor", EventAnchor.DEFAULT).forGetter(OffsetLuckyEvent::anchor)
    ).apply(instance, OffsetLuckyEvent::new));

    public void trigger(ServerLevel level, @Nullable Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity) {
        var random = level.getRandom();
        var origin = this.anchor.resolve(pos, player);
        var target = origin.offset(this.x.sample(random), this.y.sample(random), this.z.sample(random));
        this.event.value().trigger(level, player, target, state, blockEntity);
    }

    @Override
    public LuckyEventType<?> getType() {
        return LuckyEventTypes.OFFSET;
    }

    public static Builder builder(Holder<LuckyEvent> event) {
        return new Builder(event);
    }

    public static Builder builder(LuckyEvent event) {
        return builder(Holder.direct(event));
    }

    public static class Builder {
        private final Holder<LuckyEvent> event;
        private IntProvider x = DEFAULT_OFFSET;
        private IntProvider y = DEFAULT_OFFSET;
        private IntProvider z = DEFAULT_OFFSET;
        private EventAnchor anchor = EventAnchor.DEFAULT;

        private Builder(Holder<LuckyEvent> event) {
            this.event = event;
        }

        public Builder x(IntProvider x) {
            this.x = x;
            return this;
        }

        public Builder x(int x) {
            return x(ConstantInt.of(x));
        }

        public Builder x(int min, int max) {
            return x(UniformInt.of(min, max));
        }

        public Builder y(IntProvider y) {
            this.y = y;
            return this;
        }

        public Builder y(int y) {
            return y(ConstantInt.of(y));
        }

        public Builder y(int min, int max) {
            return y(UniformInt.of(min, max));
        }

        public Builder z(IntProvider z) {
            this.z = z;
            return this;
        }

        public Builder z(int z) {
            return z(ConstantInt.of(z));
        }

        public Builder z(int min, int max) {
            return z(UniformInt.of(min, max));
        }

        /**
         * Spreads the event over a box of the given horizontal radius and height, centered on the anchor.
         */
        public Builder spread(int radius, int height) {
            return x(-radius, radius).y(0, height).z(-radius, radius);
        }

        public Builder offset(Vec3i offset) {
            return x(offset.getX()).y(offset.getY()).z(offset.getZ());
        }

        public Builder anchor(EventAnchor anchor) {
            this.anchor = anchor;
            return this;
        }

        public OffsetLuckyEvent build() {
            return new OffsetLuckyEvent(this.event, this.x, this.y, this.z, this.anchor);
        }
    }
}
