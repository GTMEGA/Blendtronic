package mega.blendtronic.mixin.mixins.common.misc;

import lombok.val;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityBoat.class)
public abstract class BoatDamageRedirectMixin extends Entity {
    public BoatDamageRedirectMixin(World world) {
        super(world);
    }

    @Inject(method = "onUpdate",
            at = @At(value = "INVOKE",
                     target = "Lnet/minecraft/entity/item/EntityBoat;moveEntity(DDD)V",
                     shift = At.Shift.AFTER),
            require = 1)
    private void handleCollision(CallbackInfo ci) {
        if (worldObj.isRemote)
            return;
        if (isDead)
            return;
        if (!isCollidedHorizontally)
            return;
        val velocity = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
        if (velocity <= 0.2D)
            return;
        if (!(riddenByEntity instanceof EntityLivingBase))
            return;
        val entityLivingBase = (EntityLivingBase) riddenByEntity;
        if (entityLivingBase.isPotionActive(Potion.confusion))
            return;

        if (rand.nextFloat() >= 0.5)
            entityLivingBase.attackEntityFrom(DamageSource.fall, 1F);
    }

    @Redirect(method = "onUpdate",
              at = @At(value = "FIELD",
                       target = "Lnet/minecraft/entity/item/EntityBoat;isCollidedHorizontally:Z",
                       ordinal = 2),
              require = 1)
    private boolean redirectCollision(EntityBoat instance) {
        return false;
    }
}
