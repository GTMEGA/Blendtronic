package mega.blendtronic.mixin.mixins.common.fastnbt;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Objects;

/**
 * Base Mixin for {@link NBTTagCompound}, requires either {@link NBTTagCompoundMixin_ArrayMap} or {@link NBTTagCompoundMixin_HashMap}
 */
@Mixin(NBTTagCompound.class)
public abstract class NBTTagCompoundMixin_Base extends NBTBase implements NBTTagCompoundAccessor {
    @Inject(method = "<init>",
            at = @At("RETURN"),
            require = 1)
    private void realMapInit(CallbackInfo ci) {
        setTagMap(new Object2ObjectArrayMap<>(10));
    }

    /**
     * @author Ven
     * @reason FastUtil Replacement
     */
    @Overwrite
    public boolean equals(Object o) {
        if (super.equals(o) && o instanceof NBTTagCompound otherNbt) {
            //noinspection CastToIncompatibleInterface
            return Objects.equals(getTagMap(), ((NBTTagCompoundAccessor) otherNbt).getTagMap());
        }
        return false;
    }
}
