package fr.hugman.ultimate_lucky_block.api.block;

import fr.hugman.ultimate_lucky_block.impl.UltimateLuckyBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;

/**
 * @author Hugman
 * @since 1.0.0
 */
public class ULBBlockKeys {
    public static final ResourceKey<Block> LUCKY_BLOCK = of("lucky_block");

    public static final ResourceKey<Block> SUPER_LUCKY_BLOCK = of("super_lucky_block");
    public static final ResourceKey<Block> VERY_LUCKY_BLOCK = of("very_lucky_block");
    public static final ResourceKey<Block> UNLUCKY_BLOCK = of("unlucky_block");
    public static final ResourceKey<Block> VERY_UNLUCKY_BLOCK = of("very_unlucky_block");

    public static final ResourceKey<Block> DOUBLE_LUCKY_BLOCK = of("double_lucky_block");
    public static final ResourceKey<Block> TRIPLE_LUCKY_BLOCK = of("triple_lucky_block");

    private static ResourceKey<Block> of(String path) {
        return ResourceKey.create(Registries.BLOCK, UltimateLuckyBlock.id(path));
    }
}
