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
