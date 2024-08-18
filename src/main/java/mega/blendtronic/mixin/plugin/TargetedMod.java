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

package mega.blendtronic.mixin.plugin;

import com.falsepattern.lib.mixin.ITargetedMod;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.function.Predicate;

import static com.falsepattern.lib.mixin.IMixin.PredicateHelpers.avoid;
import static com.falsepattern.lib.mixin.IMixin.PredicateHelpers.require;
import static com.falsepattern.lib.mixin.ITargetedMod.PredicateHelpers.contains;
import static com.falsepattern.lib.mixin.ITargetedMod.PredicateHelpers.matches;
import static com.falsepattern.lib.mixin.ITargetedMod.PredicateHelpers.startsWith;
import static mega.blendtronic.mixin.plugin.Extras.OPTIFINE_DYNAMIC_LIGHTS_VERSIONS;
import static mega.blendtronic.mixin.plugin.Extras.OPTIFINE_SHADERSMOD_VERSIONS;

@RequiredArgsConstructor
public enum TargetedMod implements ITargetedMod {
    FASTCRAFT("FastCraft", false, startsWith("fastcraft")),
    EXTRA_UTILITIES("Extra Utilities", false, startsWith("extrautilities")),
    BUILDCRAFT("BuildCraft", false, matches("buildcraft-[\\d\\.]+")),
    THAUMCRAFT("Thaumcraft",false,startsWith("thaumcraft-1.7.10")),
    BIBLIOCRAFT("BiblioCraft", false, startsWith("bibliocraft")),
    OPTIFINE_WITHOUT_SHADERS("OptiFine without shaders", false, contains("optifine").and(OPTIFINE_SHADERSMOD_VERSIONS.or(OPTIFINE_DYNAMIC_LIGHTS_VERSIONS).negate())),
    OPTIFINE_WITH_SHADERS("OptiFine with shaders", false, contains("optifine").and(OPTIFINE_SHADERSMOD_VERSIONS)),
    OPTIFINE_WITH_DYNAMIC_LIGHTS("OptiFine with dynamic lights", false, contains("optifine").and(OPTIFINE_DYNAMIC_LIGHTS_VERSIONS)),
    ;
    public static Predicate<List<ITargetedMod>> REQUIRE_OPTIFINE_WITHOUT_SHADERS = require(OPTIFINE_WITHOUT_SHADERS).or(require(OPTIFINE_WITH_DYNAMIC_LIGHTS));
    public static Predicate<List<ITargetedMod>> REQUIRE_OPTIFINE_WITH_SHADERS = require(OPTIFINE_WITH_SHADERS);
    public static Predicate<List<ITargetedMod>> AVOID_OPTIFINE_WITH_SHADERS = avoid(OPTIFINE_WITH_SHADERS);
    public static Predicate<List<ITargetedMod>> REQUIRE_OPTIFINE_WITH_DYNAMIC_LIGHTS = require(OPTIFINE_WITH_SHADERS).or(require(OPTIFINE_WITH_DYNAMIC_LIGHTS));
    public static Predicate<List<ITargetedMod>> REQUIRE_ANY_OPTIFINE = require(OPTIFINE_WITH_SHADERS).or(require(OPTIFINE_WITHOUT_SHADERS)).or(require(OPTIFINE_WITH_DYNAMIC_LIGHTS));
    public static Predicate<List<ITargetedMod>> AVOID_ANY_OPTIFINE = avoid(OPTIFINE_WITH_SHADERS).and(avoid(OPTIFINE_WITHOUT_SHADERS)).and(avoid(OPTIFINE_WITH_DYNAMIC_LIGHTS));

    @Getter
    private final String modName;
    @Getter
    private final boolean loadInDevelopment;
    @Getter
    private final Predicate<String> condition;
}
