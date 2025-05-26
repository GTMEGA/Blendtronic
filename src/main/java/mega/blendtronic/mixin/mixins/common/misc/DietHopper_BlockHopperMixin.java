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

package mega.blendtronic.mixin.mixins.common.misc;

import com.google.common.collect.ImmutableList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHopper;
import net.minecraft.block.material.Material;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

// Ported from Hodgepodge
@Mixin(BlockHopper.class)
public abstract class DietHopper_BlockHopperMixin extends Block {

    // Inspired by DietHoppers by rwtema - https://github.com/rwtema/DietHopper/
    @Unique
    private static final EnumMap<EnumFacing, List<AxisAlignedBB>> BLEND$BOUNDS;

    static {
        List<AxisAlignedBB> commonBounds = ImmutableList
                .of(blend$makeAABB(0, 10, 0, 16, 16, 16), blend$makeAABB(4, 4, 4, 12, 10, 12));
        BLEND$BOUNDS = Arrays.stream(EnumFacing.values()).filter(t -> t != EnumFacing.UP).collect(
                Collectors.toMap(
                        a -> a,
                        a -> new ArrayList<>(commonBounds),
                        (u, v) -> { throw new IllegalStateException(); },
                        () -> new EnumMap<>(EnumFacing.class)));

        BLEND$BOUNDS.get(EnumFacing.DOWN).add(blend$makeAABB(6, 0, 6, 10, 4, 10));

        BLEND$BOUNDS.get(EnumFacing.NORTH).add(blend$makeAABB(6, 4, 0, 10, 8, 4));
        BLEND$BOUNDS.get(EnumFacing.SOUTH).add(blend$makeAABB(6, 4, 12, 10, 8, 16));

        BLEND$BOUNDS.get(EnumFacing.WEST).add(blend$makeAABB(12, 4, 6, 16, 8, 10));
        BLEND$BOUNDS.get(EnumFacing.EAST).add(blend$makeAABB(0, 4, 6, 4, 8, 10));
    }

    protected DietHopper_BlockHopperMixin(Material materialIn) {
        super(materialIn);
    }

    @Unique
    private static AxisAlignedBB blend$makeAABB(int fromX, int fromY, int fromZ, int toX, int toY, int toZ) {
        return AxisAlignedBB.getBoundingBox(fromX / 16F, fromY / 16F, fromZ / 16F, toX / 16F, toY / 16F, toZ / 16F);
    }

    @Unique
    private static MovingObjectPosition blend$rayTrace(Vec3 pos, Vec3 start, Vec3 end, AxisAlignedBB boundingBox) {
        final Vec3 vec3d = start.addVector(-pos.xCoord, -pos.yCoord, -pos.zCoord);
        final Vec3 vec3d1 = end.addVector(-pos.xCoord, -pos.yCoord, -pos.zCoord);

        final MovingObjectPosition raytraceresult = boundingBox.calculateIntercept(vec3d, vec3d1);
        if (raytraceresult == null) return null;

        final Vec3 res = raytraceresult.hitVec.addVector(pos.xCoord, pos.yCoord, pos.zCoord);
        return new MovingObjectPosition(
                (int) res.xCoord,
                (int) res.yCoord,
                (int) res.zCoord,
                raytraceresult.sideHit,
                pos);
    }

    @Override
    public MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3 start, Vec3 end) {
        final Vec3 pos = Vec3.createVectorHelper(x, y, z);
        final EnumFacing facing = EnumFacing
                .values()[(BlockHopper.getDirectionFromMetadata(world.getBlockMetadata(x, y, z)))];
        List<AxisAlignedBB> list = BLEND$BOUNDS.get(facing);
        if (list == null) return super.collisionRayTrace(world, x, y, z, start, end);
        return list.stream().map(bb -> blend$rayTrace(pos, start, end, bb)).anyMatch(Objects::nonNull)
               ? super.collisionRayTrace(world, x, y, z, start, end)
               : null;
    }
}
