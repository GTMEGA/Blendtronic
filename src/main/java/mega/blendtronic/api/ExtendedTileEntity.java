package mega.blendtronic.api;

import net.minecraft.tileentity.TileEntity;

/**
 * Implemented on top of {@link TileEntity}, you can implement it yourself to implement behaviour.
 * <p>
 * TODO: TileEntities such as Chests will *tick* constantly to check the state of themselves and their neighbours.
 * TODO: This can be implemented there and in other places to reduce their load on the server.
 * TODO: Additional methods may be added in here to help create more efficient TileEntities.
 */
public interface ExtendedTileEntity {
    /**
     * Called after the block meta of this tile entity has changed.
     * <p>
     * This value can be found in: {@link TileEntity#blockMetadata}
     * <p>
     * Useful if you don't want to have blocks ticking to check for this.
     */
    void blockMetaChanged();
}
