package fr.hugman.ultimate_lucky_block.api.lucky_event;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.component.TypedEntityData;
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
        Vec3i offset,
        EventAnchor anchor,
        CompoundTag blockEntityData
) implements LuckyEvent {
    public static final Vec3i DEFAULT_OFFSET = Vec3i.ZERO;
    public static final CompoundTag DEFAULT_BLOCK_ENTITY_DATA = new CompoundTag();

    public static final MapCodec<SetBlockLuckyEvent> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BlockStateProvider.CODEC.fieldOf("state_provider").forGetter(SetBlockLuckyEvent::stateProvider),
            Vec3i.CODEC.optionalFieldOf("offset", DEFAULT_OFFSET).forGetter(SetBlockLuckyEvent::offset),
            EventAnchor.CODEC.optionalFieldOf("anchor", EventAnchor.DEFAULT).forGetter(SetBlockLuckyEvent::anchor),
            CompoundTag.CODEC.optionalFieldOf("block_entity_data", DEFAULT_BLOCK_ENTITY_DATA).forGetter(SetBlockLuckyEvent::blockEntityData)
    ).apply(instance, SetBlockLuckyEvent::new));

    public SetBlockLuckyEvent(BlockStateProvider provider, Vec3i offset) {
        this(provider, offset, EventAnchor.DEFAULT, DEFAULT_BLOCK_ENTITY_DATA);
    }

    public SetBlockLuckyEvent(BlockStateProvider provider) {
        this(provider, DEFAULT_OFFSET);
    }

    public SetBlockLuckyEvent(Block block, CompoundTag blockEntityData) {
        this(BlockStateProvider.simple(block), DEFAULT_OFFSET, EventAnchor.DEFAULT, blockEntityData);
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

    public SetBlockLuckyEvent(Vec3i offset, Block... blocks) {
        this(getListProvider(blocks), offset);
    }

    public void trigger(ServerLevel level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity) {
        var target = this.anchor.resolve(pos, player).offset(this.offset);
        level.setBlock(target, stateProvider.getState(level, level.getRandom(), target), Block.UPDATE_ALL);

        if (!this.blockEntityData.isEmpty()) {
            var placedBlockEntity = level.getBlockEntity(target);
            if (placedBlockEntity != null) {
                TypedEntityData.of(placedBlockEntity.getType(), this.blockEntityData).loadInto(placedBlockEntity, level.registryAccess());
            }
        }
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
