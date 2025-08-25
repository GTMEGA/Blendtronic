package mega.blendtronic.mixin.mixins.client.gadomancy;

import baubles.common.lib.PlayerHandler;
import lombok.val;
import makeo.gadomancy.client.renderers.tile.RenderTileNodeBasic;
import mega.blendtronic.Share;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.nodes.IRevealer;
import thaumcraft.api.nodes.NodeModifier;
import thaumcraft.api.nodes.NodeType;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;

@Mixin(RenderTileNodeBasic.class)
public abstract class RenderTileNodeBasicMixin {
    @Redirect(method = "renderTileEntityAt(Lnet/minecraft/tileentity/TileEntity;DDDFF)V",
              at = @At(value = "INVOKE",
                       target = "Lmakeo/gadomancy/client/renderers/tile/RenderTileNodeBasic;renderNode(Lnet/minecraft/entity/EntityLivingBase;DZZFDDDFLthaumcraft/api/aspects/AspectList;Lthaumcraft/api/nodes/NodeType;Lthaumcraft/api/nodes/NodeModifier;)V"),
              remap = false,
              require = 1)
    private static void injectBaublesCheck(EntityLivingBase viewer, double viewDistance, boolean visible, boolean depthIgnore, float size, double x, double y, double z, float partialTicks, AspectList aspects, NodeType type, NodeModifier mod) {
        val player = Minecraft.getMinecraft().thePlayer;

        if (!visible && !depthIgnore) {
            val baubles = PlayerHandler.getPlayerBaubles(player);

            if (baubles == null) {
                return;
            }

            for (int i = 0; i < baubles.getSizeInventory(); i++) {
                val bauble = baubles.getStackInSlot(i);

                if (bauble != null && bauble.getItem() instanceof IRevealer revealer) {
                    val doRender = revealer.showNodes(bauble, player);

                    visible = doRender;
                    depthIgnore = doRender;

                    break;
                }
            }
        }

        RenderTileNodeBasic.renderNode(viewer, viewDistance, visible, depthIgnore, size, x, y, z, partialTicks, aspects, type, mod);
    }
}
