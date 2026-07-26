package fr.hugman.ultimate_lucky_block.api.lucky_event;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

/**
 * What the positions of an event are measured from.
 *
 * <p>Most events read nicely from the lucky block itself, but a trap only works if it is built around whoever set it
 * off, which is rarely the exact block they were breaking.
 *
 * @author Hugman
 * @since 1.0.0
 */
public enum EventAnchor implements StringRepresentable {
    /** The block that triggered the event. */
    BLOCK("block"),
    /** The player that triggered the event, falling back to the block when there is none, like on a redstone trigger. */
    PLAYER("player");

    public static final EventAnchor DEFAULT = BLOCK;
    public static final Codec<EventAnchor> CODEC = StringRepresentable.fromEnum(EventAnchor::values);

    private final String name;

    EventAnchor(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }

    public BlockPos resolve(BlockPos pos, @Nullable Player player) {
        return this == PLAYER && player != null ? player.blockPosition() : pos;
    }
}
