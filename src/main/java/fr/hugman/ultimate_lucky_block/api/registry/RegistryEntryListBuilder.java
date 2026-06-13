package fr.hugman.ultimate_lucky_block.api.registry;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;

/**
 * @author Hugman
 * @since 1.0.0
 */
public abstract class RegistryEntryListBuilder<B extends RegistryEntryListBuilder<B, R>, R> {
    protected final ImmutableList.Builder<Holder<R>> entries = ImmutableList.builder();

    protected abstract B getThis();

    public B add(R event) {
        return this.add(Holder.direct(event));
    }

    public B add(R... events) {
        for (R event : events) {
            this.add(event);
        }
        return getThis();
    }

    public B add(Holder<R> entry) {
        this.entries.add(entry);
        return getThis();
    }

    public B add(Holder<R>... events) {
        for (Holder<R> event : events) {
            this.add(event);
        }
        return getThis();
    }

    public B add(Iterable<Holder<R>> events) {
        for (Holder<R> event : events) {
            this.add(event);
        }
        return getThis();
    }

    public B add(HolderSet<R> entry) {
        this.entries.addAll(entry);
        return getThis();
    }
}
