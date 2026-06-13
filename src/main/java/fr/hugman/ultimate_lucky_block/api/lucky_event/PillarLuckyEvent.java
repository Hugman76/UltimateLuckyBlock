package fr.hugman.ultimate_lucky_block.api.lucky_event;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.heightproviders.ConstantHeight;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import org.jetbrains.annotations.Nullable;

/**
 * Lucky event that sets a pillar of blocks in the world (from bottom to top of the world).
 *
 * @author Hugman
 * @since 1.0.0
 */
public record PillarLuckyEvent(
        BlockStateProvider stateProvider,
        HeightProvider top,
        HeightProvider bottom
) implements LuckyEvent {
    public static final ConstantHeight DEFAULT_TOP = ConstantHeight.of(VerticalAnchor.TOP);
    public static final ConstantHeight DEFAULT_BOTTOM = ConstantHeight.of(VerticalAnchor.absolute(0));

    public static final MapCodec<PillarLuckyEvent> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BlockStateProvider.CODEC.fieldOf("state").forGetter(PillarLuckyEvent::stateProvider),
            HeightProvider.CODEC.optionalFieldOf("top", DEFAULT_TOP).forGetter(PillarLuckyEvent::top),
            HeightProvider.CODEC.optionalFieldOf("bottom", DEFAULT_BOTTOM).forGetter(PillarLuckyEvent::bottom)
    ).apply(instance, PillarLuckyEvent::new));

    public PillarLuckyEvent(BlockStateProvider provider, boolean top, boolean bottom) {
        this(provider,
                top ? ConstantHeight.of(VerticalAnchor.TOP) : ConstantHeight.of(VerticalAnchor.absolute(0)),
                bottom ? ConstantHeight.of(VerticalAnchor.BOTTOM) : ConstantHeight.of(VerticalAnchor.absolute(0))
        );
    }

    public PillarLuckyEvent(BlockState state, boolean top, boolean bottom) {
        this(BlockStateProvider.simple(state), top, bottom);
    }

    public PillarLuckyEvent(Block block, boolean top, boolean bottom) {
        this(BlockStateProvider.simple(block), top, bottom);
    }

    public PillarLuckyEvent(BlockStateProvider provider) {
        this(provider, DEFAULT_TOP, DEFAULT_BOTTOM);
    }

    public PillarLuckyEvent(BlockState state) {
        this(BlockStateProvider.simple(state));
    }

    public PillarLuckyEvent(Block block) {
        this(BlockStateProvider.simple(block));
    }

    public void trigger(ServerLevel level, @Nullable Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity) {
        level.setBlock(pos, stateProvider.getState(level, level.getRandom(), pos), Block.UPDATE_ALL);
        WorldGenerationContext heightContext = new WorldGenerationContext(level.getChunkSource().getGenerator(), level);
        var topY = getYPatched(top, pos.getY(), level.getRandom(), heightContext);
        var bottomY = getYPatched(bottom, pos.getY(), level.getRandom(), heightContext);
        for (int y = bottomY; y < topY; y++) {
            level.setBlock(new BlockPos(pos.getX(), y, pos.getZ()), stateProvider.getState(level, level.getRandom(), pos), Block.UPDATE_ALL);
        }
    }

    @Override
    public LuckyEventType<?> getType() {
        return LuckyEventTypes.WORLD_PILLAR;
    }

    private static int getYPatched(HeightProvider provider, int baseY, RandomSource random, WorldGenerationContext heightContext) {
        //IDK how to make it relative to the baseY, so we just add it
        var value = provider.sample(random, heightContext);
        if(value == heightContext.getGenDepth() || value == heightContext.getMinGenY()) {
            return value;
        }
        return Math.min(heightContext.getGenDepth(), Math.max(heightContext.getMinGenY(), value + baseY));
    }
}
