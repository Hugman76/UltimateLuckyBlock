package fr.hugman.ultimate_lucky_block.api.datagen.provider;

import fr.hugman.uhc.api.module.UHCModules;
import fr.hugman.ultimate_lucky_block.api.block.ULBBlocks;
import fr.hugman.ultimate_lucky_block.api.module.ULBUHCModules;
import fr.hugman.ultimate_lucky_block.api.world.gen.feature.ULBPlacedFeatures;
import fr.hugman.ultimate_lucky_block.impl.UltimateLuckyBlock;
import fr.hugman.uhc.api.modifier.Modifier;
import fr.hugman.uhc.api.modifier.PlacedFeaturesModifier;
import fr.hugman.uhc.api.module.UHCModule;
import fr.hugman.uhc.api.registry.UHCRegistryKeys;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ItemLike;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class ULBUHCModuleProvider extends FabricDynamicRegistryProvider {
    public ULBUHCModuleProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider registries, Entries entries) {
        var registry = registries.lookupOrThrow(UHCRegistryKeys.UHC_MODULE);
        registry.listElementIds()
                .filter(registryKey -> registryKey.identifier().getNamespace().equals(UltimateLuckyBlock.MOD_ID))
                .map(key -> entries.add(registry, key))
                .toList();
    }

    @Override
    public String getName() {
        return "UHC Modules";
    }

    public static void register(BootstrapContext<UHCModule> registerable) {
        final var placedFeatures = registerable.lookup(Registries.PLACED_FEATURE);

        register(registerable, ULBUHCModules.LUCKY_BLOCKS, ULBBlocks.LUCKY_BLOCK,
                new PlacedFeaturesModifier(HolderSet.direct(
                        placedFeatures.getOrThrow(ULBPlacedFeatures.SURFACE_LUCKY_BLOCKS),
                        placedFeatures.getOrThrow(ULBPlacedFeatures.MINERAL_LUCKY_BLOCKS)
                ))
        );
    }

    public static void register(
            BootstrapContext<UHCModule> registerable,
            ResourceKey<UHCModule> key,
            ItemLike icon,
            Modifier... modifiers
    ) {
        registerable.register(key, UHCModules.create(key, icon, modifiers));
    }

    public static void register(
            BootstrapContext<UHCModule> registerable,
            ResourceKey<UHCModule> key,
            Function<UHCModule.Builder, UHCModule.Builder> builderFunction,
            String... longDescriptionStrings
    ) {
        registerable.register(key, UHCModules.create(key, builderFunction, longDescriptionStrings));
    }
}
