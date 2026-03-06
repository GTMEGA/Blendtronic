package mega.blendtronic.mixin.mixins.common.fastnbt;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import lombok.val;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

import java.io.DataOutput;
import java.io.IOException;

/**
 * Speeds up the {@link NBTTagCompound} using a {@link Object2ObjectArrayMap}
 */
@Mixin(NBTTagCompound.class)
public abstract class NBTTagCompoundMixin_ArrayMap extends NBTBase implements NBTTagCompoundAccessor {
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
    public void write(DataOutput output) throws IOException {
        val map = (Object2ObjectArrayMap<String, NBTBase>) getTagMap();
        if (!map.isEmpty()) {
            val it = map.object2ObjectEntrySet()
                        .fastIterator();
            while (it.hasNext()) {
                val entry = it.next();
                NBTTagCompoundAccessor.internalWrite(entry.getKey(), entry.getValue(), output);
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
        //noinspection CastToIncompatibleInterface
        ((NBTTagCompoundAccessor) otherNbt).setTagMap(new Object2ObjectArrayMap<>(getTagMap().size()));

        val it = ((Object2ObjectArrayMap<String, NBTBase>) getTagMap()).object2ObjectEntrySet()
                                                                       .fastIterator();
        while (it.hasNext()) {
            val entry = it.next();
            otherNbt.setTag(entry.getKey(), (entry.getValue()).copy());
        }
        return otherNbt;
    }
}
