package fr.hugman.ultimate_lucky_block.api.datagen.provider;

import fr.hugman.ultimate_lucky_block.api.block.ULBBlocks;
import fr.hugman.ultimate_lucky_block.api.loot.ULBLootTables;
import fr.hugman.ultimate_lucky_block.api.lucky_event.*;
import fr.hugman.ultimate_lucky_block.api.lucky_event.selector.*;
import fr.hugman.ultimate_lucky_block.api.registry.ULBRegistryKeys;
import fr.hugman.ultimate_lucky_block.impl.UltimateLuckyBlock;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import java.util.concurrent.CompletableFuture;

/**
 * @author Hugman
 * @since 1.0.0
 */
public class ULBEventProvider extends FabricDynamicRegistryProvider {
    public ULBEventProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider registries, Entries entries) {
        var registry = registries.lookupOrThrow(ULBRegistryKeys.LUCKY_EVENT);
        registry.listElementIds()
                .filter(registryKey -> registryKey.identifier().getNamespace().equals(UltimateLuckyBlock.MOD_ID))
                .map(key -> entries.add(registry, key))
                .toList();
    }

    @Override
    public String getName() {
        return "Lucky Events";
    }


    public static void register(BootstrapContext<LuckyEvent> registerable) {
        var events = registerable.lookup(ULBRegistryKeys.LUCKY_EVENT);

        // Set Blocks
        registerable.register(LuckyEvents.SET_BEDROCK, new SetBlockLuckyEvent(Blocks.BEDROCK));
        registerable.register(LuckyEvents.SET_RANDOM_LUCKY_BLOCK, new SetBlockLuckyEvent(
                ULBBlocks.SUPER_LUCKY_BLOCK,
                ULBBlocks.VERY_LUCKY_BLOCK,
                ULBBlocks.UNLUCKY_BLOCK,
                ULBBlocks.VERY_UNLUCKY_BLOCK,
                ULBBlocks.DOUBLE_LUCKY_BLOCK,
                ULBBlocks.TRIPLE_LUCKY_BLOCK
        ));
        registerable.register(LuckyEvents.SET_ORE_BLOCK, new SetBlockLuckyEvent(
                Blocks.COAL_BLOCK,
                Blocks.COPPER_BLOCK,
                Blocks.IRON_BLOCK,
                Blocks.GOLD_BLOCK,
                Blocks.REDSTONE_BLOCK,
                Blocks.LAPIS_BLOCK,
                Blocks.DIAMOND_BLOCK,
                Blocks.EMERALD_BLOCK
        ));

        // Pillars
        registerable.register(LuckyEvents.SET_BEDROCK_WORLD_PILLAR, new PillarLuckyEvent(Blocks.BEDROCK, true, true));
        registerable.register(LuckyEvents.SET_WOOL_PILLAR, new PillarLuckyEvent(new WeightedStateProvider(new WeightedList.Builder<BlockState>()
                .add(Blocks.WHITE_WOOL.defaultBlockState())
                .add(Blocks.ORANGE_WOOL.defaultBlockState())
                .add(Blocks.MAGENTA_WOOL.defaultBlockState())
                .add(Blocks.LIGHT_BLUE_WOOL.defaultBlockState())
                .add(Blocks.YELLOW_WOOL.defaultBlockState())
                .add(Blocks.LIME_WOOL.defaultBlockState())
                .add(Blocks.PINK_WOOL.defaultBlockState())
                .add(Blocks.GRAY_WOOL.defaultBlockState())
                .add(Blocks.LIGHT_GRAY_WOOL.defaultBlockState())
                .add(Blocks.CYAN_WOOL.defaultBlockState())
                .add(Blocks.PURPLE_WOOL.defaultBlockState())
                .add(Blocks.BLUE_WOOL.defaultBlockState())
                .add(Blocks.BROWN_WOOL.defaultBlockState())
                .add(Blocks.GREEN_WOOL.defaultBlockState())
                .add(Blocks.RED_WOOL.defaultBlockState())
                .add(Blocks.BLACK_WOOL.defaultBlockState())
        )));

        // Summon Entities
        registerable.register(LuckyEvents.SUMMON_TAMED_CAT, SummonEntityLuckyEvent.builder(EntityType.CAT).tamed().build());
        registerable.register(LuckyEvents.SUMMON_TAMED_WOLF, SummonEntityLuckyEvent.builder(EntityType.WOLF).tamed().build());
        registerable.register(LuckyEvents.SUMMON_RAINBOW_SHEEP, SummonEntityLuckyEvent.builder(EntityType.SHEEP).name("jeb_").build());
        registerable.register(LuckyEvents.SUMMON_HAPPY_GHAST, OneOfSelectorLuckyEvent.builder()
                .add(summonHappyGhast(Items.WHITE_HARNESS))
                .add(summonHappyGhast(Items.ORANGE_HARNESS))
                .add(summonHappyGhast(Items.MAGENTA_HARNESS))
                .add(summonHappyGhast(Items.LIGHT_BLUE_HARNESS))
                .add(summonHappyGhast(Items.YELLOW_HARNESS))
                .add(summonHappyGhast(Items.LIME_HARNESS))
                .add(summonHappyGhast(Items.PINK_HARNESS))
                .add(summonHappyGhast(Items.GRAY_HARNESS))
                .add(summonHappyGhast(Items.LIGHT_GRAY_HARNESS))
                .add(summonHappyGhast(Items.CYAN_HARNESS))
                .add(summonHappyGhast(Items.PURPLE_HARNESS))
                .add(summonHappyGhast(Items.BLUE_HARNESS))
                .add(summonHappyGhast(Items.BROWN_HARNESS))
                .add(summonHappyGhast(Items.GREEN_HARNESS))
                .add(summonHappyGhast(Items.RED_HARNESS))
                .add(summonHappyGhast(Items.BLACK_HARNESS))
                .build());

        registerable.register(LuckyEvents.SUMMON_ANGRY_WOLF, SummonEntityLuckyEvent.builder(EntityType.WOLF).shouldTarget().build());
        registerable.register(LuckyEvents.SUMMON_CREEPER, SummonEntityLuckyEvent.builder(EntityType.CREEPER).shouldTarget().build());
        registerable.register(LuckyEvents.SUMMON_GHAST, new SummonEntityLuckyEvent(EntityType.GHAST));
        registerable.register(LuckyEvents.SUMMON_WARDEN, new SummonEntityLuckyEvent(EntityType.WARDEN));
        registerable.register(LuckyEvents.SUMMON_WITHER, new SummonEntityLuckyEvent(EntityType.WITHER));
        registerable.register(LuckyEvents.SUMMON_WITCH, AllOfSelectorLuckyEvent.builder()
                .add(SummonEntityLuckyEvent.builder(EntityType.WITCH).shouldTarget().build())
                .add(RepeatSelectorLuckyEvent.builder()
                        .count(3, 6)
                        .add(new SummonEntityLuckyEvent(EntityType.BAT))
                        .build())
                .build());
        registerable.register(LuckyEvents.SUMMON_GIANT, new SummonEntityLuckyEvent(EntityType.GIANT));
        registerable.register(LuckyEvents.SUMMON_CHARGED_CREEPER, AllOfSelectorLuckyEvent.builder()
                .add(SummonEntityLuckyEvent.builder(EntityType.CREEPER).shouldTarget().build())
                .add(new SummonEntityLuckyEvent(EntityType.LIGHTNING_BOLT))
                .build());
        registerable.register(LuckyEvents.SUMMON_SLIME, OneOfSelectorLuckyEvent.builder()
                .add(RepeatSelectorLuckyEvent.builder().count(1, 3).add(new SummonEntityLuckyEvent(EntityType.SLIME)).build())
                .add(RepeatSelectorLuckyEvent.builder().count(1, 2).add(new SummonEntityLuckyEvent(EntityType.MAGMA_CUBE)).build())
                .build());

        // Loots
        registerable.register(LuckyEvents.LOOT_LUCKY_SWORD, new LootLuckyEvent(ULBLootTables.LUCKY_SWORD));
        registerable.register(LuckyEvents.LOOT_LUCKY_BOW, new LootLuckyEvent(ULBLootTables.LUCKY_BOW));
        registerable.register(LuckyEvents.LOOT_ALL_DYES, new LootLuckyEvent(ULBLootTables.ALL_DYES));
        registerable.register(LuckyEvents.LOOT_END_GAME_ITEM, new LootLuckyEvent(ULBLootTables.END_GAME_ITEM));
        registerable.register(LuckyEvents.LOOT_ELYTRA, new LootLuckyEvent(ULBLootTables.ELYTRA));
        registerable.register(LuckyEvents.LOOT_BUCKETS, new LootLuckyEvent(ULBLootTables.BUCKETS));
        registerable.register(LuckyEvents.LOOT_FISH_BUCKET, new LootLuckyEvent(ULBLootTables.FISH_BUCKET));
        registerable.register(LuckyEvents.LOOT_ROTTEN_FLESH, new LootLuckyEvent(ULBLootTables.ROTTEN_FLESH));
        registerable.register(LuckyEvents.LOOT_EGGS, new LootLuckyEvent(ULBLootTables.EGGS));
        registerable.register(LuckyEvents.LOOT_POTATOES, new LootLuckyEvent(ULBLootTables.POTATOES));
        registerable.register(LuckyEvents.LOOT_PUMPKINS, new LootLuckyEvent(ULBLootTables.PUMPKINS));
    }

    private static SummonEntityLuckyEvent summonHappyGhast(Item harness) {
        var compound = new CompoundTag();
        var equipment = new CompoundTag();
        var itemElement = new CompoundTag();
        itemElement.putString("id", BuiltInRegistries.ITEM.getKey(harness).toString());
        equipment.put("body", itemElement);
        compound.put("equipment", equipment);
        return SummonEntityLuckyEvent.builder(EntityType.HAPPY_GHAST)
                .data(compound)
                .build();
    }
}
