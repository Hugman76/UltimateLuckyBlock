package fr.hugman.ultimate_lucky_block.api.lucky_event.selector;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.ultimate_lucky_block.api.lucky_event.LuckyEvent;
import fr.hugman.ultimate_lucky_block.api.lucky_event.LuckyEventType;
import fr.hugman.ultimate_lucky_block.api.lucky_event.LuckyEventTypes;
import fr.hugman.ultimate_lucky_block.api.registry.RegistryEntryListBuilder;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.IntProviders;
import net.minecraft.util.valueproviders.UniformInt;

/**
 * Triggers a lucky event a specified number of times.
 *
 * @author Hugman
 * @since 1.0.0
 */
public record RepeatSelectorLuckyEvent(
        IntProvider count,
        Holder<LuckyEvent> event
) implements SelectorLuckyEvent {
    public static final MapCodec<RepeatSelectorLuckyEvent> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            IntProviders.POSITIVE_CODEC.fieldOf("count").forGetter(RepeatSelectorLuckyEvent::count),
            LuckyEvent.ENTRY_CODEC.fieldOf("event").forGetter(RepeatSelectorLuckyEvent::event)
    ).apply(instance, RepeatSelectorLuckyEvent::new));

    public RepeatSelectorLuckyEvent(int count, Holder<LuckyEvent> event) {
        this(ConstantInt.of(count), event);
    }

    public RepeatSelectorLuckyEvent(int count, LuckyEvent event) {
        this(count, Holder.direct(event));
    }

    @Override
    public List<Holder<LuckyEvent>> get(RandomSource random, float luck) {
        var events = ImmutableList.<Holder<LuckyEvent>>builder();
        for (int i = 0, n = count.sample(random); i < n; i++) {
            events.add(event);
        }
        return events.build();
    }

    @Override
    public LuckyEventType<?> getType() {
        return LuckyEventTypes.REPEAT_SELECTOR;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends RegistryEntryListBuilder<Builder, LuckyEvent> {
        private IntProvider count = ConstantInt.of(1);

        private Builder() {}

        @Override
        protected Builder getThis() {
            return this;
        }

        public Builder count(IntProvider count) {
            this.count = count;
            return this;
        }

        public Builder count(int count) {
            return count(ConstantInt.of(count));
        }

        public Builder count(int min, int max) {
            return count(UniformInt.of(min, max));
        }

        public RepeatSelectorLuckyEvent build() {
            var list = entries.build();
            if (list.size() == 1) {
                return new RepeatSelectorLuckyEvent(count, list.getFirst());
            }
            return new RepeatSelectorLuckyEvent(count, Holder.direct(
                    OneOfSelectorLuckyEvent.builder().add(list).build()
            ));
        }
    }
}
