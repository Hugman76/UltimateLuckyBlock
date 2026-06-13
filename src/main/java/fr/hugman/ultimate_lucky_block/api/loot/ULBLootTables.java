package fr.hugman.ultimate_lucky_block.api.loot;

import fr.hugman.ultimate_lucky_block.impl.UltimateLuckyBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;

/**
 * @author Hugman
 * @since 1.0.0
 */
public class ULBLootTables {
    public static final ResourceKey<LootTable> LUCKY_SWORD = of("lucky_sword");
    public static final ResourceKey<LootTable> LUCKY_BOW = of("lucky_bow");
    public static final ResourceKey<LootTable> ALL_DYES = of("all_dyes");
    public static final ResourceKey<LootTable> END_GAME_ITEM = of("end_game_item");
    public static final ResourceKey<LootTable> ELYTRA = of("elytra");
    public static final ResourceKey<LootTable> BUCKETS = of("buckets");
    public static final ResourceKey<LootTable> FISH_BUCKET = of("fish_bucket");
    public static final ResourceKey<LootTable> ROTTEN_FLESH = of("rotten_flesh");
    public static final ResourceKey<LootTable> EGGS = of("eggs");
    public static final ResourceKey<LootTable> POTATOES = of("potatoes");
    public static final ResourceKey<LootTable> PUMPKINS = of("pumpkins");

    private static ResourceKey<LootTable> of(String path) {
        return ResourceKey.create(Registries.LOOT_TABLE, UltimateLuckyBlock.id(path));
    }
}
