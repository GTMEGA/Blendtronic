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

package mega.blendtronic.mixin.mixins.client.misc;

import mega.blendtronic.Share;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import paulscode.sound.SoundSystem;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.Source;

@Mixin(targets = "net.minecraft.client.audio.SoundManager$SoundSystemStarterThread")
public class OpenALNativeCrashFix extends SoundSystem {
    /**
     * @author FalsePattern
     * @reason Crash mitigation
     */
    @Overwrite(remap = false)
    public boolean playing(String p_playing_1_) {
        synchronized (SoundSystemConfig.THREAD_SYNC) {
            if (this.soundLibrary == null)
                return false;
            Source source = this.soundLibrary.getSources().get(p_playing_1_);
            if (source == null)
                return false;
            try {
                if (source.playing())
                    return true;
            } catch (UnsatisfiedLinkError e) {
                Share.LOG.error("Suppressed OpenAL native crash", e);
            }
            try {
                return source.paused() || source.preLoad;
            } catch (UnsatisfiedLinkError e) {
                Share.LOG.error("Suppressed OpenAL native crash", e);
            }
            return false;
        }
    }
}
