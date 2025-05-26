/*
 * Blendtronic
 *
 * Copyright (C) 2021-2025 SirFell, the MEGA team
 * All Rights Reserved
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, only version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package mega.blendtronic.mixin.mixins.common.intcache;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import mega.blendtronic.modules.intcache.NewIntCache;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.ChunkPosition;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;

import java.util.List;
import java.util.Random;

/**
 * @author ah-OOG-ah
 * @author falsepattern
 *
 * The hodgepodge version reimplements a lot of the logic with inject-cancels. This is more robust than their solution.
 */
@Mixin(WorldChunkManager.class)
public abstract class WorldChunkManagerMixin {
    @Redirect(method = { "getRainfall", "getBiomesForGeneration", "areBiomesViable",
                         "getBiomeGenAt([Lnet/minecraft/world/biome/BiomeGenBase;IIIIZ)[Lnet/minecraft/world/biome/BiomeGenBase;",
                         "findBiomePosition" },
              at = @At(value = "INVOKE",
                       target = "Lnet/minecraft/world/gen/layer/IntCache;resetIntCache()V"))
    private void nukeIntCacheReset() {}

    @Inject(method = "getRainfall",
            at = @At(value = "INVOKE_ASSIGN",
                     target = "Lnet/minecraft/world/gen/layer/GenLayer;getInts(IIII)[I",
                     shift = At.Shift.AFTER))
    private void getRainfall$cachePreReturn(float[] listToReuse, int x, int z, int width, int length,
                                            CallbackInfoReturnable<float[]> cir, @Local(name = "aint") int[] ints, @Share("intsPass") LocalRef<int[]> intsPass) {
        intsPass.set(ints);
    }

    @Inject(method = "getRainfall",
            at = @At("RETURN"))
    private void getRainfall$releaseCache(float[] listToReuse, int x, int z, int width, int length,
                                          CallbackInfoReturnable<float[]> cir, @Share("intsPass") LocalRef<int[]> intsPass) {
        NewIntCache.releaseCache(intsPass.get());
    }

    @Inject(method = "getBiomesForGeneration",
            at = @At(value = "INVOKE_ASSIGN",
                     target = "Lnet/minecraft/world/gen/layer/GenLayer;getInts(IIII)[I",
                     shift = At.Shift.AFTER))
    private void getBiomesForGeneration$cachePreReturn(BiomeGenBase[] biomes, int x, int z, int width, int height,
                                                       CallbackInfoReturnable<BiomeGenBase[]> cir, @Local(name = "aint") int[] ints, @Share("intsPass") LocalRef<int[]> intsPass) {
        intsPass.set(ints);
    }

    @Inject(method = "getBiomesForGeneration",
            at = @At("RETURN"))
    private void getBiomesForGeneration$releaseCache(BiomeGenBase[] biomes, int x, int z, int width, int height,
                                                     CallbackInfoReturnable<BiomeGenBase[]> cir, @Share("intsPass") LocalRef<int[]> intsPass) {
        NewIntCache.releaseCache(intsPass.get());
    }

    @Inject(method = "getBiomeGenAt([Lnet/minecraft/world/biome/BiomeGenBase;IIIIZ)[Lnet/minecraft/world/biome/BiomeGenBase;",
            at = @At(value = "INVOKE_ASSIGN",
                     target = "Lnet/minecraft/world/gen/layer/GenLayer;getInts(IIII)[I",
                     shift = At.Shift.AFTER))
    private void getBiomeGenAt$cachePreReturn(BiomeGenBase[] listToReuse, int x, int y, int width, int length, boolean cacheFlag,
                                              CallbackInfoReturnable<BiomeGenBase[]> cir, @Local(name = "aint") int[] ints, @Share("intsPass") LocalRef<int[]> intsPass) {
        intsPass.set(ints);
    }

    @Inject(method = "getBiomeGenAt([Lnet/minecraft/world/biome/BiomeGenBase;IIIIZ)[Lnet/minecraft/world/biome/BiomeGenBase;",
            at = @At("RETURN"),
            slice = @Slice(from = @At(value = "INVOKE_ASSIGN",
                                      target = "Lnet/minecraft/world/gen/layer/GenLayer;getInts(IIII)[I",
                                      shift = At.Shift.AFTER)))
    private void getBiomeGenAt$releaseCache(BiomeGenBase[] listToReuse, int x, int y, int width, int length, boolean cacheFlag,
                                            CallbackInfoReturnable<BiomeGenBase[]> cir, @Share("intsPass") LocalRef<int[]> intsPass) {
        NewIntCache.releaseCache(intsPass.get());
    }

    @Inject(method = "areBiomesViable",
            at = @At(value = "INVOKE_ASSIGN",
                     target = "Lnet/minecraft/world/gen/layer/GenLayer;getInts(IIII)[I",
                     shift = At.Shift.AFTER))
    private void areBiomesViable$cachePreReturn(int x, int z, int radius, List<BiomeGenBase> allowed,
                                                CallbackInfoReturnable<Boolean> cir, @Local(name = "aint") int[] ints, @Share("intsPass") LocalRef<int[]> intsPass) {
        intsPass.set(ints);
    }

    @Inject(method = "areBiomesViable",
            at = @At("RETURN"))
    private void areBiomesViable$releaseCache(int x, int z, int radius, List<BiomeGenBase> allowed,
                                              CallbackInfoReturnable<Boolean> cir, @Share("intsPass") LocalRef<int[]> intsPass) {
        NewIntCache.releaseCache(intsPass.get());
    }

    @Inject(method = "findBiomePosition",
            at = @At(value = "INVOKE_ASSIGN",
                     target = "Lnet/minecraft/world/gen/layer/GenLayer;getInts(IIII)[I",
                     shift = At.Shift.AFTER))
    private void findBiomePosition$cachePreReturn(int p_150795_1_, int p_150795_2_, int p_150795_3_, List<BiomeGenBase> p_150795_4_, Random p_150795_5_,
                                                  CallbackInfoReturnable<ChunkPosition> cir, @Local(name = "aint") int[] ints, @Share("intsPass") LocalRef<int[]> intsPass) {
        intsPass.set(ints);
    }

    @Inject(method = "findBiomePosition",
            at = @At("RETURN"))
    private void findBiomePosition$releaseCache(int p_150795_1_, int p_150795_2_, int p_150795_3_, List<BiomeGenBase> p_150795_4_, Random p_150795_5_,
                                                CallbackInfoReturnable<ChunkPosition> cir, @Share("intsPass") LocalRef<int[]> intsPass) {
        NewIntCache.releaseCache(intsPass.get());
    }

}
