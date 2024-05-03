package mega.blendtronic.mixin.plugin;

import com.falsepattern.lib.mixin.ITargetedMod;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Predicate;

import static com.falsepattern.lib.mixin.ITargetedMod.PredicateHelpers.matches;
import static com.falsepattern.lib.mixin.ITargetedMod.PredicateHelpers.startsWith;

@RequiredArgsConstructor
public enum TargetedMod implements ITargetedMod {
    FASTCRAFT("FastCraft", false, startsWith("fastcraft")),
    EXTRA_UTILITIES("Extra Utilities", false, startsWith("extrautilities")),
    BUILDCRAFT("BuildCraft", false, matches("buildcraft-[\\d\\.]+")),
    THAUMCRAFT("Thaumcraft",false,startsWith("Thaumcraft-1.7.10"))
    ;

    @Getter
    private final String modName;
    @Getter
    private final boolean loadInDevelopment;
    @Getter
    private final Predicate<String> condition;
}
