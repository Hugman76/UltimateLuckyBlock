package fr.hugman.ultimate_lucky_block.api.lucky_event;

import fr.hugman.ultimate_lucky_block.api.registry.ULBRegistryKeys;
import fr.hugman.ultimate_lucky_block.impl.UltimateLuckyBlock;
import net.minecraft.resources.ResourceKey;

/**
 * @author Hugman
 * @since 1.0.0
 */
public class LuckyEvents {
    // Set Block
    public static final ResourceKey<LuckyEvent> SET_BEDROCK = of("set_bedrock");
    public static final ResourceKey<LuckyEvent> SET_BEDROCK_WORLD_PILLAR = of("set_bedrock_world_pillar");
    public static final ResourceKey<LuckyEvent> SET_RANDOM_LUCKY_BLOCK = of("set_random_lucky_block");
    public static final ResourceKey<LuckyEvent> SET_ORE_BLOCK = of("set_ore_block");
    public static final ResourceKey<LuckyEvent> SET_WOOL_PILLAR = of("set_wool_pillar");

    // Structures
    public static final ResourceKey<LuckyEvent> SET_CONCRETE_POWDER_TOWER = of("set_concrete_powder_tower");
    public static final ResourceKey<LuckyEvent> SET_ENCHANTING_SETUP = of("set_enchanting_setup");
    public static final ResourceKey<LuckyEvent> SET_CREAKING_HEART = of("set_creaking_heart");
    public static final ResourceKey<LuckyEvent> SET_LOOT_CHEST = of("set_loot_chest");

    // Traps
    public static final ResourceKey<LuckyEvent> SET_CAGE_TRAP = of("set_cage_trap");
    public static final ResourceKey<LuckyEvent> SET_PIT_TRAP = of("set_pit_trap");
    public static final ResourceKey<LuckyEvent> SET_TRAPPED_CHEST = of("set_trapped_chest");
    public static final ResourceKey<LuckyEvent> SET_SCULK_TRAP = of("set_sculk_trap");

    // Summon Entities
    public static final ResourceKey<LuckyEvent> SUMMON_TAMED_WOLF = of("summon_tamed_wolf");
    public static final ResourceKey<LuckyEvent> SUMMON_TAMED_CAT = of("summon_tamed_cat");
    public static final ResourceKey<LuckyEvent> SUMMON_HAPPY_GHAST = of("summon_happy_ghast");
    public static final ResourceKey<LuckyEvent> SUMMON_RAINBOW_SHEEP = of("summon_rainbow_sheep");
    public static final ResourceKey<LuckyEvent> SUMMON_SULFUR_CUBE = of("summon_sulfur_cube");
    public static final ResourceKey<LuckyEvent> SUMMON_SHEEP_FLOOD = of("summon_sheep_flood");
    public static final ResourceKey<LuckyEvent> SUMMON_FIREWORK_SHOW = of("summon_firework_show");

    public static final ResourceKey<LuckyEvent> SUMMON_BOB = of("summon_bob");
    public static final ResourceKey<LuckyEvent> SUMMON_ANGRY_WOLF = of("summon_angry_wolf");
    public static final ResourceKey<LuckyEvent> SUMMON_CREEPER = of("summon_creeper");
    public static final ResourceKey<LuckyEvent> SUMMON_GHAST = of("summon_ghast");
    public static final ResourceKey<LuckyEvent> SUMMON_WARDEN = of("summon_warden");
    public static final ResourceKey<LuckyEvent> SUMMON_WITHER = of("summon_wither");
    public static final ResourceKey<LuckyEvent> SUMMON_SLIME = of("summon_slime");
    public static final ResourceKey<LuckyEvent> SUMMON_WITCH = of("summon_witch");
    public static final ResourceKey<LuckyEvent> SUMMON_GIANT = of("summon_giant");
    public static final ResourceKey<LuckyEvent> SUMMON_CHARGED_CREEPER = of("summon_charged_creeper");
    public static final ResourceKey<LuckyEvent> SUMMON_ANGRY_BEE = of("summon_angry_bee");
    public static final ResourceKey<LuckyEvent> SUMMON_VEX_SWARM = of("summon_vex_swarm");

    public static final ResourceKey<LuckyEvent> SUMMON_ONE_TNT = of("summon_one_tnt");
    public static final ResourceKey<LuckyEvent> SUMMON_ONE_WIND_CHARGE = of("summon_one_wind_charge");

    // Loots
    public static final ResourceKey<LuckyEvent> LOOT_LUCKY_SWORD = of("loot_lucky_sword");
    public static final ResourceKey<LuckyEvent> LOOT_LUCKY_BOW = of("loot_lucky_bow");
    public static final ResourceKey<LuckyEvent> LOOT_LUCKY_ARMOR = of("loot_lucky_armor");
    public static final ResourceKey<LuckyEvent> LOOT_LUCKY_TOOL = of("loot_lucky_tool");
    public static final ResourceKey<LuckyEvent> LOOT_LUCKY_POTION = of("loot_lucky_potion");
    public static final ResourceKey<LuckyEvent> LOOT_VALUABLES = of("loot_valuables");
    public static final ResourceKey<LuckyEvent> LOOT_ROTTEN_FLESH = of("loot_rotten_flesh");
    public static final ResourceKey<LuckyEvent> LOOT_ALL_DYES = of("loot_all_dyes");
    public static final ResourceKey<LuckyEvent> LOOT_END_GAME_ITEM = of("loot_end_game_item");
    public static final ResourceKey<LuckyEvent> LOOT_ELYTRA = of("loot_elytra");
    public static final ResourceKey<LuckyEvent> LOOT_BUCKETS = of("loot_buckets");
    public static final ResourceKey<LuckyEvent> LOOT_FISH_BUCKET = of("loot_fish_bucket");
    public static final ResourceKey<LuckyEvent> LOOT_EGGS = of("loot_eggs");
    public static final ResourceKey<LuckyEvent> LOOT_POTATOES = of("loot_potatoes");
    public static final ResourceKey<LuckyEvent> LOOT_PUMPKINS = of("loot_pumpkins");

    private static ResourceKey<LuckyEvent> of(String path) {
        return ResourceKey.create(ULBRegistryKeys.LUCKY_EVENT, UltimateLuckyBlock.id(path));
    }
}
