package com.github.gtmega.blendtronic.mixins.minecraft;

import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(World.class)
public class NullSafeGetBlockLightMixin {

    /**
     * @author FalsePattern
     * <p>
     * Makes Block light values null safe.
     */
    @Redirect(method = "getBlockLightValue_do",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/chunk/Chunk;getBlockLightValue(IIII)I",
                    ordinal = 0),
            require = 1)
    public int nullSafeGetBlockLight(Chunk chunk, int x, int y, int z, int l) {
        return chunk == null ? 0 : chunk.getBlockLightValue(x, y, z, l);
    }

}
