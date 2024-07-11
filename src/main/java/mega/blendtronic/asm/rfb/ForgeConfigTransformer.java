/*
 * Blendtronic
 *
 * Copyright (C) 2021-2024 SirFell, the MEGA team
 * All Rights Reserved
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package mega.blendtronic.asm.rfb;

import com.gtnewhorizons.retrofuturabootstrap.api.ClassNodeHandle;
import com.gtnewhorizons.retrofuturabootstrap.api.ExtensibleClassLoader;
import com.gtnewhorizons.retrofuturabootstrap.api.RfbClassTransformer;
import lombok.val;
import mega.blendtronic.asm.EarlyConfig;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;

import java.util.jar.Manifest;

import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.POP;
import static org.objectweb.asm.Opcodes.PUTFIELD;
import static org.objectweb.asm.Opcodes.NEW;

/**
 * Reduces the memory usage of Forge's Configuration system significantly. Deduplicates identical strings, empty arrays
 * and switches property maps from TreeMaps to fastutil open hashmaps.
 *
 * @author eigenraven
 */
public class ForgeConfigTransformer implements RfbClassTransformer {
    private static final String EARLY_HOOKS_INTERNAL = "mega/blendtronic/asm/hooks/early/EarlyHooks";
    private static final String CATEGORY_INTERNAL = "net/minecraftforge/common/config/ConfigCategory";
    private static final String PROPERTY_INTERNAL = "net/minecraftforge/common/config/Property";
    private static final String OPEN_MAP_INTERNAL = "it/unimi/dsi/fastutil/objects/Object2ObjectOpenHashMap";

    @Pattern("[a-z0-9-]+")
    @Override
    public @NotNull String id() {
        return "forge-config";
    }

    @Override
    public boolean shouldTransformClass(@NotNull ExtensibleClassLoader classLoader, @NotNull RfbClassTransformer.Context context, @Nullable Manifest manifest, @NotNull String className, @NotNull ClassNodeHandle classNode) {
        return (EarlyConfig.optimizeForgeConfigs) && classNode.isPresent() &&
               (className.equals("net.minecraftforge.common.config.Property") || className.equals("net.minecraftforge.common.config.ConfigCategory"));
    }

    @Override
    public void transformClass(@NotNull ExtensibleClassLoader classLoader, @NotNull RfbClassTransformer.Context context, @Nullable Manifest manifest, @NotNull String className, @NotNull ClassNodeHandle classNode) {
        val cn = classNode.getNode();
        if (cn == null) {
            return;
        }
        if (cn.methods == null) {
            return;
        }
        for (final MethodNode mn : cn.methods) {
            if (mn.instructions == null || mn.instructions.size() == 0) {
                continue;
            }
            for (int i = 0; i < mn.instructions.size(); i++) {
                val aInsn = mn.instructions.get(i);
                if (aInsn.getOpcode() == INVOKESTATIC && aInsn instanceof MethodInsnNode mInsn) {
                    if (("java/lang/String".equals(mInsn.owner) && "valueOf".equals(mInsn.name) && "(I)Ljava/lang/String;".equals(mInsn.desc)) ||
                        ("java/lang/Integer".equals(mInsn.owner) && "toString".equals(mInsn.name) && "(I)Ljava/lang/String;".equals(mInsn.desc))) {
                        mInsn.owner = EARLY_HOOKS_INTERNAL;
                        mInsn.name = "intToCachedString";
                    } else if (("java/lang/String".equals(mInsn.owner) && "valueOf".equals(mInsn.name) && "(D)Ljava/lang/String;".equals(mInsn.desc)) ||
                               ("java/lang/Double".equals(mInsn.owner) && "toString".equals(mInsn.name) && "(D)Ljava/lang/String;".equals(mInsn.desc))) {
                        mInsn.owner = EARLY_HOOKS_INTERNAL;
                        mInsn.name = "doubleToCachedString";
                    }
                } else if (aInsn.getOpcode() == PUTFIELD && aInsn instanceof FieldInsnNode fInsn) {
                    if (PROPERTY_INTERNAL.equals(fInsn.owner) && ("value".equals(fInsn.name) || "defaultValue".equals(fInsn.name))) {
                        val intern = new MethodInsnNode(INVOKEVIRTUAL, "java/lang/String", "intern", "()Ljava/lang/String;", false);
                        mn.instructions.insertBefore(fInsn, intern);
                        i++;
                    } else if (PROPERTY_INTERNAL.equals(fInsn.owner) && ("values".equals(fInsn.name) || "defaultValues".equals(fInsn.name) || "validValues".equals(fInsn.name))) {
                        val intern = new MethodInsnNode(INVOKESTATIC, EARLY_HOOKS_INTERNAL, "internArray", "([Ljava/lang/String;)[Ljava/lang/String;", false);
                        mn.instructions.insertBefore(fInsn, intern);
                        i++;
                    } else if (CATEGORY_INTERNAL.equals(fInsn.owner) && "properties".equals(fInsn.name) && "<init>".equals(mn.name)) {
                        mn.instructions.insertBefore(fInsn, new InsnNode(POP));
                        mn.instructions.insertBefore(fInsn, new TypeInsnNode(NEW, OPEN_MAP_INTERNAL));
                        mn.instructions.insertBefore(fInsn, new InsnNode(DUP));
                        mn.instructions.insertBefore(fInsn, new MethodInsnNode(INVOKESPECIAL, OPEN_MAP_INTERNAL, "<init>", "()V", false));
                        i += 4;
                    }
                }
            }
        }
    }
}