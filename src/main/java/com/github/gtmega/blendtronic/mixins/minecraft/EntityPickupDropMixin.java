package com.github.gtmega.blendtronic.mixins.minecraft;

import net.minecraft.entity.EntityLiving;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static org.objectweb.asm.Opcodes.PUTFIELD;

@Mixin(EntityLiving.class)
public abstract class EntityPickupDropMixin {

    @Shadow protected abstract void dropEquipment(boolean p_82160_1_, int p_82160_2_);

    /**
     * @author FalsePattern
     * <p>
     * Let mobs holding loot to despawn.
     */
    @Redirect(method = "onLivingUpdate",
            at = @At(value = "FIELD",
                    target = "Lnet/minecraft/entity/EntityLiving;persistenceRequired:Z",
                    opcode = Opcodes.PUTFIELD,
                    ordinal = 0),
            require = 1)
    private void bypassPersistence(EntityLiving instance, boolean value) {}

    /**
     * @author FalsePattern
     * <p>
     * Forces despawning entities to drop their equipment.
     */
    @Redirect(method = "despawnEntity",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/entity/EntityLiving;setDead()V"),
            require = 1)
    private void dropEquipmentOnDespawn(EntityLiving instance) {
        ((EntityPickupDropMixin)(Object)instance).dropEquipment(false, 0);
        instance.setDead();
    }

}
