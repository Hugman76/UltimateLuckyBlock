package fr.hugman.ultimate_lucky_block.api.item;

import fr.hugman.ultimate_lucky_block.impl.UltimateLuckyBlock;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Util;

/**
 * Translation keys of the custom-named items handed out by lucky events.
 *
 * <p>These items are vanilla items with a custom name, so they have no translation key of their own: they take one
 * from here instead of a hardcoded name, so that they can be translated like anything else.
 *
 * @author Hugman
 * @since 1.0.0
 */
public class ULBItemNames {
    public static final String LUCKY_SWORD = of("lucky_sword");
    public static final String LUCKY_BOW = of("lucky_bow");

    public static final String LUCKY_HELMET = of("lucky_helmet");
    public static final String LUCKY_CHESTPLATE = of("lucky_chestplate");
    public static final String LUCKY_LEGGINGS = of("lucky_leggings");
    public static final String LUCKY_BOOTS = of("lucky_boots");

    public static final String LUCKY_PICKAXE = of("lucky_pickaxe");
    public static final String LUCKY_SHOVEL = of("lucky_shovel");
    public static final String LUCKY_AXE = of("lucky_axe");

    public static final String LUCKY_POTION = of("lucky_potion");

    public static Component text(String key) {
        return Component.translatable(key);
    }

    private static String of(String path) {
        return Util.makeDescriptionId("item", UltimateLuckyBlock.id(path));
    }
}
