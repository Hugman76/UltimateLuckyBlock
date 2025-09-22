package fr.hugman.ultimate_lucky_block.impl.data;

import fr.hugman.uhc.api.registry.UHCRegistryKeys;
import fr.hugman.ultimate_lucky_block.api.datagen.provider.*;
import fr.hugman.ultimate_lucky_block.api.registry.ULBRegistryKeys;
import fr.hugman.ultimate_lucky_block.impl.UltimateLuckyBlock;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;
import org.jetbrains.annotations.Nullable;
import xyz.nucleoid.plasmid.api.game.config.GameConfigs;

public class ULBDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(ULBModelProvider::new);

        // - Lucky Block
        pack.addProvider(ULBEventProvider::new);
        pack.addProvider(ULBPoolEventProvider::new);

        // - UHC
        pack.addProvider(ULBUHCConfigProvider::new);
        pack.addProvider(ULBGameProvider::new);
        pack.addProvider(ULBUHCModuleProvider::new);
        pack.addProvider(ULBUHCModuleTagProvider::new);

        // - Tags
        pack.addProvider(ULBEventTagProvider::new);

        // - World Generation
        pack.addProvider(ULBConfiguredFeatureProvider::new);
        pack.addProvider(ULBPlacedFeatureProvider::new);

        // - Loot Tables
        pack.addProvider(ULBLootTableProvider::new);
    }

    @Override
    public void buildRegistry(RegistryBuilder registryBuilder) {
        // - Lucky Block
        registryBuilder.addRegistry(ULBRegistryKeys.LUCKY_EVENT, ULBEventProvider::register);
        registryBuilder.addRegistry(ULBRegistryKeys.LUCKY_EVENT, ULBPoolEventProvider::register);

        // - UHC
        registryBuilder.addRegistry(GameConfigs.REGISTRY_KEY, ULBGameProvider::register);
        registryBuilder.addRegistry(UHCRegistryKeys.UHC_CONFIG, ULBUHCConfigProvider::register);
        registryBuilder.addRegistry(UHCRegistryKeys.UHC_MODULE, ULBUHCModuleProvider::register);

        // - World Generation
        registryBuilder.addRegistry(RegistryKeys.CONFIGURED_FEATURE, ULBConfiguredFeatureProvider::register);
        registryBuilder.addRegistry(RegistryKeys.PLACED_FEATURE, ULBPlacedFeatureProvider::register);
    }

    @Override
    @Nullable
    public String getEffectiveModId() {
        return UltimateLuckyBlock.MOD_ID;
    }
}
