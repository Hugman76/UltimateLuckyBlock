package fr.hugman.ultimate_lucky_block.api.item;

import eu.pb4.polymer.core.api.item.PolymerCreativeModeTabUtils;
import fr.hugman.ultimate_lucky_block.api.block.ULBBlocks;
import fr.hugman.ultimate_lucky_block.impl.UltimateLuckyBlock;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import static fr.hugman.ultimate_lucky_block.api.block.ULBBlocks.*;

public class ULBCreativeTabs {
    public static final CreativeModeTab LUCKY_BLOCKS = of("lucky_blocks", FabricCreativeModeTab.builder()
            .title(Component.translatable("item_group.ultimate_lucky_block.lucky_blocks"))
            .icon(() -> new ItemStack(ULBBlocks.LUCKY_BLOCK))
            .displayItems((context, entries) -> {
                entries.accept(LUCKY_BLOCK);
                entries.accept(SUPER_LUCKY_BLOCK);
                entries.accept(VERY_LUCKY_BLOCK);
                entries.accept(UNLUCKY_BLOCK);
                entries.accept(VERY_UNLUCKY_BLOCK);
                entries.accept(DOUBLE_LUCKY_BLOCK);
                entries.accept(TRIPLE_LUCKY_BLOCK);
            })
            .build());


    private static CreativeModeTab of(String path, CreativeModeTab itemGroup) {
        PolymerCreativeModeTabUtils.registerPolymerCreativeModeTab(UltimateLuckyBlock.id(path), itemGroup);
        return itemGroup;
    }
}
