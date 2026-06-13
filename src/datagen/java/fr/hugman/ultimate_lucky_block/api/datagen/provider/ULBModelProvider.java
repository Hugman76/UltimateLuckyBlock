package fr.hugman.ultimate_lucky_block.api.datagen.provider;

import fr.hugman.ultimate_lucky_block.api.block.ULBBlocks;
import fr.hugman.ultimate_lucky_block.impl.UltimateLuckyBlock;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.data.*;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.world.level.block.Block;

/**
 * @author Hugman
 * @since 1.0.0
 */
public class ULBModelProvider extends FabricModelProvider {
    public ULBModelProvider(FabricPackOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators gen) {
        registerLuckyBlock(gen, ULBBlocks.LUCKY_BLOCK, "yellow");

        registerLuckyBlock(gen, ULBBlocks.SUPER_LUCKY_BLOCK, "green");
        registerLuckyBlock(gen, ULBBlocks.VERY_LUCKY_BLOCK, "diamond");
        registerLuckyBlock(gen, ULBBlocks.UNLUCKY_BLOCK, "red");
        registerLuckyBlock(gen, ULBBlocks.VERY_UNLUCKY_BLOCK, "purple");

        registerLuckyBlock(gen, ULBBlocks.DOUBLE_LUCKY_BLOCK, "double_yellow", "yellow");
        registerLuckyBlock(gen, ULBBlocks.TRIPLE_LUCKY_BLOCK, "triple_yellow", "yellow");
    }

    private void registerLuckyBlock(BlockModelGenerators gen, Block block, String suffix) {
        registerLuckyBlock(gen, block, suffix, suffix);
    }

    private void registerLuckyBlock(BlockModelGenerators gen, Block block, String sideSuffix, String endSuffix) {
        TextureMapping textureMap = TextureMapping.column(
                new Material(UltimateLuckyBlock.id("block/lucky_block/side_" + sideSuffix)),
                new Material(UltimateLuckyBlock.id("block/lucky_block/end_" + endSuffix))
        );
        var model = ModelTemplates.CUBE_COLUMN.create(block, textureMap, gen.modelOutput);
        BlockModelGenerators.plainVariant(model);
        gen.itemModelOutput.accept(block.asItem(), ItemModelUtils.plainModel(model));
    }

    @Override
    public void generateItemModels(ItemModelGenerators gen) {
    }
}

