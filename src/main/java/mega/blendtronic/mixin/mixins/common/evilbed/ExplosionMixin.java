package mega.blendtronic.mixin.mixins.common.evilbed;

import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mega.blendtronic.config.MinecraftConfig;
import mega.blendtronic.modules.evilbed.EvilBedState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.Explosion;

@Mixin(Explosion.class)
public abstract class ExplosionMixin {
    @Shadow public float explosionSize;

    @Inject(method = "doExplosionA",
            at = @At("HEAD"))
    private void evilbed_extraEvilMode(CallbackInfo ci) {
        if (EvilBedState.getFuse() && !MinecraftConfig.evilBeds) {
            this.explosionSize *= 20;
        }
    }

    @Expression("(1.0 - ?) * ?") // God, how do they do it?
    @ModifyExpressionValue(method = "doExplosionA",
                           at = @At("MIXINEXTRAS:EXPRESSION"))
    private double evilbed_sendThemFlying(double original) {
        if (EvilBedState.getFuse()) {
            return original * 10;
        } else {
            return original;
        }
    }

    @WrapOperation(method = "doExplosionA",
                   at = @At(value = "INVOKE",
                            target = "Lnet/minecraft/entity/Entity;attackEntityFrom(Lnet/minecraft/util/DamageSource;F)Z"))
    private boolean evilbed_butDontKillThem(Entity instance,
                                            DamageSource source,
                                            float amount,
                                            Operation<Boolean> original) {
        if (EvilBedState.getFuse() && MinecraftConfig.evilBeds) {
            return false;
        } else {
            return original.call(instance, source, amount);
        }
    }

    @Inject(method = "doExplosionA",
            at = @At("RETURN"))
    private void evilbed_reset(CallbackInfo ci) {
        EvilBedState.resetFuse();
    }
}
