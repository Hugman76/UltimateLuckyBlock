package fr.hugman.ultimate_lucky_block.api.world.gen.feature;

import fr.hugman.ultimate_lucky_block.impl.UltimateLuckyBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

/**
 * @author Hugman
 * @since 1.0.0
 */
public class ULBConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> MINERAL_LUCKY_BLOCKS = of("mineral_lucky_blocks");
    public static final ResourceKey<ConfiguredFeature<?, ?>> SURFACE_LUCKY_BLOCKS = of("surface_lucky_blocks");

    private static ResourceKey<ConfiguredFeature<?, ?>> of(String path) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, UltimateLuckyBlock.id(path));
    }
}
