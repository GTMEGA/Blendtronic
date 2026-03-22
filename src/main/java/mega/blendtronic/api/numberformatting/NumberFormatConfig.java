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

package mega.blendtronic.api.numberformatting;


import com.falsepattern.lib.config.Config;
import mega.blendtronic.Tags;

@Config(modid = Tags.MOD_ID,
        category = "number_formatting")
public final class NumberFormatConfig {

    @Config.Comment({ "Completely disables ALL number formatting, all numbers will show 'raw', i.e. 1000000", })
    @Config.DefaultBoolean(false)
    public static boolean disableFormattedNotation = false;

    @Config.Comment({
            "Completely disables exponential notation. No scientific, standard form or engineering notation." })
    @Config.DefaultBoolean(false)
    public static boolean globalDisableExponentialNotation = false;

    @Config.Comment({ "True indicates to use mB, false uses L." })
    @Config.DefaultBoolean(true)
    public static boolean useForgeFluidMillibuckets = true;

    @Config.Comment({ "Controls how very large numbers are rendered when scientific notation is used.",
            "  SCIENTIFIC   – Standard scientific notation (e.g. 1.23e12)",
            "  ENGINEERING  – Engineering notation with exponent in multiples of 3 (e.g. 123e9)",
            "  POWER_OF_TEN – Power-of-ten notation using explicit multiplication (e.g. 1.23*10^12)",
            "Invalid values will cause the game to fail fast with an error." })
    @Config.DefaultString("SCIENTIFIC")
    public static String formatPattern = "SCIENTIFIC";

    @Config.Ignore
    public static ExponentialFormat EXPONENTIAL_FORMAT = ExponentialFormat.SCIENTIFIC;
}