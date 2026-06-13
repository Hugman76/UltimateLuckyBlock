package fr.hugman.ultimate_lucky_block.api.block;

import eu.pb4.polymer.blocks.api.BlockModelType;
import eu.pb4.polymer.blocks.api.PolymerBlockModel;
import eu.pb4.polymer.blocks.api.PolymerBlockResourceUtils;
import eu.pb4.polymer.blocks.api.PolymerTexturedBlock;
import fr.hugman.ultimate_lucky_block.api.lucky_event.LuckyEvent;
import fr.hugman.ultimate_lucky_block.api.registry.ULBRegistryKeys;
import net.fabricmc.fabric.api.networking.v1.context.PacketContext;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.redstone.Orientation;
import org.jetbrains.annotations.Nullable;

/**
 * @author Hugman
 * @since 1.0.0
 */
public class LuckyBlock extends Block implements PolymerTexturedBlock, LuckyBlockInterface {
    private final BlockState model;
    private final ResourceKey<LuckyEvent> event;

    public LuckyBlock(Properties settings, ResourceKey<LuckyEvent> event, ResourceKey<Block> key) {
        super(settings);

        this.model = PolymerBlockResourceUtils.requestBlock(BlockModelType.FULL_BLOCK, PolymerBlockModel.of(key.identifier().withPrefix("block/")));
        this.event = event;
    }

    @Override
    public BlockState getPolymerBlockState(BlockState state, PacketContext context) {
        return model;
    }

    @Override
    public void onLuckyBlockTrigger(ServerLevel world, @Nullable Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity) {
        world.registryAccess()
                .lookupOrThrow(ULBRegistryKeys.LUCKY_EVENT)
                .getOrThrow(this.event).value()
                .trigger(world, player, pos, state, blockEntity);
        //TODO: add particles and sounds
    }

    @Override
    protected void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean notify) {
        if (oldState.getBlock() != state.getBlock() && world instanceof ServerLevel serverWorld) {
            this.update(state, serverWorld, pos);
        }
    }

    @Override
    protected void neighborChanged(BlockState state, Level world, BlockPos pos, Block sourceBlock, @Nullable Orientation wireOrientation, boolean notify) {
        if (world instanceof ServerLevel serverWorld) {
            this.update(state, serverWorld, pos);
        }
    }

    public void update(BlockState state, ServerLevel world, BlockPos pos) {
        if (world.hasNeighborSignal(pos)) {
            world.removeBlock(pos, false);
            this.onLuckyBlockTrigger(world, null, pos, state, world.getBlockEntity(pos));
        }
    }
}
