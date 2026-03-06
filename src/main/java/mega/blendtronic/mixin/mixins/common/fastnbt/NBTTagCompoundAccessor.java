package mega.blendtronic.mixin.mixins.common.fastnbt;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

import java.io.DataOutput;
import java.io.IOException;
import java.util.Map;

@Mixin(NBTTagCompound.class)
public interface NBTTagCompoundAccessor {
    @Invoker("func_150298_a")
    @SuppressWarnings("RedundantThrows")
    static void internalWrite(String name, NBTBase data, DataOutput output) throws IOException {
    }

    @Accessor("tagMap")
    Map<String, NBTBase> getTagMap();

    @Accessor("tagMap")
    void setTagMap(Map<String, NBTBase> tagMap);
}
