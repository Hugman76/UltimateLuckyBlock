package fr.hugman.ultimate_lucky_block.api.lucky_event.selector;

import fr.hugman.ultimate_lucky_block.api.lucky_event.LuckyEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Helper class for events that purely trigger other events.
 *
 * @author Hugman
 * @since 1.0.0
 */
public interface SelectorLuckyEvent extends LuckyEvent {
    List<Holder<LuckyEvent>> get(RandomSource random, float luck);

    default void trigger(ServerLevel world, @Nullable Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity) {
        float luck = player == null ? 0.0F : player.getLuck();
        for (Holder<LuckyEvent> event : get(world.getRandom(), luck)) {
            event.value().trigger(world, player, pos, state, blockEntity);
        }
    }
}
