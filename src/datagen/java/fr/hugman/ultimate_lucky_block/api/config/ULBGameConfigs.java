package fr.hugman.ultimate_lucky_block.api.config;

import fr.hugman.ultimate_lucky_block.impl.UltimateLuckyBlock;
import net.minecraft.resources.ResourceKey;
import xyz.nucleoid.plasmid.api.game.config.GameConfig;
import xyz.nucleoid.plasmid.api.registry.PlasmidRegistryKeys;

/**
 * @author Hugman
 * @since 1.0.0
 */
public class ULBGameConfigs {
    public static ResourceKey<GameConfig<?>> of(String path) {
        return ResourceKey.create(PlasmidRegistryKeys.GAME_CONFIG, UltimateLuckyBlock.id(path));
    }
}
