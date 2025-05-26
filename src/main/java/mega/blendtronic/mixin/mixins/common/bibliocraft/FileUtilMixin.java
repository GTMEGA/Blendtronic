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

package mega.blendtronic.mixin.mixins.common.bibliocraft;

import jds.bibliocraft.FileUtil;
import mega.blendtronic.util.PathSanitizer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(FileUtil.class)
public abstract class FileUtilMixin {
    @ModifyArg(
            method = { "isBookSaved", "saveBook", "loadBook", "getAuthorList", "getPublistList",
                       "addPublicPrivateFieldToBook", "deleteBook", "updatePublicFlag", "saveNBTtoFile", "saveBookMeta",
                       "getBookType(Lnet/minecraft/world/World;I)I", "getBookType(ZI)I", "loadBookNBT" },
            at = @At(value = "INVOKE", target = "Ljava/io/File;<init>(Ljava/io/File;Ljava/lang/String;)V"),
            index = 1,
            remap = false)
    private String hodgepodge$sanitizeBookPath(String v) {
        return PathSanitizer.sanitizeFileName(v);
    }
}
