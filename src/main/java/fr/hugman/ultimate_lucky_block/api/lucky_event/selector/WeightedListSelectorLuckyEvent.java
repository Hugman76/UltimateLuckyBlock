package fr.hugman.ultimate_lucky_block.api.lucky_event.selector;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.hugman.ultimate_lucky_block.api.lucky_event.LuckyEvent;
import fr.hugman.ultimate_lucky_block.api.lucky_event.LuckyEventType;
import fr.hugman.ultimate_lucky_block.api.lucky_event.LuckyEventTypes;
import fr.hugman.ultimate_lucky_block.api.registry.ULBRegistryKeys;
import org.apache.commons.lang3.mutable.MutableInt;

import java.util.Collections;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.RegistryAccess;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

/**
 * Triggers a lucky event randomly based on a weighted list of entries that can be impacted by luck.
 *
 * @author Hugman
 * @since 1.0.0
 */
public record WeightedListSelectorLuckyEvent(List<Entry> entries) implements SelectorLuckyEvent {
    public static final MapCodec<WeightedListSelectorLuckyEvent> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Entry.CODEC.listOf().fieldOf("entries").forGetter(WeightedListSelectorLuckyEvent::entries)
    ).apply(instance, WeightedListSelectorLuckyEvent::new));

    @Override
    public List<Holder<LuckyEvent>> get(RandomSource random, float luck) {
        var list = Lists.<Entry>newArrayList();
        var mutableInt = new MutableInt();

        for (var entry : entries) {
            int weight = entry.getWeight(luck);
            if (weight > 0) {
                mutableInt.add(weight);
                list.add(entry);
            }
        }

        var listSize = list.size();
        if (listSize == 0 || mutableInt.getValue() <= 0) {
            return List.of();
        }

        if (listSize == 1) {
            return Collections.singletonList(list.getFirst().event());
        }

        int randomValue = random.nextInt(mutableInt.getValue());
        for (var entry : list) {
            randomValue -= entry.getWeight(luck);
            if (randomValue < 0) {
                return Collections.singletonList(entry.event());
            }
        }

        return List.of();
    }

    @Override
    public LuckyEventType<?> getType() {
        return LuckyEventTypes.WEIGHTED_LIST_SELECTOR;
    }

    /**
     * A single candidate of the list.
     *
     * @param weight  how likely the entry is to be picked at a luck of {@code 0}
     * @param quality how much the weight moves for every point of luck. A positive quality makes the entry more likely
     *                as luck rises, a negative one makes it less likely, and a quality of {@code 0} leaves it
     *                untouched. An entry whose weight drops to {@code 0} or below can no longer be picked.
     */
    record Entry(Holder<LuckyEvent> event, int weight, int quality) {
        private static final int DEFAULT_WEIGHT = 1;
        private static final int DEFAULT_QUALITY = 0;

        public static final Codec<Entry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                LuckyEvent.ENTRY_CODEC.fieldOf("event").forGetter(Entry::event),
                ExtraCodecs.POSITIVE_INT.optionalFieldOf("weight", DEFAULT_WEIGHT).orElse(DEFAULT_WEIGHT).forGetter(Entry::weight),
                Codec.INT.optionalFieldOf("quality", DEFAULT_QUALITY).orElse(DEFAULT_QUALITY).forGetter(Entry::quality)
        ).apply(instance, Entry::new));

        public int getWeight(float luck) {
            return Math.max(Mth.floor((weight + quality * (luck))), 0);
        }
    }

    public static Builder builder(HolderGetter<LuckyEvent> registry) {
        return new Builder(registry);
    }

    public static Builder builder(RegistryAccess drm) {
        return builder(drm.lookupOrThrow(ULBRegistryKeys.LUCKY_EVENT));
    }

    public static class Builder {
        private final ImmutableList.Builder<Entry> entries = ImmutableList.builder();
        private final HolderGetter<LuckyEvent> lookup;

        private Builder(HolderGetter<LuckyEvent> lookup) {
            this.lookup = lookup;
        }

        public Builder add(int weight, int quality, Holder<LuckyEvent> entry) {
            this.entries.add(new Entry(entry, weight, quality));
            return this;
        }

        public Builder add(int weight, int quality, LuckyEvent event) {
            return this.add(weight, quality, Holder.direct(event));
        }

        public Builder add(int weight, int quality, LuckyEvent... events) {
            return this.add(weight, quality, OneOfSelectorLuckyEvent.builder().add(events).build());
        }

        public Builder add(int weight, int quality, TagKey<LuckyEvent> tag) {
            return this.add(weight, quality, new OneOfSelectorLuckyEvent(lookup.getOrThrow(tag)));
        }

        public WeightedListSelectorLuckyEvent build() {
            return new WeightedListSelectorLuckyEvent(entries.build());
        }
    }
}
