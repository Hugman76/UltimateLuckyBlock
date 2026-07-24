package fr.hugman.ultimate_lucky_block.api.datagen.provider;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableSubProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
import net.minecraft.world.level.storage.loot.functions.SetNameFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

import static fr.hugman.ultimate_lucky_block.api.loot.ULBLootTables.*;

/**
 * @author Hugman
 * @since 1.0.0
 */
public class ULBLootTableProvider extends SimpleFabricLootTableSubProvider {

    public ULBLootTableProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(output, registryLookup, LootContextParamSets.BLOCK);
    }

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> lootTableBiConsumer) {
        BiConsumer<ResourceKey<LootTable>, LootTable.Builder> consumer = (lootTableRegistryKey, builder) -> builder.setRandomSequence(lootTableRegistryKey.identifier());
        consumer = consumer.andThen(lootTableBiConsumer);

        consumer.accept(LUCKY_SWORD, LootTable.lootTable().withPool(LootPool.lootPool().add(
                LootItem.lootTableItem(Items.GOLDEN_SWORD)
                        .apply(EnchantRandomlyFunction.randomEnchantment())
                        .apply(SetNameFunction.setName(Component.nullToEmpty("Lucky Sword"), SetNameFunction.Target.CUSTOM_NAME)))
        ));
        consumer.accept(LUCKY_BOW, LootTable.lootTable().withPool(LootPool.lootPool().add(
                LootItem.lootTableItem(Items.BOW)
                        .apply(EnchantRandomlyFunction.randomEnchantment())
                        .apply(SetNameFunction.setName(Component.nullToEmpty("Lucky Bow"), SetNameFunction.Target.CUSTOM_NAME)))
        ));
        var allDyes = LootTable.lootTable();
        for (DyeColor color : DyeColor.values()) {
            allDyes.pool(LootPool.lootPool().add(LootItem.lootTableItem(Items.DYE.pick(color))).build());
        }
        consumer.accept(ALL_DYES, allDyes);
        consumer.accept(END_GAME_ITEM, LootTable.lootTable().pool(LootPool.lootPool()
                .add(LootItem.lootTableItem(Items.NETHER_STAR))
                .add(LootItem.lootTableItem(Items.BEACON))
                .add(LootItem.lootTableItem(Items.DRAGON_EGG))
                .add(LootItem.lootTableItem(Items.CONDUIT))
                .build()));
        consumer.accept(ELYTRA, LootTable.lootTable().withPool(LootPool.lootPool().add(LootItem.lootTableItem(Items.ELYTRA))));
        consumer.accept(BUCKETS, LootTable.lootTable().withPool(LootPool.lootPool()
                .setRolls(UniformGenerator.between(1, 20))
                .add(LootItem.lootTableItem(Items.MILK_BUCKET))
                .add(LootItem.lootTableItem(Items.LAVA_BUCKET))
                .add(LootItem.lootTableItem(Items.WATER_BUCKET))
                .add(LootItem.lootTableItem(Items.POWDER_SNOW_BUCKET))
        ));
        consumer.accept(FISH_BUCKET, LootTable.lootTable().withPool(LootPool.lootPool()
                .add(LootItem.lootTableItem(Items.PUFFERFISH_BUCKET))
                .add(LootItem.lootTableItem(Items.SALMON_BUCKET))
                .add(LootItem.lootTableItem(Items.COD_BUCKET))
                .add(LootItem.lootTableItem(Items.TROPICAL_FISH_BUCKET))
                .add(LootItem.lootTableItem(Items.AXOLOTL_BUCKET))
                .add(LootItem.lootTableItem(Items.TADPOLE_BUCKET))
        ));
        consumer.accept(ROTTEN_FLESH, LootTable.lootTable().withPool(LootPool.lootPool()
                .add(LootItem.lootTableItem(Items.ROTTEN_FLESH))
        ));
        consumer.accept(EGGS, LootTable.lootTable().withPool(LootPool.lootPool()
                .setRolls(UniformGenerator.between(1, 20))
                .add(LootItem.lootTableItem(Items.EGG))
                .add(LootItem.lootTableItem(Items.BROWN_EGG))
                .add(LootItem.lootTableItem(Items.BLUE_EGG))
        ));
        consumer.accept(POTATOES, LootTable.lootTable().withPool(LootPool.lootPool()
                .setRolls(UniformGenerator.between(1, 20))
                .add(LootItem.lootTableItem(Items.POTATO))
                .add(LootItem.lootTableItem(Items.POISONOUS_POTATO))
                .add(LootItem.lootTableItem(Items.BAKED_POTATO))
        ));
        consumer.accept(PUMPKINS, LootTable.lootTable().withPool(LootPool.lootPool()
                .setRolls(UniformGenerator.between(1, 20))
                .add(LootItem.lootTableItem(Items.PUMPKIN))
                .add(LootItem.lootTableItem(Items.CARVED_PUMPKIN))
        ));
    }
}
