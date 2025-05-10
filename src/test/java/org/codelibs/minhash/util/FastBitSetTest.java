/*
 * Copyright 2012-2025 CodeLibs Project and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.codelibs.minhash.util;

import junit.framework.TestCase;

public class FastBitSetTest extends TestCase {
    public void test_set() {
        FastBitSet bitSet;

        try {
            new FastBitSet(0);
            fail();
        } catch (IllegalArgumentException e) {
            // ignore
        }

        byte data = 1;
        for (int i = 0; i < 8; i++) {
            bitSet = new FastBitSet(i + 1);
            assertEquals(i + 1, bitSet.nbit);
            assertEquals(1, bitSet.data.length);
            bitSet.set(i, true);
            assertEquals(data, bitSet.toByteArray()[0]);
            bitSet.set(i, false);
            assertEquals(0x0, bitSet.toByteArray()[0]);
            data <<= 1;
        }

        data = 1;
        for (int i = 8; i < 16; i++) {
            bitSet = new FastBitSet(i + 1);
            assertEquals(i + 1, bitSet.nbit);
            assertEquals(2, bitSet.data.length);
            bitSet.set(i, true);
            assertEquals(0x0, bitSet.toByteArray()[0]);
            assertEquals(data, bitSet.toByteArray()[1]);
            bitSet.set(i, false);
            assertEquals(0x0, bitSet.toByteArray()[0]);
            assertEquals(0x0, bitSet.toByteArray()[1]);
            data <<= 1;
        }

        data = 1;
        for (int i = 16; i < 24; i++) {
            bitSet = new FastBitSet(i + 1);
            assertEquals(i + 1, bitSet.nbit);
            assertEquals(3, bitSet.data.length);
            bitSet.set(i, true);
            assertEquals(0x0, bitSet.toByteArray()[0]);
            assertEquals(0x0, bitSet.toByteArray()[1]);
            assertEquals(data, bitSet.toByteArray()[2]);
            bitSet.set(i, false);
            assertEquals(0x0, bitSet.toByteArray()[0]);
            assertEquals(0x0, bitSet.toByteArray()[1]);
            assertEquals(0x0, bitSet.toByteArray()[2]);
            data <<= 1;
        }
    }
}
