package mega.blendtronic.mixin.mixins.common.misc;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(World.class)
public class WorldUnsafeGetBlockMixin {

    /**
     * @author FalsePattern, SirFell
     * <p>
     * Fixes NPE of any World.class unsafe getBlock calls.
     */
    @Redirect(method = "getBlock",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/chunk/Chunk;getBlock(III)Lnet/minecraft/block/Block;",
                    ordinal = 0),
            require = 1)
    private Block bypassReturnGetBlock(Chunk chunk, int xMod15, int y, int zMod15) {
        return chunk != null ? chunk.getBlock(xMod15, y, zMod15) : Blocks.air;
    }
}