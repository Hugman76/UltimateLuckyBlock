package fr.hugman.ultimate_lucky_block.api.datagen.provider;

import fr.hugman.ultimate_lucky_block.api.block.ULBBlocks;
import fr.hugman.ultimate_lucky_block.impl.UltimateLuckyBlock;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.block.Block;
import net.minecraft.client.data.*;

/**
 * @author Hugman
 * @since 1.0.0
 */
public class ULBModelProvider extends FabricModelProvider {
    public ULBModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator gen) {
        registerLuckyBlock(gen, ULBBlocks.LUCKY_BLOCK, "yellow");

        registerLuckyBlock(gen, ULBBlocks.SUPER_LUCKY_BLOCK, "green");
        registerLuckyBlock(gen, ULBBlocks.VERY_LUCKY_BLOCK, "diamond");
        registerLuckyBlock(gen, ULBBlocks.UNLUCKY_BLOCK, "red");
        registerLuckyBlock(gen, ULBBlocks.VERY_UNLUCKY_BLOCK, "purple");

        registerLuckyBlock(gen, ULBBlocks.DOUBLE_LUCKY_BLOCK, "double_yellow", "yellow");
        registerLuckyBlock(gen, ULBBlocks.TRIPLE_LUCKY_BLOCK, "triple_yellow", "yellow");
    }

    private void registerLuckyBlock(BlockStateModelGenerator gen, Block block, String suffix) {
        registerLuckyBlock(gen, block, suffix, suffix);
    }

    private void registerLuckyBlock(BlockStateModelGenerator gen, Block block, String sideSuffix, String endSuffix) {
        TextureMap textureMap = TextureMap.sideEnd(
                UltimateLuckyBlock.id("block/lucky_block/side_" + sideSuffix),
                UltimateLuckyBlock.id("block/lucky_block/end_" + endSuffix)
        );
        var model = Models.CUBE_COLUMN.upload(block, textureMap, gen.modelCollector);
        BlockStateModelGenerator.createWeightedVariant(model);
        gen.itemModelOutput.accept(block.asItem(), ItemModels.basic(model));
    }

    @Override
    public void generateItemModels(ItemModelGenerator gen) {
    }
}

