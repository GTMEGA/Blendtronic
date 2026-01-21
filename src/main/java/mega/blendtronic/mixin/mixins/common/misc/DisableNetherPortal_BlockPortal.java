package mega.blendtronic.mixin.mixins.common.misc;

import mega.blendtronic.config.MinecraftConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import net.minecraft.block.BlockPortal;
import net.minecraft.world.World;

@Mixin(value = BlockPortal.class, priority = 1100) // Apply after anything else that might alter the logic here
public abstract class DisableNetherPortal_BlockPortal {
    /**
     * @author Ven
     * @reason Disabled by config {@link MinecraftConfig#disablePortalCreation}
     */
    @Overwrite
    public boolean func_150000_e(World p_150000_1_, int p_150000_2_, int p_150000_3_, int p_150000_4_) {
        return false;
    }
}
