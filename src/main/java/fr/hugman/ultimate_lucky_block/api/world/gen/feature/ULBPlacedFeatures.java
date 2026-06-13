package fr.hugman.ultimate_lucky_block.api.world.gen.feature;

import fr.hugman.ultimate_lucky_block.impl.UltimateLuckyBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

/**
 * @author Hugman
 * @since 1.0.0
 */
public class ULBPlacedFeatures {
    public static final ResourceKey<PlacedFeature> MINERAL_LUCKY_BLOCKS = of("mineral_lucky_blocks");
    public static final ResourceKey<PlacedFeature> SURFACE_LUCKY_BLOCKS = of("surface_lucky_blocks");

    private static ResourceKey<PlacedFeature> of(String path) {
        return ResourceKey.create(Registries.PLACED_FEATURE, UltimateLuckyBlock.id(path));
    }
}
