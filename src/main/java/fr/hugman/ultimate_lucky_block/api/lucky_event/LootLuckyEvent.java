package fr.hugman.ultimate_lucky_block.api.lucky_event;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

/**
 * Lucky event that drops the content of a loot table.
 *
 * @author Hugman
 * @since 1.0.0
 */
public record LootLuckyEvent(ResourceKey<LootTable> lootTable) implements LuckyEvent {
    public static final MapCodec<LootLuckyEvent> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ResourceKey.codec(Registries.LOOT_TABLE).fieldOf("loot_table").forGetter(LootLuckyEvent::lootTable)
    ).apply(instance, LootLuckyEvent::new));

    public void trigger(ServerLevel world, @Nullable Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity) {
        var heldStack = player != null ? player.getMainHandItem() : ItemStack.EMPTY;
        getLoots(world, pos, player, heldStack).forEach((stack) -> Block.popResource(world, pos, stack));
    }

    public List<ItemStack> getLoots(ServerLevel world, BlockPos pos, @Nullable Entity entity, ItemStack heldStack) {
        return world.getServer().reloadableRegistries().getLootTable(this.lootTable)
                .getRandomItems(new LootParams.Builder(world)
                        .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos))
                        .withParameter(LootContextParams.TOOL, heldStack)
                        .withParameter(LootContextParams.BLOCK_STATE, world.getBlockState(pos))
                        .withOptionalParameter(LootContextParams.BLOCK_ENTITY, world.getBlockEntity(pos))
                        .withOptionalParameter(LootContextParams.THIS_ENTITY, entity)
                        .create(LootContextParamSets.BLOCK));
    }

    @Override
    public LuckyEventType<?> getType() {
        return LuckyEventTypes.LOOT;
    }
}
