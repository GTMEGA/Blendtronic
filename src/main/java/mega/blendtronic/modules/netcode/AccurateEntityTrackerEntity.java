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

package mega.blendtronic.modules.netcode;

public interface AccurateEntityTrackerEntity {
    double accurate$lastScaledXPosition();
    void accurate$lastScaledXPosition(double value);

    double accurate$lastScaledYPosition();
    void accurate$lastScaledYPosition(double value);

    double accurate$lastScaledZPosition();
    void accurate$lastScaledZPosition(double value);

    float accurate$lastYaw();
    void accurate$lastYaw(float value);

    float accurate$lastPitch();
    void accurate$lastPitch(float value);

    float accurate$lastHeadMotion();
    void accurate$lastHeadMotion(float value);

}
