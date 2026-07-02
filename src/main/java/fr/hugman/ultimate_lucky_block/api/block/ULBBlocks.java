package fr.hugman.ultimate_lucky_block.api.block;

import eu.pb4.polymer.core.api.block.PolymerBlock;
import eu.pb4.polymer.core.api.item.PolymerBlockItem;
import fr.hugman.ultimate_lucky_block.api.lucky_event.LuckyEvent;
import fr.hugman.ultimate_lucky_block.api.lucky_event.LuckyPoolEvents;
import java.util.function.Function;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

/**
 * @author Hugman
 * @since 1.0.0
 */
public class ULBBlocks {
    public static final LuckyBlock LUCKY_BLOCK = luckyBlock(ULBBlockKeys.LUCKY_BLOCK, LuckyPoolEvents.NORMAL, BlockBehaviour.Properties.ofFullCopy(Blocks.WOOL.yellow()));

    public static final LuckyBlock SUPER_LUCKY_BLOCK = luckyBlock(ULBBlockKeys.SUPER_LUCKY_BLOCK, LuckyPoolEvents.LUCKY, BlockBehaviour.Properties.ofFullCopy(Blocks.WOOL.lime()));
    public static final LuckyBlock VERY_LUCKY_BLOCK = luckyBlock(ULBBlockKeys.VERY_LUCKY_BLOCK, LuckyPoolEvents.VERY_LUCKY, BlockBehaviour.Properties.ofFullCopy(Blocks.WOOL.lightBlue()));
    public static final LuckyBlock UNLUCKY_BLOCK = luckyBlock(ULBBlockKeys.UNLUCKY_BLOCK, LuckyPoolEvents.UNLUCKY, BlockBehaviour.Properties.ofFullCopy(Blocks.WOOL.red()));
    public static final LuckyBlock VERY_UNLUCKY_BLOCK = luckyBlock(ULBBlockKeys.VERY_UNLUCKY_BLOCK, LuckyPoolEvents.VERY_UNLUCKY, BlockBehaviour.Properties.ofFullCopy(Blocks.WOOL.purple()));

    public static final LuckyBlock DOUBLE_LUCKY_BLOCK = luckyBlock(ULBBlockKeys.DOUBLE_LUCKY_BLOCK, LuckyPoolEvents.DOUBLE, BlockBehaviour.Properties.ofFullCopy(LUCKY_BLOCK));
    public static final LuckyBlock TRIPLE_LUCKY_BLOCK = luckyBlock(ULBBlockKeys.TRIPLE_LUCKY_BLOCK, LuckyPoolEvents.TRIPLE, BlockBehaviour.Properties.ofFullCopy(LUCKY_BLOCK));

    private static <B extends Block & PolymerBlock> B noItem(ResourceKey<Block> key, Function<BlockBehaviour.Properties, B> factory, BlockBehaviour.Properties blockSettings) {
        B block = factory.apply(blockSettings.setId(key));
        return Registry.register(BuiltInRegistries.BLOCK, key, block);
    }

    private static <B extends Block & PolymerBlock> B of(ResourceKey<Block> key, Function<BlockBehaviour.Properties, B> factory, BlockBehaviour.Properties blockSettings, Item.Properties itemSettings) {
        B block = noItem(key, factory, blockSettings);
        var itemRegistryKey = ResourceKey.create(Registries.ITEM, key.identifier());
        Registry.register(BuiltInRegistries.ITEM, itemRegistryKey, new PolymerBlockItem(block, itemSettings.setId(itemRegistryKey).useBlockDescriptionPrefix()));
        return block;
    }

    private static LuckyBlock luckyBlock(ResourceKey<Block> key, ResourceKey<LuckyEvent> event, BlockBehaviour.Properties settings) {
        return of(key, s -> new LuckyBlock(s, event, key), settings, new Item.Properties().component(DataComponents.CONTAINER, ItemContainerContents.EMPTY));
    }
}
