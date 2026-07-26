package fr.hugman.ultimate_lucky_block.api.datagen.provider;

import fr.hugman.ultimate_lucky_block.api.block.ULBBlocks;
import fr.hugman.ultimate_lucky_block.api.loot.ULBLootTables;
import fr.hugman.ultimate_lucky_block.api.lucky_event.*;
import fr.hugman.ultimate_lucky_block.api.lucky_event.selector.*;
import fr.hugman.ultimate_lucky_block.api.registry.ULBRegistryKeys;
import fr.hugman.ultimate_lucky_block.impl.UltimateLuckyBlock;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CreakingHeartBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SpeleothemBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.storage.loot.LootTable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author Hugman
 * @since 1.0.0
 */
public class ULBEventProvider extends FabricDynamicRegistryProvider {
    /** How far up a falling trap is dropped from, and how far up the line below it has to be cleared. */
    private static final int DROP_HEIGHT = 30;
    /** How far down a pit trap goes. */
    private static final int PIT_DEPTH = 25;

    private static final BlockState PALE_OAK_PILLAR = Blocks.PALE_OAK_LOG.defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y);

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

    /**
     * A sample of every {@code minecraft:sulfur_cube_archetype}, as the blocks that give a Sulfur Cube that archetype
     * once swallowed. An archetype is picked first, then one of its blocks, so every archetype shows up as often as
     * any other however many blocks it accepts.
     */
    private static final List<List<Item>> SULFUR_CUBE_ARCHETYPES = List.of(
            // Bouncy
            List.of(Items.OAK_PLANKS, Items.SPRUCE_PLANKS, Items.BIRCH_PLANKS, Items.OAK_LOG, Items.BAMBOO_BLOCK, Items.BAMBOO_MOSAIC),
            // Explosive
            List.of(Items.TNT),
            // Fast flat
            List.of(Items.SPONGE, Items.DRIED_KELP_BLOCK, Items.MOSS_BLOCK, Items.MELON, Items.HAY_BLOCK, Items.PUMPKIN, Items.JACK_O_LANTERN, Items.OCHRE_FROGLIGHT),
            // Fast sliding
            List.of(Items.BLUE_ICE, Items.PACKED_ICE, Items.SNOW_BLOCK),
            // High resistance
            List.of(Items.SOUL_SAND, Items.SOUL_SOIL),
            // Hot
            List.of(Items.MAGMA_BLOCK),
            // Light
            Items.WOOL.asList(),
            // Regular
            List.of(Items.DIRT, Items.GRASS_BLOCK, Items.PODZOL, Items.CLAY, Items.MUD, Items.PACKED_MUD, Items.COAL_BLOCK, Items.BONE_BLOCK),
            // Slow bouncy
            List.of(Items.STONE, Items.DEEPSLATE, Items.BLACKSTONE, Items.AMETHYST_BLOCK, Items.QUARTZ_BLOCK, Items.GLOWSTONE, Items.SEA_LANTERN, Items.OBSIDIAN, Items.DIAMOND_BLOCK, Items.EMERALD_BLOCK),
            // Slow flat
            List.of(Items.IRON_BLOCK, Items.GOLD_BLOCK, Items.NETHERITE_BLOCK, Items.ANCIENT_DEBRIS, Items.RAW_IRON_BLOCK, Items.RAW_GOLD_BLOCK, Items.RAW_COPPER_BLOCK),
            // Slow sliding
            List.of(Items.BROWN_MUSHROOM_BLOCK, Items.RED_MUSHROOM_BLOCK, Items.MUSHROOM_STEM, Items.MYCELIUM, Items.SHROOMLIGHT, Items.NETHER_WART_BLOCK),
            // Sticky
            List.of(Items.HONEYCOMB_BLOCK)
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

        // Traps, built around the player rather than the block, so that they actually catch whoever set them off
        // A roofless iron bar cage, a cleared line above it, and something heavy dropped down that line. The cage is
        // exactly one block wide on the inside, so the player has nowhere to step aside to and takes the hit.
        registerable.register(LuckyEvents.SET_CAGE_TRAP, AllOfSelectorLuckyEvent.builder()
                .add(new FillLuckyEvent(Blocks.AIR, new Vec3i(0, 1, 0), new Vec3i(0, DROP_HEIGHT - 1, 0), FillLuckyEvent.Shape.SOLID, EventAnchor.PLAYER))
                .add(new FillLuckyEvent(Blocks.IRON_BARS, new Vec3i(-1, 0, -1), new Vec3i(1, 3, 1), FillLuckyEvent.Shape.WALLS, EventAnchor.PLAYER))
                .add(OffsetLuckyEvent.builder(OneOfSelectorLuckyEvent.builder()
                                .add(fallingBlock(Blocks.ANVIL.defaultBlockState()))
                                .add(fallingBlock(stalactite(Blocks.POINTED_DRIPSTONE)))
                                .add(fallingBlock(stalactite(Blocks.SULFUR_SPIKE)))
                                .build())
                        .y(DROP_HEIGHT)
                        .anchor(EventAnchor.PLAYER)
                        .build())
                .build());
        // A shaft straight down, with enough cobweb at the bottom to survive it and stay stuck there for a while.
        registerable.register(LuckyEvents.SET_PIT_TRAP, AllOfSelectorLuckyEvent.builder()
                .add(new FillLuckyEvent(Blocks.AIR, new Vec3i(-1, -1, -1), new Vec3i(1, -PIT_DEPTH, 1), FillLuckyEvent.Shape.SOLID, EventAnchor.PLAYER))
                .add(new FillLuckyEvent(Blocks.COBWEB, new Vec3i(-1, -PIT_DEPTH, -1), new Vec3i(1, -PIT_DEPTH + 1, 1), FillLuckyEvent.Shape.SOLID, EventAnchor.PLAYER))
                .build());
        // A trapped chest sitting right on top of a TNT block, which the chest powers as soon as it is opened.
        registerable.register(LuckyEvents.SET_TRAPPED_CHEST, AllOfSelectorLuckyEvent.builder()
                .add(new SetBlockLuckyEvent(BlockStateProvider.simple(Blocks.TNT), new Vec3i(0, -1, 0)))
                .add(new SetBlockLuckyEvent(Blocks.TRAPPED_CHEST, lootTable(ULBLootTables.JUNK)))
                .build());
        // The TNT is in plain sight, the sensor above it just needs someone to walk past.
        registerable.register(LuckyEvents.SET_SCULK_TRAP, AllOfSelectorLuckyEvent.builder()
                .add(new SetBlockLuckyEvent(Blocks.TNT))
                .add(new SetBlockLuckyEvent(BlockStateProvider.simple(Blocks.SCULK_SENSOR), new Vec3i(0, 1, 0)))
                .build());

        // Structures
        registerable.register(LuckyEvents.SET_LOOT_CHEST, new SetBlockLuckyEvent(Blocks.CHEST, lootTable(ULBLootTables.LOOT_CHEST)));
        // The heart goes down first: the logs placed after it are what make it notice it has what it needs to work.
        registerable.register(LuckyEvents.SET_CREAKING_HEART, AllOfSelectorLuckyEvent.builder()
                .add(new SetBlockLuckyEvent(BlockStateProvider.simple(Blocks.CREAKING_HEART.defaultBlockState()
                        .setValue(CreakingHeartBlock.AXIS, Direction.Axis.Y)), new Vec3i(0, 1, 0)))
                .add(new SetBlockLuckyEvent(BlockStateProvider.simple(PALE_OAK_PILLAR), new Vec3i(0, 0, 0)))
                .add(new SetBlockLuckyEvent(BlockStateProvider.simple(PALE_OAK_PILLAR), new Vec3i(0, 2, 0)))
                .build());
        registerable.register(LuckyEvents.SET_CONCRETE_POWDER_TOWER, new FillLuckyEvent(
                colorPool(Blocks.CONCRETE_POWDER.asList()),
                new Vec3i(0, 1, 0),
                new Vec3i(0, 10, 0)
        ));
        // A 5x5 ring of bookshelves two blocks tall around an enchanting table, with the inside cleared so that every
        // shelf actually counts towards the enchanting power.
        registerable.register(LuckyEvents.SET_ENCHANTING_SETUP, AllOfSelectorLuckyEvent.builder()
                .add(new FillLuckyEvent(Blocks.AIR, new Vec3i(-1, 0, -1), new Vec3i(1, 1, 1)))
                .add(new FillLuckyEvent(Blocks.BOOKSHELF, new Vec3i(-2, 0, -2), new Vec3i(2, 1, 2), FillLuckyEvent.Shape.WALLS))
                .add(new SetBlockLuckyEvent(Blocks.ENCHANTING_TABLE))
                .build());

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
        registerable.register(LuckyEvents.SUMMON_SULFUR_CUBE, OneOfSelectorLuckyEvent.builder()
                .add(SULFUR_CUBE_ARCHETYPES.stream()
                        .map(ULBEventProvider::summonSulfurCube)
                        .toList())
                .build());
        registerable.register(LuckyEvents.SUMMON_SHEEP_FLOOD, RepeatSelectorLuckyEvent.builder()
                .count(20, 30)
                .add(OffsetLuckyEvent.builder(new SummonEntityLuckyEvent(EntityTypes.SHEEP))
                        .x(-4, 4)
                        .z(-4, 4)
                        .build())
                .build());
        registerable.register(LuckyEvents.SUMMON_FIREWORK_SHOW, RepeatSelectorLuckyEvent.builder()
                .count(10, 20)
                .add(OffsetLuckyEvent.builder(OneOfSelectorLuckyEvent.builder()
                                .add(firework("large_ball", DyeColor.RED, DyeColor.YELLOW))
                                .add(firework("small_ball", DyeColor.LIGHT_BLUE, DyeColor.WHITE))
                                .add(firework("star", DyeColor.MAGENTA, DyeColor.PINK))
                                .add(firework("burst", DyeColor.LIME, DyeColor.CYAN))
                                .add(firework("creeper", DyeColor.GREEN, DyeColor.BLACK))
                                .build())
                        .spread(4, 2)
                        .build())
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
        registerable.register(LuckyEvents.SUMMON_ANGRY_BEE, SummonEntityLuckyEvent.builder(EntityTypes.BEE).shouldTarget().build());
        registerable.register(LuckyEvents.SUMMON_VEX_SWARM, RepeatSelectorLuckyEvent.builder()
                .count(5, 15)
                .add(OffsetLuckyEvent.builder(SummonEntityLuckyEvent.builder(EntityTypes.VEX).shouldTarget().build())
                        .spread(5, 10)
                        .anchor(EventAnchor.PLAYER)
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
        registerable.register(LuckyEvents.LOOT_LUCKY_ARMOR, new LootLuckyEvent(ULBLootTables.LUCKY_ARMOR));
        registerable.register(LuckyEvents.LOOT_LUCKY_TOOL, new LootLuckyEvent(ULBLootTables.LUCKY_TOOL));
        registerable.register(LuckyEvents.LOOT_LUCKY_POTION, new LootLuckyEvent(ULBLootTables.LUCKY_POTION));
        registerable.register(LuckyEvents.LOOT_VALUABLES, new LootLuckyEvent(ULBLootTables.VALUABLES));
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

    /**
     * A block dropped as a falling block entity, so that blocks that cannot simply be placed in mid-air still fall.
     */
    private static SummonEntityLuckyEvent fallingBlock(BlockState state) {
        var compound = new CompoundTag();
        compound.put("BlockState", NbtUtils.writeBlockState(state));
        compound.putInt("Time", 1);
        compound.putBoolean("HurtEntities", true);
        compound.putFloat("FallHurtAmount", 2.0F);
        compound.putInt("FallHurtMax", 40);
        return SummonEntityLuckyEvent.builder(EntityTypes.FALLING_BLOCK)
                .data(compound)
                .build();
    }

    private static BlockState stalactite(Block block) {
        return block.defaultBlockState().setValue(SpeleothemBlock.TIP_DIRECTION, Direction.DOWN);
    }

    private static SummonEntityLuckyEvent firework(String shape, DyeColor... colors) {
        var explosion = new CompoundTag();
        explosion.putString("shape", shape);
        explosion.putIntArray("colors", Arrays.stream(colors).mapToInt(DyeColor::getFireworkColor).toArray());
        explosion.putBoolean("has_trail", true);
        explosion.putBoolean("has_twinkle", true);
        var explosions = new ListTag();
        explosions.add(explosion);

        var fireworks = new CompoundTag();
        fireworks.put("explosions", explosions);
        fireworks.putByte("flight_duration", (byte) 1);
        var components = new CompoundTag();
        components.put("minecraft:fireworks", fireworks);
        var item = new CompoundTag();
        item.putString("id", BuiltInRegistries.ITEM.getKey(Items.FIREWORK_ROCKET).toString());
        item.put("components", components);

        var compound = new CompoundTag();
        compound.put("FireworksItem", item);
        compound.putInt("LifeTime", 20);
        return SummonEntityLuckyEvent.builder(EntityTypes.FIREWORK_ROCKET)
                .data(compound)
                .build();
    }

    /**
     * Block entity data that leaves a container to fill itself from a loot table the first time it is opened.
     */
    private static CompoundTag lootTable(ResourceKey<LootTable> lootTable) {
        var compound = new CompoundTag();
        compound.putString("LootTable", lootTable.identifier().toString());
        return compound;
    }

    private static WeightedStateProvider colorPool(List<Block> blocks) {
        var pool = new WeightedList.Builder<BlockState>();
        blocks.forEach(block -> pool.add(block.defaultBlockState()));
        return new WeightedStateProvider(pool);
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
        return summonWithBodyItem(EntityTypes.HAPPY_GHAST, harness);
    }

    /**
     * A Sulfur Cube carrying one of the blocks of the given archetype, picked at random.
     */
    private static Holder<LuckyEvent> summonSulfurCube(List<Item> archetype) {
        if (archetype.size() == 1) {
            return Holder.direct(summonWithBodyItem(EntityTypes.SULFUR_CUBE, archetype.getFirst()));
        }
        var builder = OneOfSelectorLuckyEvent.builder();
        archetype.forEach(item -> builder.add(summonWithBodyItem(EntityTypes.SULFUR_CUBE, item)));
        return Holder.direct(builder.build());
    }

    private static SummonEntityLuckyEvent summonWithBodyItem(EntityType<?> type, Item item) {
        var compound = new CompoundTag();
        var equipment = new CompoundTag();
        var itemElement = new CompoundTag();
        itemElement.putString("id", BuiltInRegistries.ITEM.getKey(item).toString());
        equipment.put("body", itemElement);
        compound.put("equipment", equipment);
        return SummonEntityLuckyEvent.builder(type)
                .data(compound)
                .build();
    }
}
