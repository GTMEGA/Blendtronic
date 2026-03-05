package mega.blendtronic.mixin.mixins.common.evilbed;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import mega.blendtronic.config.MinecraftConfig;
import mega.blendtronic.modules.evilbed.EvilBedState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

@Mixin(net.minecraft.block.BlockBed.class)
public abstract class BlockBedMixin {
    @WrapOperation(method = "onBlockActivated",
                   at = @At(value = "INVOKE",
                            target = "Lnet/minecraft/world/World;getBiomeGenForCoords(II)Lnet/minecraft/world/biome/BiomeGenBase;"))
    private BiomeGenBase alwaysExplode(World instance,
                                       int x,
                                       int z,
                                       Operation<BiomeGenBase> original,
                                       @Local(argsOnly = true) EntityPlayer player) {
        if (original.call(instance, x, z) == BiomeGenBase.hell) {
            return BiomeGenBase.hell;
        }

        if (MinecraftConfig.evilBeds) {
            player.addChatComponentMessage(new ChatComponentTranslation("§c§lYou're going into orbit, you stupid mutt!"));
        } else {
            player.addChatComponentMessage(new ChatComponentTranslation("§4§lWhat made you think that was gonna work, buddy?"));
        }

        EvilBedState.setFuse();
        return BiomeGenBase.hell;
    }
}
