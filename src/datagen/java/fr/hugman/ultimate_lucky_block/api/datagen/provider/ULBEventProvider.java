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
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author Hugman
 * @since 1.0.0
 */
public class ULBEventProvider extends FabricDynamicRegistryProvider {
    private static final Map<ResourceKey<Enchantment>, Integer> BOB_ARMOR_ENCHANTMENTS = Map.of(
            Enchantments.PROTECTION, 4,
            Enchantments.UNBREAKING, 3
    );
    private static final Map<ResourceKey<Enchantment>, Integer> BOB_SWORD_ENCHANTMENTS = Map.of(
            Enchantments.SHARPNESS, 5,
            Enchantments.UNBREAKING, 3,
            Enchantments.FIRE_ASPECT, 2
    );
    private static final Map<ResourceKey<Enchantment>, Integer> BOB_SHIELD_ENCHANTMENTS = Map.of(
            Enchantments.UNBREAKING, 3
    );

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
                Blocks.COPPER_BLOCK.weathering().unaffected(),
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
                .add(Blocks.WOOL.white().defaultBlockState())
                .add(Blocks.WOOL.orange().defaultBlockState())
                .add(Blocks.WOOL.magenta().defaultBlockState())
                .add(Blocks.WOOL.lightBlue().defaultBlockState())
                .add(Blocks.WOOL.yellow().defaultBlockState())
                .add(Blocks.WOOL.lime().defaultBlockState())
                .add(Blocks.WOOL.pink().defaultBlockState())
                .add(Blocks.WOOL.gray().defaultBlockState())
                .add(Blocks.WOOL.lightGray().defaultBlockState())
                .add(Blocks.WOOL.cyan().defaultBlockState())
                .add(Blocks.WOOL.purple().defaultBlockState())
                .add(Blocks.WOOL.blue().defaultBlockState())
                .add(Blocks.WOOL.brown().defaultBlockState())
                .add(Blocks.WOOL.green().defaultBlockState())
                .add(Blocks.WOOL.red().defaultBlockState())
                .add(Blocks.WOOL.black().defaultBlockState())
        )));

        // Summon Entities
        registerable.register(LuckyEvents.SUMMON_TAMED_CAT, SummonEntityLuckyEvent.builder(EntityTypes.CAT).tamed().build());
        registerable.register(LuckyEvents.SUMMON_TAMED_WOLF, SummonEntityLuckyEvent.builder(EntityTypes.WOLF).tamed().build());
        registerable.register(LuckyEvents.SUMMON_RAINBOW_SHEEP, SummonEntityLuckyEvent.builder(EntityTypes.SHEEP).name("jeb_").build());
        registerable.register(LuckyEvents.SUMMON_HAPPY_GHAST, OneOfSelectorLuckyEvent.builder()
                .add(summonHappyGhast(Items.HARNESS.white()))
                .add(summonHappyGhast(Items.HARNESS.orange()))
                .add(summonHappyGhast(Items.HARNESS.magenta()))
                .add(summonHappyGhast(Items.HARNESS.lightBlue()))
                .add(summonHappyGhast(Items.HARNESS.yellow()))
                .add(summonHappyGhast(Items.HARNESS.lime()))
                .add(summonHappyGhast(Items.HARNESS.pink()))
                .add(summonHappyGhast(Items.HARNESS.gray()))
                .add(summonHappyGhast(Items.HARNESS.lightGray()))
                .add(summonHappyGhast(Items.HARNESS.cyan()))
                .add(summonHappyGhast(Items.HARNESS.purple()))
                .add(summonHappyGhast(Items.HARNESS.blue()))
                .add(summonHappyGhast(Items.HARNESS.brown()))
                .add(summonHappyGhast(Items.HARNESS.green()))
                .add(summonHappyGhast(Items.HARNESS.red()))
                .add(summonHappyGhast(Items.HARNESS.black()))
                .build());

        registerable.register(LuckyEvents.SUMMON_BOB, SummonEntityLuckyEvent.builder(EntityTypes.ZOMBIE)
                .data(bobEquipment())
                .name("Bob")
                .build());
        registerable.register(LuckyEvents.SUMMON_ANGRY_WOLF, SummonEntityLuckyEvent.builder(EntityTypes.WOLF).shouldTarget().build());
        registerable.register(LuckyEvents.SUMMON_CREEPER, SummonEntityLuckyEvent.builder(EntityTypes.CREEPER).shouldTarget().build());
        registerable.register(LuckyEvents.SUMMON_GHAST, new SummonEntityLuckyEvent(EntityTypes.GHAST));
        registerable.register(LuckyEvents.SUMMON_WARDEN, new SummonEntityLuckyEvent(EntityTypes.WARDEN));
        registerable.register(LuckyEvents.SUMMON_WITHER, new SummonEntityLuckyEvent(EntityTypes.WITHER));
        registerable.register(LuckyEvents.SUMMON_WITCH, AllOfSelectorLuckyEvent.builder()
                .add(SummonEntityLuckyEvent.builder(EntityTypes.WITCH).shouldTarget().build())
                .add(RepeatSelectorLuckyEvent.builder()
                        .count(3, 6)
                        .add(new SummonEntityLuckyEvent(EntityTypes.BAT))
                        .build())
                .build());
        registerable.register(LuckyEvents.SUMMON_GIANT, new SummonEntityLuckyEvent(EntityTypes.GIANT));
        registerable.register(LuckyEvents.SUMMON_CHARGED_CREEPER, AllOfSelectorLuckyEvent.builder()
                .add(SummonEntityLuckyEvent.builder(EntityTypes.CREEPER).shouldTarget().build())
                .add(new SummonEntityLuckyEvent(EntityTypes.LIGHTNING_BOLT))
                .build());
        registerable.register(LuckyEvents.SUMMON_SLIME, OneOfSelectorLuckyEvent.builder()
                .add(RepeatSelectorLuckyEvent.builder().count(1, 3).add(new SummonEntityLuckyEvent(EntityTypes.SLIME)).build())
                .add(RepeatSelectorLuckyEvent.builder().count(1, 2).add(new SummonEntityLuckyEvent(EntityTypes.MAGMA_CUBE)).build())
                .build());

        var tnt = motion(0.0D, 0.5D, 0.0D);
        tnt.putInt("fuse", 20);
        registerable.register(LuckyEvents.SUMMON_ONE_TNT, SummonEntityLuckyEvent.builder(EntityTypes.TNT)
                .data(tnt)
                .build());
        registerable.register(LuckyEvents.SUMMON_ONE_WIND_CHARGE, SummonEntityLuckyEvent.builder(EntityTypes.WIND_CHARGE)
                .data(motion(0.0D, -0.5D, 0.0D))
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

    private static CompoundTag motion(double x, double y, double z) {
        var motion = new ListTag();
        motion.add(DoubleTag.valueOf(x));
        motion.add(DoubleTag.valueOf(y));
        motion.add(DoubleTag.valueOf(z));
        var compound = new CompoundTag();
        compound.put("Motion", motion);
        return compound;
    }

    private static CompoundTag bobEquipment() {
        var equipment = new CompoundTag();
        equipment.put("head", enchantedItem(Items.DIAMOND_HELMET, BOB_ARMOR_ENCHANTMENTS));
        equipment.put("chest", enchantedItem(Items.DIAMOND_CHESTPLATE, BOB_ARMOR_ENCHANTMENTS));
        equipment.put("legs", enchantedItem(Items.DIAMOND_LEGGINGS, BOB_ARMOR_ENCHANTMENTS));
        equipment.put("feet", enchantedItem(Items.DIAMOND_BOOTS, BOB_ARMOR_ENCHANTMENTS));
        equipment.put("mainhand", enchantedItem(Items.DIAMOND_SWORD, BOB_SWORD_ENCHANTMENTS));
        equipment.put("offhand", enchantedItem(Items.SHIELD, BOB_SHIELD_ENCHANTMENTS));
        var compound = new CompoundTag();
        compound.put("equipment", equipment);
        return compound;
    }

    private static CompoundTag enchantedItem(Item item, Map<ResourceKey<Enchantment>, Integer> enchantments) {
        var enchantmentsElement = new CompoundTag();
        enchantments.forEach((enchantment, level) -> enchantmentsElement.putInt(enchantment.identifier().toString(), level));
        var components = new CompoundTag();
        components.put("minecraft:enchantments", enchantmentsElement);
        var compound = new CompoundTag();
        compound.putString("id", BuiltInRegistries.ITEM.getKey(item).toString());
        compound.put("components", components);
        return compound;
    }

    private static SummonEntityLuckyEvent summonHappyGhast(Item harness) {
        var compound = new CompoundTag();
        var equipment = new CompoundTag();
        var itemElement = new CompoundTag();
        itemElement.putString("id", BuiltInRegistries.ITEM.getKey(harness).toString());
        equipment.put("body", itemElement);
        compound.put("equipment", equipment);
        return SummonEntityLuckyEvent.builder(EntityTypes.HAPPY_GHAST)
                .data(compound)
                .build();
    }
}
