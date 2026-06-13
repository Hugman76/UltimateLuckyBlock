package fr.hugman.ultimate_lucky_block.api.lucky_event;

import com.mojang.serialization.Codec;
import fr.hugman.ultimate_lucky_block.api.registry.ULBRegistries;
import fr.hugman.ultimate_lucky_block.api.registry.ULBRegistryKeys;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/**
 * Lucky event, triggers something in the world at a fixed position by a player.
 *
 * <p>Usually triggered by a player breaking a lucky block, but can be used in other contexts as well.
 *
 * @author Hugman
 * @since 1.0.0
 */
public interface LuckyEvent {
    Codec<LuckyEvent> TYPE_CODEC = ULBRegistries.LUCKY_EVENT_TYPE.byNameCodec().dispatch(LuckyEvent::getType, LuckyEventType::codec);

    Codec<Holder<LuckyEvent>> ENTRY_CODEC = RegistryFileCodec.create(ULBRegistryKeys.LUCKY_EVENT, TYPE_CODEC);
    Codec<HolderSet<LuckyEvent>> LIST_CODEC = RegistryCodecs.homogeneousList(ULBRegistryKeys.LUCKY_EVENT, TYPE_CODEC);

    LuckyEventType<?> getType();

    void trigger(ServerLevel world, @Nullable Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity);
}
