package fr.hugman.ultimate_lucky_block.api.datagen.provider;

import fr.hugman.ultimate_lucky_block.api.block.ULBBlocks;
import fr.hugman.ultimate_lucky_block.impl.UltimateLuckyBlock;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

/**
 * @author Hugman
 * @since 1.0.0
 */
public class ULBLangProvider extends FabricLanguageProvider {
    public ULBLangProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(output, registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.Provider registryLookup, TranslationBuilder builder) {
        builder.add("item_group.ultimate_lucky_block.lucky_blocks", "Lucky Blocks");

        builder.add(ULBBlocks.LUCKY_BLOCK, "Lucky Block");

        builder.add(ULBBlocks.SUPER_LUCKY_BLOCK, "Super Lucky Block");
        builder.add(ULBBlocks.VERY_LUCKY_BLOCK, "Very Lucky Block");
        builder.add(ULBBlocks.UNLUCKY_BLOCK, "Unlucky Block");
        builder.add(ULBBlocks.VERY_UNLUCKY_BLOCK, "Very Unlucky Block");

        builder.add(ULBBlocks.DOUBLE_LUCKY_BLOCK, "Double Lucky Block");
        builder.add(ULBBlocks.TRIPLE_LUCKY_BLOCK, "Triple Lucky Block");
    }

    @Override
    protected Path getLangFilePath(String code) {
        return this.packOutput
                .createPathProvider(PackOutput.Target.DATA_PACK, "lang")
                .json(UltimateLuckyBlock.id(code));
    }
}
