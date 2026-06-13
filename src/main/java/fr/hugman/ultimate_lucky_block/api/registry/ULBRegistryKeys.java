package fr.hugman.ultimate_lucky_block.api.registry;

import fr.hugman.ultimate_lucky_block.api.lucky_event.LuckyEvent;
import fr.hugman.ultimate_lucky_block.api.lucky_event.LuckyEventType;
import fr.hugman.ultimate_lucky_block.impl.UltimateLuckyBlock;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

/**
 * @author Hugman
 * @since 1.0.0
 */
public class ULBRegistryKeys {
    public static final ResourceKey<Registry<LuckyEvent>> LUCKY_EVENT = ResourceKey.createRegistryKey(UltimateLuckyBlock.id("lucky_event"));
    public static final ResourceKey<Registry<LuckyEventType<?>>> LUCKY_EVENT_TYPE = ResourceKey.createRegistryKey(UltimateLuckyBlock.id("lucky_event_type"));
}
