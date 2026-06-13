package fr.hugman.ultimate_lucky_block.api.registry;

import fr.hugman.ultimate_lucky_block.impl.UltimateLuckyBlock;
import net.minecraft.tags.TagKey;
import fr.hugman.uhc.api.module.UHCModule;
import fr.hugman.uhc.api.registry.UHCRegistryKeys;

/**
 * @author Hugman
 * @since 1.0.0
 */
public class ULBUHCModuleTags {
    public static final TagKey<UHCModule> UHCRUN = of("uhcrun");
    public static final TagKey<UHCModule> DOUBLERUNNER = of("doublerunner");

    private static TagKey<UHCModule> of(String path) {
        return TagKey.create(UHCRegistryKeys.UHC_MODULE, UltimateLuckyBlock.id(path));
    }
}