package mega.blendtronic.mixin.mixins.common.misc;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import lombok.val;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

import java.io.DataOutput;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Mixin(NBTTagCompound.class)
public abstract class FasterNBT_NBTTagCompoundMixin extends NBTBase {
    @Shadow
    public Map<String, NBTBase> tagMap;

    @Shadow
    private static void func_150298_a(String p_150298_0_, NBTBase p_150298_1_, DataOutput p_150298_2_) {
    }

    @WrapOperation(method = "<init>",
                   at = @At(value = "NEW",
                       target = "()Ljava/util/HashMap;"),
                   require = 1)
    private @Nullable HashMap<?, ?> nullMapInit(Operation<HashMap<?, ?>> original) {
        return null;
    }

    @Inject(method = "<init>",
            at = @At("RETURN"),
            require = 1)
    private void realMapInit(CallbackInfo ci) {
        this.tagMap = new Object2ObjectArrayMap<>(10);
    }

    /**
     * @author Ven
     * @reason FastUtil Replacement
     */
    @Overwrite
    public void write(DataOutput output) throws IOException {
        val map = (Object2ObjectArrayMap<String, NBTBase>) this.tagMap;
        if (!map.isEmpty()) {
            val it = map.object2ObjectEntrySet()
                        .fastIterator();
            while (it.hasNext()) {
                val entry = it.next();
                func_150298_a(entry.getKey(), entry.getValue(), output);
            }
        }

        // EOF
        output.writeByte(0);
    }

    /**
     * Copied across from HodgePodge: <a href="https://github.com/GTNewHorizons/Hodgepodge/blob/02c793837503285e4287a424d7b7abf7523f621b/src/main/java/com/mitchej123/hodgepodge/core/fml/transformers/mc/NBTTagCompoundHashMapTransformer.java#L23">...</a>
     *
     * @author Ven
     * @reason FastUtil Replacement
     */
    @Overwrite
    public NBTBase copy() {
        val otherNbt = new NBTTagCompound();
        otherNbt.tagMap = new Object2ObjectArrayMap<>(this.tagMap.size());

        val it = ((Object2ObjectArrayMap<String, NBTBase>) this.tagMap).object2ObjectEntrySet()
                                                                       .fastIterator();
        while (it.hasNext()) {
            val entry = it.next();
            otherNbt.setTag(entry.getKey(), (entry.getValue()).copy());
        }
        return otherNbt;
    }

    /**
     * @author Ven
     * @reason FastUtil Replacement
     */
    @Overwrite
    public boolean equals(Object o) {
        if (super.equals(o) && o instanceof NBTTagCompound otherNbt) {
            return Objects.equals(this.tagMap, otherNbt.tagMap);
        }
        return false;
    }
}
