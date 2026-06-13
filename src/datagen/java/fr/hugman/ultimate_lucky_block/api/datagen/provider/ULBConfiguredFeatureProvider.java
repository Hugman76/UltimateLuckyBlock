package fr.hugman.ultimate_lucky_block.api.datagen.provider;

import fr.hugman.ultimate_lucky_block.api.block.ULBBlocks;
import fr.hugman.ultimate_lucky_block.api.world.gen.feature.ULBConfiguredFeatures;
import fr.hugman.ultimate_lucky_block.impl.UltimateLuckyBlock;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.ReplaceBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author Hugman
 * @since 1.0.0
 */
public class ULBConfiguredFeatureProvider extends FabricDynamicRegistryProvider {
    public ULBConfiguredFeatureProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider registries, Entries entries) {
        var registry = registries.lookupOrThrow(Registries.CONFIGURED_FEATURE);
        registry.listElementIds()
                .filter(registryKey -> registryKey.identifier().getNamespace().equals(UltimateLuckyBlock.MOD_ID))
                .map(key -> entries.add(registry, key))
                .toList();
    }

    @Override
    public String getName() {
        return "Configured Features (Lucky)";
    }

    public static void register(BootstrapContext<ConfiguredFeature<?, ?>> registerable) {
        var stoneOresReplaceables = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        var deepslateOresReplaceables = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);

        var normalProvider = new WeightedStateProvider(
                WeightedList.<BlockState>builder()
                        .add(ULBBlocks.LUCKY_BLOCK.defaultBlockState(), 15)

                        .add(ULBBlocks.SUPER_LUCKY_BLOCK.defaultBlockState(), 4)
                        .add(ULBBlocks.VERY_LUCKY_BLOCK.defaultBlockState(), 1)
                        .add(ULBBlocks.UNLUCKY_BLOCK.defaultBlockState(), 4)
                        .add(ULBBlocks.VERY_UNLUCKY_BLOCK.defaultBlockState(), 1)

                        .add(ULBBlocks.DOUBLE_LUCKY_BLOCK.defaultBlockState(), 5)
                        .add(ULBBlocks.TRIPLE_LUCKY_BLOCK.defaultBlockState(), 2)
        );

        of(registerable, ULBConfiguredFeatures.SURFACE_LUCKY_BLOCKS, Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(normalProvider));
        of(registerable, ULBConfiguredFeatures.MINERAL_LUCKY_BLOCKS, Feature.REPLACE_SINGLE_BLOCK, new ReplaceBlockConfiguration(List.of(
                OreConfiguration.target(stoneOresReplaceables, ULBBlocks.LUCKY_BLOCK.defaultBlockState()), // TODO make a stone lucky block
                OreConfiguration.target(deepslateOresReplaceables, ULBBlocks.LUCKY_BLOCK.defaultBlockState()) // TODO make a deepslate lucky block
        )));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void of(BootstrapContext<ConfiguredFeature<?, ?>> registry, ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC config) {
        FeatureUtils.register(registry, key, feature, config);
    }
}
