package fr.hugman.ultimate_lucky_block.api.lucky_event;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import org.jetbrains.annotations.Nullable;

/**
 * Lucky event that fills a box of blocks in the world.
 *
 * <p>The box is defined by two corners, both relative to the triggering position. The state provider is sampled once
 * per filled position, so a randomized provider gives a different block every time.
 *
 * <p>Use the {@link Shape} to only fill a part of the box: a cage is a box of {@link Shape#WALLS}, a cleared area is a
 * box of air, and a hollow structure with something inside is a box of {@link Shape#HOLLOW} followed by a
 * {@link SetBlockLuckyEvent}.
 *
 * @author Hugman
 * @since 1.0.0
 */
public record FillLuckyEvent(
        BlockStateProvider stateProvider,
        Vec3i from,
        Vec3i to,
        Shape shape,
        EventAnchor anchor
) implements LuckyEvent {
    public static final Shape DEFAULT_SHAPE = Shape.SOLID;

    public static final MapCodec<FillLuckyEvent> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BlockStateProvider.CODEC.fieldOf("state_provider").forGetter(FillLuckyEvent::stateProvider),
            Vec3i.CODEC.fieldOf("from").forGetter(FillLuckyEvent::from),
            Vec3i.CODEC.fieldOf("to").forGetter(FillLuckyEvent::to),
            Shape.CODEC.optionalFieldOf("shape", DEFAULT_SHAPE).forGetter(FillLuckyEvent::shape),
            EventAnchor.CODEC.optionalFieldOf("anchor", EventAnchor.DEFAULT).forGetter(FillLuckyEvent::anchor)
    ).apply(instance, FillLuckyEvent::new));

    public FillLuckyEvent(BlockStateProvider provider, Vec3i from, Vec3i to, Shape shape) {
        this(provider, from, to, shape, EventAnchor.DEFAULT);
    }

    public FillLuckyEvent(BlockStateProvider provider, Vec3i from, Vec3i to) {
        this(provider, from, to, DEFAULT_SHAPE);
    }

    public FillLuckyEvent(BlockState state, Vec3i from, Vec3i to, Shape shape) {
        this(BlockStateProvider.simple(state), from, to, shape);
    }

    public FillLuckyEvent(Block block, Vec3i from, Vec3i to, Shape shape, EventAnchor anchor) {
        this(BlockStateProvider.simple(block), from, to, shape, anchor);
    }

    public FillLuckyEvent(Block block, Vec3i from, Vec3i to, Shape shape) {
        this(BlockStateProvider.simple(block), from, to, shape);
    }

    public FillLuckyEvent(Block block, Vec3i from, Vec3i to) {
        this(block, from, to, DEFAULT_SHAPE);
    }

    public void trigger(ServerLevel level, @Nullable Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity) {
        var random = level.getRandom();
        var origin = this.anchor.resolve(pos, player);

        int minX = origin.getX() + Math.min(from.getX(), to.getX());
        int minY = origin.getY() + Math.min(from.getY(), to.getY());
        int minZ = origin.getZ() + Math.min(from.getZ(), to.getZ());
        int maxX = origin.getX() + Math.max(from.getX(), to.getX());
        int maxY = origin.getY() + Math.max(from.getY(), to.getY());
        int maxZ = origin.getZ() + Math.max(from.getZ(), to.getZ());

        for (int x = minX; x <= maxX; x++) {
            boolean edgeX = x == minX || x == maxX;
            for (int y = minY; y <= maxY; y++) {
                boolean edgeY = y == minY || y == maxY;
                for (int z = minZ; z <= maxZ; z++) {
                    boolean edgeZ = z == minZ || z == maxZ;
                    if (!shape.contains(edgeX, edgeY, edgeZ)) {
                        continue;
                    }
                    var filledPos = new BlockPos(x, y, z);
                    level.setBlock(filledPos, stateProvider.getState(level, random, filledPos), Block.UPDATE_ALL);
                }
            }
        }
    }

    @Override
    public LuckyEventType<?> getType() {
        return LuckyEventTypes.FILL;
    }

    /**
     * Which positions of the box are actually filled.
     */
    public enum Shape implements StringRepresentable {
        /** Every position of the box. */
        SOLID("solid"),
        /** The six outer faces of the box, leaving the inside untouched. */
        HOLLOW("hollow"),
        /** The four vertical faces of the box, leaving the top, the bottom and the inside untouched. */
        WALLS("walls"),
        /** The twelve edges of the box. */
        OUTLINE("outline");

        public static final Codec<Shape> CODEC = StringRepresentable.fromEnum(Shape::values);

        private final String name;

        Shape(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }

        /**
         * @param edgeX whether the position is on the lowest or highest X of the box
         * @param edgeY whether the position is on the lowest or highest Y of the box
         * @param edgeZ whether the position is on the lowest or highest Z of the box
         * @return whether a position of the box is part of this shape
         */
        public boolean contains(boolean edgeX, boolean edgeY, boolean edgeZ) {
            return switch (this) {
                case SOLID -> true;
                case HOLLOW -> edgeX || edgeY || edgeZ;
                case WALLS -> edgeX || edgeZ;
                case OUTLINE -> (edgeX && edgeY) || (edgeY && edgeZ) || (edgeX && edgeZ);
            };
        }
    }
}
