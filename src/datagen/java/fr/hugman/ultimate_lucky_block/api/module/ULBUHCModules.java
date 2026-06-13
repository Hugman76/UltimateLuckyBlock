package fr.hugman.ultimate_lucky_block.api.module;

import fr.hugman.ultimate_lucky_block.impl.UltimateLuckyBlock;
import net.minecraft.resources.ResourceKey;
import fr.hugman.uhc.api.module.UHCModule;
import fr.hugman.uhc.api.registry.UHCRegistryKeys;

/**
 * @author Hugman
 * @since 1.0.0
 */
public class ULBUHCModules {
    public static final ResourceKey<UHCModule> LUCKY_BLOCKS = of("lucky_blocks");

    public static ResourceKey<UHCModule> of(String path) {
        return ResourceKey.create(UHCRegistryKeys.UHC_MODULE, UltimateLuckyBlock.id(path));
    }
}
