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
        val cn = classNode.getNode();
        if (cn == null)
            return false;
        for (val method: cn.methods) {
            val insnList = method.instructions.iterator();
            while (insnList.hasNext()) {
                if (isFluidIDFieldAccess(insnList.next()) != null) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void transformClass(@NotNull String className, @NotNull ClassNodeHandle classNode) {
        val cn = classNode.getNode();
        if (cn == null)
            return;
        for (val method: cn.methods) {
            val insnList = method.instructions.iterator();
            while (insnList.hasNext()) {
                val fluidInsn = isFluidIDFieldAccess(insnList.next());
                if (fluidInsn != null) {
                    insnList.set(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, FLUID_TYPE, GETID_NAME, GETID_DESC, false));
                }
            }
        }
    }
}
