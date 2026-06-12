package dev.enco.greatcombat.core.restrictions;

import dev.enco.greatcombat.api.models.IWrappedItem;
import dev.enco.greatcombat.api.models.MetaChecker;
import java.util.List;
import java.util.Objects;
import lombok.Generated;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

public enum DefaultCheckers implements MetaChecker
{
    SIMILAR(false){

        @Override
        public boolean matches(@NotNull IWrappedItem f, @NotNull IWrappedItem s) {
            return f.itemStack().isSimilar(s.itemStack());
        }
    }
    ,
    META(true){

        @Override
        public boolean matches(@NotNull IWrappedItem f, @NotNull IWrappedItem s) {
            return f.itemMeta().equals((Object)s.itemMeta());
        }
    }
    ,
    MATERIAL(false){

        @Override
        public boolean matches(@NotNull IWrappedItem f, @NotNull IWrappedItem s) {
            return f.itemStack().getType().equals((Object)s.itemStack().getType());
        }
    }
    ,
    ITEM_FLAGS(false){

        @Override
        public boolean matches(@NotNull IWrappedItem f, @NotNull IWrappedItem s) {
            return f.itemStack().getItemFlags().equals(s.itemStack().getItemFlags());
        }
    }
    ,
    DISPLAY_NAME(true){

        @Override
        public boolean matches(@NotNull IWrappedItem f, @NotNull IWrappedItem s) {
            return Objects.equals(f.itemMeta().getDisplayName(), s.itemMeta().getDisplayName());
        }
    }
    ,
    LORE(true){

        @Override
        public boolean matches(@NotNull IWrappedItem f, @NotNull IWrappedItem s) {
            List l1 = f.itemMeta().getLore();
            List l2 = s.itemMeta().getLore();
            return Objects.equals(l1, l2);
        }
    }
    ,
    ENCHANTMENTS(false){

        @Override
        public boolean matches(@NotNull IWrappedItem f, @NotNull IWrappedItem s) {
            return f.itemStack().getEnchantments().equals(s.itemStack().getEnchantments());
        }
    }
    ,
    ATTRIBUTES(true){

        @Override
        public boolean matches(@NotNull IWrappedItem f, @NotNull IWrappedItem s) {
            return Objects.equals(f.itemMeta().getAttributeModifiers(), s.itemMeta().getAttributeModifiers());
        }
    }
    ,
    PDC(true){

        @Override
        public boolean matches(@NotNull IWrappedItem f, @NotNull IWrappedItem s) {
            return f.itemMeta().getPersistentDataContainer().equals((Object)s.itemMeta().getPersistentDataContainer());
        }
    }
    ,
    UNBREAKABLE(true){

        @Override
        public boolean matches(@NotNull IWrappedItem f, @NotNull IWrappedItem s) {
            return f.itemMeta().isUnbreakable() == s.itemMeta().isUnbreakable();
        }
    }
    ,
    POTION_EFFECTS(true){

        @Override
        public boolean matches(@NotNull IWrappedItem f, @NotNull IWrappedItem s) {
            PotionMeta fm;
            ItemMeta itemMeta;
            block3: {
                block2: {
                    itemMeta = f.itemMeta();
                    if (!(itemMeta instanceof PotionMeta)) break block2;
                    fm = (PotionMeta)itemMeta;
                    itemMeta = s.itemMeta();
                    if (itemMeta instanceof PotionMeta) break block3;
                }
                return false;
            }
            PotionMeta sm = (PotionMeta)itemMeta;
            return fm.getCustomEffects().equals(sm.getCustomEffects());
        }
    }
    ,
    POTION_BASE(true){

        @Override
        public boolean matches(@NotNull IWrappedItem f, @NotNull IWrappedItem s) {
            PotionMeta fm;
            ItemMeta itemMeta;
            block5: {
                block4: {
                    itemMeta = f.itemMeta();
                    if (!(itemMeta instanceof PotionMeta)) break block4;
                    fm = (PotionMeta)itemMeta;
                    itemMeta = s.itemMeta();
                    if (itemMeta instanceof PotionMeta) break block5;
                }
                return false;
            }
            PotionMeta sm = (PotionMeta)itemMeta;
            try {
                return Objects.equals(fm.getBasePotionType(), sm.getBasePotionType());
            }
            catch (NoSuchMethodError e) {
                return Objects.equals(fm.getBasePotionData(), sm.getBasePotionData());
            }
        }
    }
    ,
    COLOR(true){

        @Override
        public boolean matches(@NotNull IWrappedItem f, @NotNull IWrappedItem s) {
            LeatherArmorMeta fm;
            ItemMeta itemMeta;
            block3: {
                block2: {
                    itemMeta = f.itemMeta();
                    if (!(itemMeta instanceof LeatherArmorMeta)) break block2;
                    fm = (LeatherArmorMeta)itemMeta;
                    itemMeta = s.itemMeta();
                    if (itemMeta instanceof LeatherArmorMeta) break block3;
                }
                return false;
            }
            LeatherArmorMeta sm = (LeatherArmorMeta)itemMeta;
            return fm.getColor().equals((Object)sm.getColor());
        }
    }
    ,
    CUSTOM_MODEL_DATA(true){

        @Override
        public boolean matches(@NotNull IWrappedItem f, @NotNull IWrappedItem s) {
            ItemMeta fm = f.itemMeta();
            ItemMeta sm = s.itemMeta();
            return fm.hasCustomModelData() == sm.hasCustomModelData() && (!fm.hasCustomModelData() || fm.getCustomModelData() == sm.getCustomModelData());
        }
    }
    ,
    SKULL(true){

        @Override
        public boolean matches(@NotNull IWrappedItem f, @NotNull IWrappedItem s) {
            SkullMeta fm;
            ItemMeta itemMeta;
            block3: {
                block2: {
                    itemMeta = f.itemMeta();
                    if (!(itemMeta instanceof SkullMeta)) break block2;
                    fm = (SkullMeta)itemMeta;
                    itemMeta = s.itemMeta();
                    if (itemMeta instanceof SkullMeta) break block3;
                }
                return false;
            }
            SkullMeta sm = (SkullMeta)itemMeta;
            return Objects.equals(fm.getOwningPlayer(), sm.getOwningPlayer());
        }
    };

    private final boolean requiresMeta;

    private DefaultCheckers(boolean requiresMeta) {
        this.requiresMeta = requiresMeta;
    }

    @Override
    public boolean requiresMeta() {
        return this.requiresMeta;
    }

    @Generated
    public boolean isRequiresMeta() {
        return this.requiresMeta;
    }
}
