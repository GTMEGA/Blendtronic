package com.github.gtmega.blendtronic.mixins.minecraft;

import net.minecraft.entity.EntityLiving;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static org.objectweb.asm.Opcodes.PUTFIELD;

@Mixin(EntityLiving.class)
public class EntityLivingDropLootOnDespawnMixin {
    @Shadow
    private boolean persistenceRequired;

    @Shadow
    protected void dropEquipment(boolean wasHitByPlayer, int lootMultiplicatorPct) {
        throw new AbstractMethodError("Shadow");
    }

    /**
     * @author ElNounch
     * <p>
     * Allow mobs that picked up loot to despawn.
     */
    @Redirect(method="onLivingUpdate()V", at = @At(value="FIELD", target="Lnet/minecraft/entity/EntityLiving;persistenceRequired:Z", opcode=PUTFIELD))
    public void dontSetPersistenceRequired(EntityLiving o, boolean newVal) {
    }

    /**
     * @author ElNounch
     * <p>
     * Drop Picked Loot on despawn.
     */
    @Inject(method = "despawnEntity()V", at = @At(value="INVOKE", target="Lnet/minecraft/entity/EntityLiving;setDead()V"))
    public void despawnEntitydropPickedLoot(CallbackInfo ci) {
        this.dropEquipment(false, 0);
    }
}
