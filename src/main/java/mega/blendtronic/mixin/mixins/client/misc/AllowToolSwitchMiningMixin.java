package mega.blendtronic.mixin.mixins.client.misc;

import net.minecraft.client.multiplayer.PlayerControllerMP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PlayerControllerMP.class)
public abstract class AllowToolSwitchMiningMixin {
    @Shadow
    private int currentBlockX;
    @Shadow
    private int currentBlockY;
    @Shadow
    private int currentblockZ;

    /**
     * @author Ven
     * @reason Allows switching tools when breaking blocks
     */
    @Overwrite
    private boolean sameToolAndBlock(int posX, int posY, int posZ) {
        return posX == currentBlockX && posY == this.currentBlockY && posZ == this.currentblockZ;
    }
}
