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

package mega.blendtronic.util;

import java.util.regex.Pattern;

/**
 * Utilities for sanitizing file paths generated with user input
 */
public final class PathSanitizer {

    // This class just has some static utilities
    private PathSanitizer() {}

    // On Linux it's just /, macOS forbids / and :
    // On Windows it's <>:"/\|?*
    private static final Pattern ILLEGAL_FILE_NAME_CHARS = Pattern.compile("[/\\\\<>:\"|?*]");

    /**
     * Replaces all illegal characters (including directory separators) for a file name from input with dashes.
     *
     * @param input The untrusted file name
     * @return A name that can be trusted to be safe for file reading/creation
     */
    public static String sanitizeFileName(String input) {
        return ILLEGAL_FILE_NAME_CHARS.matcher(input).replaceAll("-").replace('\0', '-');
    }
}
