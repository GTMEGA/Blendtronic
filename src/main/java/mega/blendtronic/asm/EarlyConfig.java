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

package mega.blendtronic.asm;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.launchwrapper.Launch;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Properties;

public class EarlyConfig {
    private static final Logger LOGGER = LogManager.getLogger("BlendtronicEarly");

    public static final boolean optimizeForgeConfigs;

    static {
        Properties config = new Properties();
        File configLocation = new File(Launch.minecraftHome, "config/blendtronicEarly.properties");
        try (Reader r = new BufferedReader(new FileReader(configLocation))) {
            config.load(r);
        } catch (FileNotFoundException e) {
            LOGGER.debug("No existing configuration file. Will use defaults");
        } catch (IOException e) {
            LOGGER.error("Error reading configuration file. Will use defaults", e);
        }
        optimizeForgeConfigs = Boolean.parseBoolean(config.getProperty("optimizeForgeConfigs", "true"));
        config.setProperty("optimizeForgeConfigs", String.valueOf(optimizeForgeConfigs));
        try (Writer r = new BufferedWriter(new FileWriter(configLocation))) {
            config.store(r, "Configuration file for early blendtronic class transformers");
        } catch (IOException e) {
            LOGGER.error("Error reading configuration file. Will use defaults", e);
        }
    }

    public static void ensureLoaded() {}
}
