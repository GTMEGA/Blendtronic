/*
 * Blendtronic
 *
 * Copyright (C) 2021-2025 SirFell, the MEGA team
 * All Rights Reserved
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, only version 3 of the License.
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

public interface AccuratePositionContainer {
    double accurate$posX();
    double accurate$posY();
    double accurate$posZ();
    void accurate$posX(double x);
    void accurate$posY(double y);
    void accurate$posZ(double z);

    default void accurate$pos(double x, double y, double z) {
        accurate$posX(x);
        accurate$posY(y);
        accurate$posZ(z);
    }

    default void accuratePosition$writePacketData(PacketBuffer data) {
        data.writeDouble(accurate$posX());
        data.writeDouble(accurate$posY());
        data.writeDouble(accurate$posZ());
    }

    default void accuratePosition$readPacketData(PacketBuffer data) {
        accurate$posX(data.readDouble());
        accurate$posY(data.readDouble());
        accurate$posZ(data.readDouble());
    }
}
