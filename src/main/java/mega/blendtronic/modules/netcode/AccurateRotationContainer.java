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

public interface AccurateRotationContainer {
    float accurate$rotationYaw();
    float accurate$rotationPitch();
    void accurate$rotationYaw(float yaw);
    void accurate$rotationPitch(float pitch);

    default void accurate$rotation(float yaw, float pitch) {
        accurate$rotationYaw(yaw);
        accurate$rotationPitch(pitch);
    }

    default void accurateRotation$writePacketData(PacketBuffer data) {
        data.writeFloat(accurate$rotationYaw());
        data.writeFloat(accurate$rotationPitch());
    }

    default void accurateRotation$readPacketData(PacketBuffer data) {
        accurate$rotationYaw(data.readFloat());
        accurate$rotationPitch(data.readFloat());
    }
}
