package fr.hugman.ultimate_lucky_block.api.config;

import fr.hugman.ultimate_lucky_block.impl.UltimateLuckyBlock;
import net.minecraft.resources.ResourceKey;
import fr.hugman.uhc.api.config.UHCConfig;
import fr.hugman.uhc.api.registry.UHCRegistryKeys;

/**
 * @author Hugman
 * @since 1.0.0
 */
public class ULBUHCConfigs {
    public static final ResourceKey<UHCConfig> LUCKY_UHC = of("lucky_uhc");
    public static final ResourceKey<UHCConfig> LUCKY_UHCRUN = of("lucky_uhcrun");
    public static final ResourceKey<UHCConfig> LUCKY_DOUBLERUNNER = of("lucky_doublerunner");

    private static ResourceKey<UHCConfig> of(String path) {
        return ResourceKey.create(UHCRegistryKeys.UHC_CONFIG, UltimateLuckyBlock.id(path));
    }
}
