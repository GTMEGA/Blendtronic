package mega.blendtronic.mixin.mixins.common.misc;

import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.Collection;
import java.util.HashSet;

@Mixin(value = World.class)
public class WorldUpdateEntitiesRemoveAllMixin {
    /**
     * @author Botn365
     * <p>
     * Reduces lag spike when toRemoveTileEntities is large
     */
    @ModifyArg(method = "updateEntities",at = @At(value = "INVOKE",target = "Ljava/util/List;removeAll(Ljava/util/Collection;)Z",ordinal = 1),index = 0,require = 1)
    private Collection<?> fix(Collection<?> collection) {
        return new HashSet<>(collection);
    }
}
