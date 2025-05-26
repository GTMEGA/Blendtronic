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

package mega.blendtronic.asm;

import com.falsepattern.lib.turboasm.ClassNodeHandle;
import com.falsepattern.lib.turboasm.TurboClassTransformer;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;

import net.minecraftforge.classloading.FluidIdTransformer;

/**
 * More performant version of {@link FluidIdTransformer}
 */
public class FluidIDFixer implements TurboClassTransformer {
    private static final String FLUID_TYPE = "net/minecraftforge/fluids/FluidStack";
    private static final String GETID_NAME = "getFluidID";
    private static final String LEGACY_FIELDNAME = "fluidID";
    private static final String GETID_DESC = "()I";

    @Override
    public String owner() {
        return "FluidIDFixer";
    }

    @Override
    public String name() {
        return "Blendtronic";
    }

    private static FieldInsnNode isFluidIDFieldAccess(AbstractInsnNode insn) {
        if (insn.getOpcode() != Opcodes.GETFIELD || !(insn instanceof FieldInsnNode))
            return null;
        val fieldInsn = (FieldInsnNode) insn;
        if (!LEGACY_FIELDNAME.equals(fieldInsn.name) ||
            !FLUID_TYPE.equals(fieldInsn.owner))
            return null;

        return fieldInsn;
    }

    @Override
    public boolean shouldTransformClass(@NotNull String className, @NotNull ClassNodeHandle classNode) {
        return !className.startsWith("net/minecraftforge/") &&
               !className.startsWith("net/minecraft/");
    }

    @Override
    public boolean transformClass(@NotNull String className, @NotNull ClassNodeHandle classNode) {
        val cn = classNode.getNode();
        if (cn == null)
            return false;
        boolean modified = false;
        for (val method: cn.methods) {
            val insnList = method.instructions.iterator();
            while (insnList.hasNext()) {
                val fluidInsn = isFluidIDFieldAccess(insnList.next());
                if (fluidInsn != null) {
                    insnList.set(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, FLUID_TYPE, GETID_NAME, GETID_DESC, false));
                    modified = true;
                }
            }
        }
        return modified;
    }
}
