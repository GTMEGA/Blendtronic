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

package mega.blendtronic.modules.intcache;

import java.util.List;
import java.util.concurrent.Semaphore;

import it.unimi.dsi.fastutil.ints.Int2ObjectFunction;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;

/**
 * Ported over from Hodgepodge, with some extra optimizations on our side
 *
 * @author ah-OOG-ah
 * @author falsepattern
 */
public class NewIntCache {

    private static final int SMALLEST = 256;
    private static final int MIN_LEVEL = 32 - Integer.numberOfLeadingZeros(SMALLEST - 1);

    private static final Int2ObjectMap<ObjectList<int[]>> cachedObjects = new Int2ObjectOpenHashMap<>();

    private static final Semaphore SEMAPHORE = new Semaphore(1);

    @SuppressWarnings("Convert2Lambda") //No lambdas to minimize allocations
    private static final Int2ObjectFunction<ObjectList<int[]>> LIST_CONSTRUCTOR = new Int2ObjectFunction<>() {
        @Override
        public ObjectList<int[]> get(int key) {
            return new ObjectArrayList<>(16);
        }
    };

    public static int[] getCache(int size) {
        // Get the smallest power of two larger than or equal to the number
        final int level = (size <= SMALLEST) ? MIN_LEVEL : 32 - Integer.numberOfLeadingZeros(size - 1);

        while (!SEMAPHORE.tryAcquire()) {
            Thread.yield();
        }
        try {
            final List<int[]> caches = cachedObjects.computeIfAbsent(level, LIST_CONSTRUCTOR);

            if (caches.isEmpty()) {
                return new int[2 << (level - 1)];
            }
            return caches.remove(caches.size() - 1);
        } finally {
            SEMAPHORE.release();
        }
    }

    public static void releaseCache(int[] cache) {
        final int level = (cache.length <= SMALLEST) ? MIN_LEVEL : 32 - Integer.numberOfLeadingZeros(cache.length - 1);
        while (!SEMAPHORE.tryAcquire()) {
            Thread.yield();
        }
        try {
            cachedObjects.computeIfAbsent(level, LIST_CONSTRUCTOR).add(cache);
        } finally {
            SEMAPHORE.release();
        }
    }
}