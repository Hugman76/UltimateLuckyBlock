package fr.hugman.ultimate_lucky_block.impl;

import com.google.common.reflect.Reflection;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import fr.hugman.ultimate_lucky_block.api.block.LuckyBlockInterface;
import fr.hugman.ultimate_lucky_block.api.block.ULBBlocks;
import fr.hugman.ultimate_lucky_block.api.itemgroup.ULBItemGroups;
import fr.hugman.ultimate_lucky_block.api.lucky_event.LuckyEventTypes;
import fr.hugman.ultimate_lucky_block.api.registry.ULBRegistries;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Hugman
 * @since 1.0.0
 */
public class UltimateLuckyBlock implements ModInitializer {
    public static final String MOD_ID = "ultimate_lucky_block";
    public static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void onInitialize() {
        ULBRegistries.registerDynamics();
        Reflection.initialize(LuckyEventTypes.class);

        Reflection.initialize(ULBBlocks.class);
        Reflection.initialize(ULBItemGroups.class);

        if (PolymerResourcePackUtils.addModAssets(MOD_ID)) {
            LOGGER.info("Successfully added mod assets for " + MOD_ID);
        } else {
            LOGGER.error("Failed to add mod assets for " + MOD_ID);
        }
        PlayerBlockBreakEvents.AFTER.register((world, playerEntity, blockPos, blockState, blockEntity) -> {
            if (blockState.getBlock() instanceof LuckyBlockInterface lucky && world instanceof ServerWorld serverWorld) {
                lucky.onLuckyBlockTrigger(serverWorld, playerEntity, blockPos, blockState, blockEntity);
            }
        });
    }

    public static Identifier id(String path) {
        return Identifier.of(MOD_ID, path);
    }
}
