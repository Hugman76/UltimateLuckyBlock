package fr.hugman.ultimate_lucky_block.api.datagen.provider;

import fr.hugman.ultimate_lucky_block.api.item.ULBItemNames;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableSubProvider;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SetNameFunction;
import net.minecraft.world.level.storage.loot.functions.SetPotionFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

import static fr.hugman.ultimate_lucky_block.api.loot.ULBLootTables.*;

/**
 * @author Hugman
 * @since 1.0.0
 */
public class ULBLootTableProvider extends SimpleFabricLootTableSubProvider {
    private static final List<Holder<Potion>> LUCKY_POTION_EFFECTS = List.of(
            Potions.NIGHT_VISION,
            Potions.INVISIBILITY,
            Potions.LEAPING,
            Potions.FIRE_RESISTANCE,
            Potions.SWIFTNESS,
            Potions.WATER_BREATHING,
            Potions.HEALING,
            Potions.REGENERATION,
            Potions.STRENGTH,
            Potions.LUCK,
            Potions.SLOW_FALLING
    );

    public ULBLootTableProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(output, registryLookup, LootContextParamSets.BLOCK);
    }

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> lootTableBiConsumer) {
        BiConsumer<ResourceKey<LootTable>, LootTable.Builder> consumer = (lootTableRegistryKey, builder) -> builder.setRandomSequence(lootTableRegistryKey.identifier());
        consumer = consumer.andThen(lootTableBiConsumer);

        consumer.accept(LUCKY_SWORD, luckyItems(new LuckyItem(Items.GOLDEN_SWORD, ULBItemNames.LUCKY_SWORD)));
        consumer.accept(LUCKY_BOW, luckyItems(new LuckyItem(Items.BOW, ULBItemNames.LUCKY_BOW)));
        consumer.accept(LUCKY_ARMOR, luckyItems(
                new LuckyItem(Items.GOLDEN_HELMET, ULBItemNames.LUCKY_HELMET),
                new LuckyItem(Items.GOLDEN_CHESTPLATE, ULBItemNames.LUCKY_CHESTPLATE),
                new LuckyItem(Items.GOLDEN_LEGGINGS, ULBItemNames.LUCKY_LEGGINGS),
                new LuckyItem(Items.GOLDEN_BOOTS, ULBItemNames.LUCKY_BOOTS)
        ));
        consumer.accept(LUCKY_TOOL, luckyItems(
                new LuckyItem(Items.GOLDEN_PICKAXE, ULBItemNames.LUCKY_PICKAXE),
                new LuckyItem(Items.GOLDEN_SHOVEL, ULBItemNames.LUCKY_SHOVEL),
                new LuckyItem(Items.GOLDEN_AXE, ULBItemNames.LUCKY_AXE)
        ));

        var luckyPotion = LootPool.lootPool()
                .apply(SetNameFunction.setName(ULBItemNames.text(ULBItemNames.LUCKY_POTION), SetNameFunction.Target.CUSTOM_NAME));
        for (Holder<Potion> potion : LUCKY_POTION_EFFECTS) {
            luckyPotion.add(LootItem.lootTableItem(Items.POTION).apply(SetPotionFunction.setPotion(potion)));
        }
        consumer.accept(LUCKY_POTION, LootTable.lootTable().withPool(luckyPotion));

        consumer.accept(LOOT_CHEST, LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(UniformGenerator.between(3, 6))
                        .add(LootItem.lootTableItem(Items.DIAMOND).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))))
                        .add(LootItem.lootTableItem(Items.EMERALD).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6))))
                        .add(LootItem.lootTableItem(Items.GOLD_INGOT).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 9))))
                        .add(LootItem.lootTableItem(Items.IRON_INGOT).apply(SetItemCountFunction.setCount(UniformGenerator.between(4, 12))))
                        .add(LootItem.lootTableItem(Items.ENDER_PEARL).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
                        .add(LootItem.lootTableItem(Items.EXPERIENCE_BOTTLE).apply(SetItemCountFunction.setCount(UniformGenerator.between(4, 16))))
                )
                .withPool(LootPool.lootPool()
                        .add(LootItem.lootTableItem(Items.GOLDEN_APPLE).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
                        .add(LootItem.lootTableItem(Items.ENCHANTED_GOLDEN_APPLE))
                )
                .withPool(LootPool.lootPool()
                        .apply(EnchantRandomlyFunction.randomEnchantment())
                        .add(LootItem.lootTableItem(Items.GOLDEN_SWORD)
                                .apply(SetNameFunction.setName(ULBItemNames.text(ULBItemNames.LUCKY_SWORD), SetNameFunction.Target.CUSTOM_NAME)))
                        .add(LootItem.lootTableItem(Items.GOLDEN_PICKAXE)
                                .apply(SetNameFunction.setName(ULBItemNames.text(ULBItemNames.LUCKY_PICKAXE), SetNameFunction.Target.CUSTOM_NAME)))
                        .add(LootItem.lootTableItem(Items.BOW)
                                .apply(SetNameFunction.setName(ULBItemNames.text(ULBItemNames.LUCKY_BOW), SetNameFunction.Target.CUSTOM_NAME)))
                ));

        consumer.accept(JUNK, LootTable.lootTable().withPool(LootPool.lootPool()
                .setRolls(UniformGenerator.between(3, 8))
                .add(LootItem.lootTableItem(Items.ROTTEN_FLESH).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 5))))
                .add(LootItem.lootTableItem(Items.POISONOUS_POTATO))
                .add(LootItem.lootTableItem(Items.STICK).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6))))
                .add(LootItem.lootTableItem(Items.STRING).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))))
                .add(LootItem.lootTableItem(Items.BONE).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))))
                .add(LootItem.lootTableItem(Items.DIRT).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 8))))
                .add(LootItem.lootTableItem(Items.GRAVEL).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 8))))
        ));

        consumer.accept(VALUABLES, LootTable.lootTable().withPool(LootPool.lootPool()
                .setRolls(UniformGenerator.between(2, 4))
                .add(LootItem.lootTableItem(Items.DIAMOND).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
                .add(LootItem.lootTableItem(Items.EMERALD).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4))))
                .add(LootItem.lootTableItem(Items.GOLD_INGOT).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 6))))
                .add(LootItem.lootTableItem(Items.IRON_INGOT).apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 8))))
                .add(LootItem.lootTableItem(Items.LAPIS_LAZULI).apply(SetItemCountFunction.setCount(UniformGenerator.between(4, 12))))
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

    /**
     * A single randomly enchanted item, picked at random among the given ones. Every item keeps its own name, so that
     * a lucky pickaxe does not end up called a lucky shovel.
     */
    private static LootTable.Builder luckyItems(LuckyItem... items) {
        var pool = LootPool.lootPool().apply(EnchantRandomlyFunction.randomEnchantment());
        for (LuckyItem item : items) {
            pool.add(LootItem.lootTableItem(item.item())
                    .apply(SetNameFunction.setName(ULBItemNames.text(item.nameKey()), SetNameFunction.Target.CUSTOM_NAME)));
        }
        return LootTable.lootTable().withPool(pool);
    }

    private record LuckyItem(Item item, String nameKey) {}
}
