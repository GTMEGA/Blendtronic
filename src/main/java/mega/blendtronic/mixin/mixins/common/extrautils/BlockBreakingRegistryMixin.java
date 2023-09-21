package mega.blendtronic.mixin.mixins.common.extrautils;


import com.rwtema.extrautils.tileentity.enderquarry.BlockBreakingRegistry;
import net.minecraft.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = BlockBreakingRegistry.class, remap = false)
public abstract class BlockBreakingRegistryMixin {
    @Redirect(method = "setupBreaking",
              at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;getRenderType()I"),
              remap = true,
              require = 1)
    private int getRenderTypeIgnored(Block instance) {
        return 0;
    }
}
