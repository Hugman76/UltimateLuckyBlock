package fr.hugman.ultimate_lucky_block.api.datagen.provider;

import fr.hugman.ultimate_lucky_block.impl.UltimateLuckyBlock;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.renderer.texture.atlas.SpriteSource;
import net.minecraft.client.renderer.texture.atlas.SpriteSources;
import net.minecraft.client.renderer.texture.atlas.sources.PalettedPermutations;
import net.minecraft.data.AtlasIds;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author Hugman
 * @since 1.0.0
 */
public class ULBAtlasProvider implements DataProvider {
    private static final List<Identifier> LUCKY_BLOCK_TEXTURES = List.of(
            UltimateLuckyBlock.id("block/lucky_block/side"),
            UltimateLuckyBlock.id("block/lucky_block/side_double"),
            UltimateLuckyBlock.id("block/lucky_block/side_triple"),
            UltimateLuckyBlock.id("block/lucky_block/end")
    );
    private static final Identifier LUCKY_BLOCK_PALETTE_KEY = UltimateLuckyBlock.id("color_palettes/lucky_block");
    private static final Map<String, Identifier> LUCKY_BLOCK_PERMUTATIONS = Map.of(
            "yellow", luckyBlockPalette("yellow"),
            "red", luckyBlockPalette("red"),
            "green", luckyBlockPalette("green"),
            "diamond", luckyBlockPalette("diamond"),
            "purple", luckyBlockPalette("purple")
    );

    private final PackOutput.PathProvider pathProvider;

    public ULBAtlasProvider(FabricPackOutput output) {
        this.pathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "atlases");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        List<SpriteSource> sources = List.of(new PalettedPermutations(
                LUCKY_BLOCK_TEXTURES,
                LUCKY_BLOCK_PALETTE_KEY,
                LUCKY_BLOCK_PERMUTATIONS
        ));
        return DataProvider.saveStable(output, SpriteSources.FILE_CODEC, sources, this.pathProvider.json(AtlasIds.BLOCKS));
    }

    @Override
    public String getName() {
        return "Atlases";
    }

    private static Identifier luckyBlockPalette(String color) {
        return UltimateLuckyBlock.id("color_palettes/lucky_block/" + color);
    }
}
