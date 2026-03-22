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

package mega.blendtronic.api.numberformatting.options;

import static mega.blendtronic.api.numberformatting.Constants.BD_THOUSAND;

import java.math.BigDecimal;

import mega.blendtronic.api.numberformatting.NumberFormatUtil;

@SuppressWarnings("unchecked")
public final class CompactOptions extends NumberOptionsBase<CompactOptions> {

    private BigDecimal compactThreshold = BD_THOUSAND;

    public CompactOptions() {}

    // ---------- Setters ----------

    @Override
    public CompactOptions setExponentialThreshold(Number number) {
        BigDecimal value = NumberFormatUtil.bigDecimalConverter(number);
        if (value.signum() < 0) {
            throw new IllegalArgumentException("Scientific threshold must be >= 0");
        }

        // Prevent conflict with compact threshold.
        // Compact must *always* be lower than the scientific threshold, or configuration becomes inconsistent.
        BigDecimal currentCompact = getCompactThreshold();
        if (value.compareTo(currentCompact) <= 0) {
            throw new IllegalArgumentException(
                    "Scientific threshold must be strictly greater than compact threshold (" + currentCompact + ")");
        }

        super.setExponentialThreshold(value);
        return this;
    }

    public CompactOptions setCompactThreshold(Number number) {
        BigDecimal value = NumberFormatUtil.bigDecimalConverter(number);
        if (value.signum() < 0) {
            throw new IllegalArgumentException("Compact threshold must be >= 0");
        }

        // Prevent conflict with scientific threshold.
        // Compact must *always* be lower than the scientific threshold, or configuration becomes inconsistent.
        BigDecimal currentScientific = getExponentialThreshold();
        if (value.compareTo(currentScientific) >= 0) {
            throw new IllegalArgumentException(
                    "Compact threshold must be strictly less than scientific threshold (" + currentScientific + ")");
        }

        this.compactThreshold = value;
        return this;
    }

    // ---------- Getters ----------

    public BigDecimal getCompactThreshold() {
        return compactThreshold;
    }
}