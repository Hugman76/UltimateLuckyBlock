package fr.hugman.ultimate_lucky_block.api.datagen.provider;

import fr.hugman.ultimate_lucky_block.api.config.ULBUHCConfigs;
import fr.hugman.ultimate_lucky_block.api.module.ULBUHCModules;
import fr.hugman.ultimate_lucky_block.api.registry.ULBUHCModuleTags;
import fr.hugman.ultimate_lucky_block.impl.UltimateLuckyBlock;
import fr.hugman.uhc.api.config.UHCConfig;
import fr.hugman.uhc.api.config.UHCMapConfig;
import fr.hugman.uhc.api.config.UHCTimersConfig;
import fr.hugman.uhc.api.registry.UHCRegistryKeys;
import fr.hugman.uhc.api.util.DoubleRange;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.dimension.LevelStem;
import java.util.concurrent.CompletableFuture;

/**
 * @author Hugman
 * @since 1.0.0
 */
public class ULBUHCConfigProvider extends FabricDynamicRegistryProvider {
    public ULBUHCConfigProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider registries, Entries entries) {
        var registry = registries.lookupOrThrow(UHCRegistryKeys.UHC_CONFIG);
        registry.listElementIds()
                .filter(registryKey -> registryKey.identifier().getNamespace().equals(UltimateLuckyBlock.MOD_ID))
                .map(key -> entries.add(registry, key))
                .toList();
    }

    @Override
    public String getName() {
        return "UHC Configs (Lucky)";
    }

    public static void register(BootstrapContext<UHCConfig> registerable) {
        var modules = registerable.lookup(UHCRegistryKeys.UHC_MODULE);

        registerable.register(ULBUHCConfigs.LUCKY_UHC, new UHCConfig(UHCMapConfig.of(
                LevelStem.OVERWORLD,
                new DoubleRange(400, 10000),
                0.5D
        ), UHCTimersConfig.DEFAULT, HolderSet.direct(modules.getOrThrow(ULBUHCModules.LUCKY_BLOCKS))));
        registerable.register(ULBUHCConfigs.LUCKY_UHCRUN, new UHCConfig(UHCMapConfig.of(
                LevelStem.OVERWORLD,
                new DoubleRange(200, 8000),
                0.6D
        ), UHCTimersConfig.DEFAULT.withWarmup(1200), modules.getOrThrow(ULBUHCModuleTags.UHCRUN)));
        registerable.register(ULBUHCConfigs.LUCKY_DOUBLERUNNER, new UHCConfig(UHCMapConfig.of(
                LevelStem.OVERWORLD,
                new DoubleRange(200, 8000),
                0.75D
        ), UHCTimersConfig.DEFAULT.withWarmup(600), modules.getOrThrow(ULBUHCModuleTags.DOUBLERUNNER)));
    }
}
