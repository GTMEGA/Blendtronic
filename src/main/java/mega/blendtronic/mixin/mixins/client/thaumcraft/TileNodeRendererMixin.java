package mega.blendtronic.mixin.mixins.client.thaumcraft;

import baubles.api.BaublesApi;
import baubles.common.lib.PlayerHandler;
import lombok.val;
import mega.blendtronic.Share;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.nodes.IRevealer;
import thaumcraft.api.nodes.NodeModifier;
import thaumcraft.api.nodes.NodeType;
import thaumcraft.client.renderers.tile.TileNodeRenderer;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;

@Mixin(TileNodeRenderer.class)
public abstract class TileNodeRendererMixin {
    @Redirect(method = "renderTileEntityAt",
              at = @At(value = "INVOKE",
                       target = "Lthaumcraft/client/renderers/tile/TileNodeRenderer;renderNode(Lnet/minecraft/entity/EntityLivingBase;DZZFIIIFLthaumcraft/api/aspects/AspectList;Lthaumcraft/api/nodes/NodeType;Lthaumcraft/api/nodes/NodeModifier;)V"),
              require = 1)
    private void injectBaublesCheck(EntityLivingBase viewer, double viewDistance, boolean visible, boolean depthIgnore, float size, int x, int y, int z, float partialTicks, AspectList aspects, NodeType type, NodeModifier mod) {
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

        TileNodeRenderer.renderNode(viewer, viewDistance, visible, depthIgnore, size, x, y, z, partialTicks, aspects, type, mod);
    }
}
