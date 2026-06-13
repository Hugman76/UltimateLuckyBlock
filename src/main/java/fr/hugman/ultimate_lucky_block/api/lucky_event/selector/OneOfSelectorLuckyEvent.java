package fr.hugman.ultimate_lucky_block.api.lucky_event.selector;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.ultimate_lucky_block.api.lucky_event.LuckyEvent;
import fr.hugman.ultimate_lucky_block.api.lucky_event.LuckyEventType;
import fr.hugman.ultimate_lucky_block.api.lucky_event.LuckyEventTypes;
import fr.hugman.ultimate_lucky_block.api.registry.RegistryEntryListBuilder;
import java.util.Collections;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;

/**
 * Triggers a lucky event randomly selected from a list.
 *
 * @author Hugman
 * @since 1.0.0
 */
public record OneOfSelectorLuckyEvent(HolderSet<LuckyEvent> events) implements SelectorLuckyEvent {
    public static final MapCodec<OneOfSelectorLuckyEvent> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ExtraCodecs.nonEmptyHolderSet(LuckyEvent.LIST_CODEC).fieldOf("events").forGetter(OneOfSelectorLuckyEvent::events)
    ).apply(instance, OneOfSelectorLuckyEvent::new));

    @Override
    public List<Holder<LuckyEvent>> get(RandomSource random, float luck) {
        return events.getRandomElement(random).map(Collections::singletonList).orElse(Collections.emptyList());
    }

    @Override
    public LuckyEventType<?> getType() {
        return LuckyEventTypes.ONE_OF_SELECTOR;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends RegistryEntryListBuilder<Builder, LuckyEvent> {
        private Builder() {}

        @Override
        protected Builder getThis() {
            return this;
        }

        public OneOfSelectorLuckyEvent build() {
            return new OneOfSelectorLuckyEvent(HolderSet.direct(entries.build()));
        }
    }
}
