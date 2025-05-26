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

public interface AccurateEntityServerPos {
    double accurate$serverPosX();
    double accurate$serverPosY();
    double accurate$serverPosZ();
    void accurate$serverPosX(double x);
    void accurate$serverPosY(double y);
    void accurate$serverPosZ(double z);

    default void accurate$serverPos(double x, double y, double z) {
        accurate$serverPosX(x);
        accurate$serverPosY(y);
        accurate$serverPosZ(z);
    }
}
