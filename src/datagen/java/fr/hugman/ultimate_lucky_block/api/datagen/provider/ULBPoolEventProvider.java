package fr.hugman.ultimate_lucky_block.api.datagen.provider;

import fr.hugman.ultimate_lucky_block.api.lucky_event.LuckyEvent;
import fr.hugman.ultimate_lucky_block.api.lucky_event.LuckyEventTags;
import fr.hugman.ultimate_lucky_block.api.lucky_event.LuckyPoolEvents;
import fr.hugman.ultimate_lucky_block.api.lucky_event.selector.RepeatSelectorLuckyEvent;
import fr.hugman.ultimate_lucky_block.api.lucky_event.selector.WeightedListSelectorLuckyEvent;
import fr.hugman.ultimate_lucky_block.api.registry.ULBRegistryKeys;
import fr.hugman.ultimate_lucky_block.impl.UltimateLuckyBlock;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.worldgen.BootstrapContext;
import java.util.concurrent.CompletableFuture;

/**
 * Which tier a lucky block rolls, and how much the luck of the player moves that roll.
 *
 * <p>Every pool is a bell curve over the five tiers: the tier a block is named after is its most common outcome, and
 * the extremes stay rare. The weights of a pool add up to 100, so each one reads directly as its percentage at a luck
 * of {@code 0}.
 *
 * <p>Quality is how much a weight moves per point of luck. Bad tiers get a negative quality and good tiers a positive
 * one, so luck shifts probability from the bad half of the curve to the good half instead of piling more good events
 * on top. The curve keeps its shape at any luck, and a single Luck potion cannot turn a Lucky Block into a very lucky
 * event dispenser.
 *
 * @author Hugman
 * @since 1.0.0
 */
public class ULBPoolEventProvider extends FabricDynamicRegistryProvider {
    public ULBPoolEventProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider registries, Entries entries) {
        var registry = registries.lookupOrThrow(ULBRegistryKeys.LUCKY_EVENT);
        registry.listElementIds()
                .filter(registryKey -> registryKey.identifier().getNamespace().equals(UltimateLuckyBlock.MOD_ID))
                .map(key -> entries.add(registry, key))
                .toList();
    }

    @Override
    public String getName() {
        return "Lucky Events (Pools)";
    }


    public static void register(BootstrapContext<LuckyEvent> registerable) {
        var events = registerable.lookup(ULBRegistryKeys.LUCKY_EVENT);

        registerable.register(LuckyPoolEvents.NORMAL, WeightedListSelectorLuckyEvent.builder(events)
                .add(4, -2, LuckyEventTags.VERY_UNLUCKY)
                .add(20, -8, LuckyEventTags.UNLUCKY)
                .add(52, 0, LuckyEventTags.NORMAL)
                .add(20, 8, LuckyEventTags.LUCKY)
                .add(4, 2, LuckyEventTags.VERY_LUCKY)
                .build()
        );
        registerable.register(LuckyPoolEvents.LUCKY, WeightedListSelectorLuckyEvent.builder(events)
                .add(2, -2, LuckyEventTags.VERY_UNLUCKY)
                .add(10, -6, LuckyEventTags.UNLUCKY)
                .add(36, 0, LuckyEventTags.NORMAL)
                .add(40, 4, LuckyEventTags.LUCKY)
                .add(12, 4, LuckyEventTags.VERY_LUCKY)
                .build()
        );
        registerable.register(LuckyPoolEvents.VERY_LUCKY, WeightedListSelectorLuckyEvent.builder(events)
                // Never very unlucky
                .add(4, -4, LuckyEventTags.UNLUCKY)
                .add(20, 0, LuckyEventTags.NORMAL)
                .add(48, 4, LuckyEventTags.LUCKY)
                .add(28, 8, LuckyEventTags.VERY_LUCKY)
                .build()
        );
        registerable.register(LuckyPoolEvents.UNLUCKY, WeightedListSelectorLuckyEvent.builder(events)
                .add(12, -4, LuckyEventTags.VERY_UNLUCKY)
                .add(40, -6, LuckyEventTags.UNLUCKY)
                .add(36, 0, LuckyEventTags.NORMAL)
                .add(10, 6, LuckyEventTags.LUCKY)
                .add(2, 4, LuckyEventTags.VERY_LUCKY)
                .build()
        );
        registerable.register(LuckyPoolEvents.VERY_UNLUCKY, WeightedListSelectorLuckyEvent.builder(events)
                // Never very lucky
                .add(28, -8, LuckyEventTags.VERY_UNLUCKY)
                .add(48, -4, LuckyEventTags.UNLUCKY)
                .add(20, 0, LuckyEventTags.NORMAL)
                .add(4, 4, LuckyEventTags.LUCKY)
                .build()
        );
        registerable.register(LuckyPoolEvents.DOUBLE, RepeatSelectorLuckyEvent.builder().count(2).add(events.getOrThrow(LuckyPoolEvents.NORMAL)).build());
        registerable.register(LuckyPoolEvents.TRIPLE, RepeatSelectorLuckyEvent.builder().count(3).add(events.getOrThrow(LuckyPoolEvents.NORMAL)).build());
    }
}
