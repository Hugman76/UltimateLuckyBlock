package fr.hugman.ultimate_lucky_block.api.lucky_event;

import fr.hugman.ultimate_lucky_block.api.registry.ULBRegistryKeys;
import fr.hugman.ultimate_lucky_block.impl.UltimateLuckyBlock;
import net.minecraft.resources.ResourceKey;

/**
 * @author Hugman
 * @since 1.0.0
 */
public class LuckyPoolEvents {
    public static final ResourceKey<LuckyEvent> NORMAL = of("normal");
    public static final ResourceKey<LuckyEvent> LUCKY = of("lucky");
    public static final ResourceKey<LuckyEvent> VERY_LUCKY = of("very_lucky");
    public static final ResourceKey<LuckyEvent> UNLUCKY = of("unlucky");
    public static final ResourceKey<LuckyEvent> VERY_UNLUCKY = of("very_unlucky");
    public static final ResourceKey<LuckyEvent> DOUBLE = of("double");
    public static final ResourceKey<LuckyEvent> TRIPLE = of("triple");

    private static ResourceKey<LuckyEvent> of(String path) {
        return ResourceKey.create(ULBRegistryKeys.LUCKY_EVENT, UltimateLuckyBlock.id("pool/" + path));
    }
}
