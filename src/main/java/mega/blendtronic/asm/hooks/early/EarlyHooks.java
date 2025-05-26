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

package mega.blendtronic.asm.hooks.early;

public class EarlyHooks {
    private static final String INT_MIN_STRING = Integer.toString(Integer.MIN_VALUE);
    private static final String INT_MAX_STRING = Integer.toString(Integer.MAX_VALUE);
    private static final String DOUBLE_MAX_STRING = Double.toString(Double.MAX_VALUE);
    private static final String DOUBLE_NEG_MAX_STRING = Double.toString(-Double.MAX_VALUE);

    public static String intToCachedString(int value) {
        return switch (value) {
            case Integer.MIN_VALUE -> INT_MIN_STRING;
            case Integer.MAX_VALUE -> INT_MAX_STRING;
            case 0 -> "0";
            case 1 -> "1";
            case 2 -> "2";
            case 3 -> "3";
            case 4 -> "4";
            case 5 -> "5";
            case 10 -> "10";
            case 100 -> "100";
            default -> String.valueOf(value).intern();
        };
    }

    public static String doubleToCachedString(double value) {
        if (value == Double.MAX_VALUE) {
            return DOUBLE_MAX_STRING;
        } else if (value == -Double.MAX_VALUE) {
            return DOUBLE_NEG_MAX_STRING;
        }
        return String.valueOf(value).intern();
    }

    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    public static String[] internArray(String[] array) {
        if (array == null) {
            return null;
        }
        if (array.length == 0) {
            return EMPTY_STRING_ARRAY;
        }
        for (int i = 0; i < array.length; i++) {
            array[i] = (array[i] == null) ? null : array[i].intern();
        }
        return array;
    }
}
