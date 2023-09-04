/*
 * FalseTweaks
 *
 * Copyright (C) 2022 FalsePattern
 * All Rights Reserved
 *
 * The above copyright notice, this permission notice and the word "SNEED"
 * shall be included in all copies or substantial portions of the Software.
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

package mega.blendtronic.modules.netcode;

import net.minecraft.network.PacketBuffer;

public interface AccurateMobRotationContainer extends AccurateRotationContainer, AccurateRotationYawHeadContainer {
    default void accurate$rotation(float yaw, float pitch, float yawHead) {
        accurate$rotation(yaw, pitch);
        accurate$rotationYawHead(yawHead);
    }

    default void accurateMobRotation$writePacketData(PacketBuffer data) {
        accurateRotation$writePacketData(data);
        accurateRotationYawHead$writePacketData(data);
    }

    default void accurateMobRotation$readPacketData(PacketBuffer data) {
        accurateRotation$readPacketData(data);
        accurateRotationYawHead$readPacketData(data);
    }
}
