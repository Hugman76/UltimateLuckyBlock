package fr.hugman.ultimate_lucky_block.api.lucky_event;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import org.jetbrains.annotations.Nullable;

/**
 * Lucky event that sets a block in the world.
 *
 * @author Hugman
 * @since 1.0.0
 */
public record SetBlockLuckyEvent(
        BlockStateProvider stateProvider,
        Vec3i offset
) implements LuckyEvent {
    public static final Vec3i DEFAULT_OFFSET = Vec3i.ZERO;

    public static final MapCodec<SetBlockLuckyEvent> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BlockStateProvider.CODEC.fieldOf("state_provider").forGetter(SetBlockLuckyEvent::stateProvider),
            Vec3i.CODEC.optionalFieldOf("offset", DEFAULT_OFFSET).forGetter(SetBlockLuckyEvent::offset)
    ).apply(instance, SetBlockLuckyEvent::new));

    public SetBlockLuckyEvent(BlockStateProvider provider) {
        this(provider, DEFAULT_OFFSET);
    }

    public SetBlockLuckyEvent(BlockState state) {
        this(BlockStateProvider.simple(state));
    }

    public SetBlockLuckyEvent(Block block) {
        this(BlockStateProvider.simple(block));
    }

    public SetBlockLuckyEvent(Block... blocks) {
        this(getListProvider(blocks), DEFAULT_OFFSET);
    }

    public void trigger(ServerLevel level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity) {
        level.setBlock(pos.offset(offset), stateProvider.getState(level, level.getRandom(), pos), Block.UPDATE_ALL);
    }

    @Override
    public LuckyEventType<?> getType() {
        return LuckyEventTypes.SET_BLOCK;
    }

    private static WeightedStateProvider getListProvider(Block... blocks) {
        var pool = new WeightedList.Builder<BlockState>();
        for (Block block : blocks) {
            pool.add(block.defaultBlockState());
        }
        return new WeightedStateProvider(pool);
    }
}
