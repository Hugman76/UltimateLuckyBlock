package fr.hugman.ultimate_lucky_block.api.datagen.provider;

import fr.hugman.ultimate_lucky_block.api.world.gen.feature.ULBConfiguredFeatures;
import fr.hugman.ultimate_lucky_block.api.world.gen.feature.ULBPlacedFeatures;
import fr.hugman.ultimate_lucky_block.impl.UltimateLuckyBlock;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraft.world.level.levelgen.placement.SurfaceWaterDepthFilter;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author Hugman
 * @since 1.0.0
 */
public class ULBPlacedFeatureProvider extends FabricDynamicRegistryProvider {
    public ULBPlacedFeatureProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider registries, Entries entries) {
        var registry = registries.lookupOrThrow(Registries.PLACED_FEATURE);
        registry.listElementIds()
                .filter(registryKey -> registryKey.identifier().getNamespace().equals(UltimateLuckyBlock.MOD_ID))
                .map(key -> entries.add(registry, key))
                .toList();
    }

    @Override
    public String getName() {
        return "Placed Features (Lucky)";
    }


    public static void register(BootstrapContext<PlacedFeature> registerable) {
        final var configured = registerable.lookup(Registries.CONFIGURED_FEATURE);

        var fullRangePlacement = HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(0), VerticalAnchor.belowTop(0));

        of(registerable, ULBPlacedFeatures.MINERAL_LUCKY_BLOCKS, configured.getOrThrow(ULBConfiguredFeatures.MINERAL_LUCKY_BLOCKS),
                modifiersWithCount(64, fullRangePlacement)
        );
        of(registerable, ULBPlacedFeatures.SURFACE_LUCKY_BLOCKS, configured.getOrThrow(ULBConfiguredFeatures.SURFACE_LUCKY_BLOCKS),
                CountPlacement.of(UniformInt.of(0, 2)),
                InSquarePlacement.spread(),
                SurfaceWaterDepthFilter.forMaxDepth(0),
                PlacementUtils.HEIGHTMAP_OCEAN_FLOOR,
                BlockPredicateFilter.forPredicate(BlockPredicate.solid(Direction.DOWN.getUnitVec3i()))
            );
    }

    public static void of(
            BootstrapContext<PlacedFeature> featureRegisterable,
            ResourceKey<PlacedFeature> key,
            Holder<ConfiguredFeature<?, ?>> feature,
            List<PlacementModifier> modifiers
    ) {
        PlacementUtils.register(featureRegisterable, key, feature, modifiers);
    }

    public static void of(
            BootstrapContext<PlacedFeature> featureRegisterable,
            ResourceKey<PlacedFeature> key,
            Holder<ConfiguredFeature<?, ?>> feature,
            PlacementModifier... modifiers
    ) {
        PlacementUtils.register(featureRegisterable, key, feature, modifiers);
    }

    private static List<PlacementModifier> modifiersWithCount(int count, PlacementModifier heightModifier) {
        return modifiers(CountPlacement.of(count), heightModifier);
    }

    private static List<PlacementModifier> modifiersWithRarity(int chance, PlacementModifier heightModifier) {
        return modifiers(RarityFilter.onAverageOnceEvery(chance), heightModifier);
    }

    private static List<PlacementModifier> modifiers(PlacementModifier countModifier, PlacementModifier heightModifier) {
        return List.of(countModifier, InSquarePlacement.spread(), heightModifier);
    }
}
