package mega.blendtronic.asm;

import com.falsepattern.lib.turboasm.MergeableTurboTransformer;
import com.falsepattern.lib.turboasm.TurboClassTransformer;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.Arrays;
import java.util.List;

@Getter
@Accessors(fluent = true)
public class BlendtronicTransformer extends MergeableTurboTransformer {
    public BlendtronicTransformer() {
        super(Arrays.asList());
    }
}
