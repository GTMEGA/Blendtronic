/*
 * Blendtronic
 *
 * Copyright (C) 2021-2024 SirFell, the MEGA team
 * All Rights Reserved
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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
