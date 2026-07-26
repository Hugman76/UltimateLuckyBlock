package fr.hugman.ultimate_lucky_block.api.datagen.provider;

import fr.hugman.ultimate_lucky_block.api.lucky_event.LuckyEvent;
import fr.hugman.ultimate_lucky_block.api.registry.ULBRegistryKeys;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import java.util.concurrent.CompletableFuture;

import static fr.hugman.ultimate_lucky_block.api.lucky_event.LuckyEventTags.*;
import static fr.hugman.ultimate_lucky_block.api.lucky_event.LuckyEvents.*;

/**
 * @author Hugman
 * @since 1.0.0
 */
public class ULBEventTagProvider extends FabricTagsProvider<LuckyEvent> {
    public ULBEventTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
        super(output, ULBRegistryKeys.LUCKY_EVENT, completableFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {
        builder(VERY_UNLUCKY).add(
                        SUMMON_WITHER,
                        SUMMON_WARDEN,
                        SUMMON_CHARGED_CREEPER,
                        SUMMON_VEX_SWARM
                )
                .addOptional(SUMMON_BOB);

        builder(UNLUCKY).add(
                SUMMON_CREEPER,
                SUMMON_GHAST,
                SUMMON_WITCH,
                SUMMON_SLIME,
                SUMMON_ANGRY_WOLF,
                SUMMON_ANGRY_BEE,
                SET_BEDROCK_WORLD_PILLAR,
                SET_CAGE_TRAP,
                SET_PIT_TRAP,
                SET_TRAPPED_CHEST,
                SET_SCULK_TRAP,
                LOOT_ROTTEN_FLESH
        )
                .addOptional(SUMMON_ONE_WIND_CHARGE)
                .addOptional(SUMMON_ONE_TNT);

        builder(NORMAL).add(
                SUMMON_RAINBOW_SHEEP,
                SET_BEDROCK,
                SET_WOOL_PILLAR,
                SET_CONCRETE_POWDER_TOWER,
                SET_CREAKING_HEART,
                SUMMON_SULFUR_CUBE,
                SUMMON_SHEEP_FLOOD,
                SUMMON_FIREWORK_SHOW,
                SUMMON_GIANT,
                LOOT_ALL_DYES,
                LOOT_BUCKETS,
                LOOT_FISH_BUCKET,
                LOOT_EGGS,
                LOOT_PUMPKINS,
                LOOT_POTATOES
        );

        builder(LUCKY).add(
                SUMMON_HAPPY_GHAST,
                SUMMON_TAMED_WOLF,
                SUMMON_TAMED_CAT,
                LOOT_LUCKY_SWORD,
                LOOT_LUCKY_BOW,
                LOOT_LUCKY_ARMOR,
                LOOT_LUCKY_TOOL,
                LOOT_LUCKY_POTION,
                LOOT_VALUABLES,
                SET_LOOT_CHEST,
                SET_RANDOM_LUCKY_BLOCK
        );

        builder(VERY_LUCKY).add(
                LOOT_END_GAME_ITEM,
                SET_ORE_BLOCK,
                SET_ENCHANTING_SETUP,
                LOOT_ELYTRA
        );
    }
}