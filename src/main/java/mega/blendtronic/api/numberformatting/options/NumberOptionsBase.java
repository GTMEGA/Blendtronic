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

import static mega.blendtronic.api.numberformatting.Constants.BD_TRILLION;

import java.math.BigDecimal;
import java.math.RoundingMode;

import mega.blendtronic.api.numberformatting.NumberFormatUtil;

@SuppressWarnings({ "unchecked", "unused" })
public abstract class NumberOptionsBase<T extends NumberOptionsBase<T>> {

    private int decimalPlaces = 2;

    private BigDecimal exponentialThreshold = BD_TRILLION;
    private boolean disableExponentialFormatting = false;
    private RoundingMode roundingMode = RoundingMode.HALF_UP;

    protected NumberOptionsBase() {}

    // ---------- Setters ----------

    public T setDecimalPlaces(int decimalPlaces) {
        if (decimalPlaces < 0) {
            throw new IllegalArgumentException("Decimal places must be >= 0");
        }
        this.decimalPlaces = decimalPlaces;
        return (T) this;
    }

    public T setExponentialThreshold(Number number) {
        if (number == null) {
            throw new IllegalArgumentException("Number cannot be null");
        }
        BigDecimal value = NumberFormatUtil.bigDecimalConverter(number);
        if (value.signum() < 0) {
            throw new IllegalArgumentException("Exponential threshold must be >= 0");
        }
        this.exponentialThreshold = value.abs();
        return (T) this;
    }

    public T disableExponentialFormatting() {
        this.disableExponentialFormatting = true;
        return (T) this;
    }

    public T enableExponentialFormatting() {
        this.disableExponentialFormatting = false;
        return (T) this;
    }

    public T setRoundingMode(RoundingMode roundingMode) {
        this.roundingMode = roundingMode;
        return (T) this;
    }

    // ---------- Getters ----------

    public int getDecimalPlaces() {
        return decimalPlaces;
    }

    public BigDecimal getExponentialThreshold() {
        return exponentialThreshold;
    }

    public boolean isExponentialFormattingDisabled() {
        return disableExponentialFormatting == true;
    }

    public boolean isExponentialFormattingEnabled() {
        return disableExponentialFormatting == false;
    }

    public RoundingMode getRoundingMode() {
        return roundingMode;
    }

    // ---------- Converters ----------

    public FormatOptions toFormatOptions() {
        FormatOptions options = new FormatOptions();
        options.setExponentialThreshold(exponentialThreshold);
        options.setDecimalPlaces(decimalPlaces);
        options.setRoundingMode(roundingMode);

        if (disableExponentialFormatting) {
            options.disableExponentialFormatting();
        } else {
            options.enableExponentialFormatting();
        }

        return options;
    }

    public CompactOptions toCompactOptions() {
        CompactOptions options = new CompactOptions();
        options.setExponentialThreshold(exponentialThreshold);
        options.setDecimalPlaces(decimalPlaces);
        options.setRoundingMode(roundingMode);

        if (disableExponentialFormatting) {
            options.disableExponentialFormatting();
        } else {
            options.enableExponentialFormatting();
        }

        return options;
    }

}