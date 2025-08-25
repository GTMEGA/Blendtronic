package mega.blendtronic.mixin.mixins.common.thaumcraft;

import baubles.api.BaubleType;
import baubles.api.expanded.IBaubleExpanded;
import org.spongepowered.asm.mixin.Mixin;
import thaumcraft.api.nodes.IRevealer;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

@Mixin(value = IRevealer.class, remap = false)
public interface IRevealerMixin extends IBaubleExpanded {
    @Override
    default String[] getBaubleTypes(ItemStack itemStack) {
        return new String[] { "revealing" };
    }

    default BaubleType getBaubleType(ItemStack var1) {
        return null;
    }

    default void onWornTick(ItemStack var1, EntityLivingBase var2) {

    }

    default void onEquipped(ItemStack var1, EntityLivingBase var2) {

    }

    default void onUnequipped(ItemStack var1, EntityLivingBase var2) {

    }

    default boolean canEquip(ItemStack var1, EntityLivingBase var2) {
        return true;
    }

    default boolean canUnequip(ItemStack var1, EntityLivingBase var2) {
        return true;
    }
}
