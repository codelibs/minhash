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

    public void test_set_multipleBits() {
        FastBitSet bitSet = new FastBitSet(16);

        // Set multiple bits
        bitSet.set(0, true);
        bitSet.set(3, true);
        bitSet.set(7, true);
        bitSet.set(8, true);
        bitSet.set(15, true);

        byte[] data = bitSet.toByteArray();
        assertEquals(2, data.length);

        // Check individual bits
        assertEquals(0x1, data[0] & 0x1);       // bit 0
        assertEquals(0x8, data[0] & 0x8);       // bit 3
        assertEquals(0x80, data[0] & 0x80);     // bit 7
        assertEquals(0x1, data[1] & 0x1);       // bit 8
        assertEquals(0x80, data[1] & 0x80);     // bit 15
    }

    public void test_set_toggleBits() {
        FastBitSet bitSet = new FastBitSet(8);

        // Set a bit to true
        bitSet.set(3, true);
        assertEquals(0x8, bitSet.toByteArray()[0]);

        // Set the same bit to false
        bitSet.set(3, false);
        assertEquals(0x0, bitSet.toByteArray()[0]);

        // Set it to true again
        bitSet.set(3, true);
        assertEquals(0x8, bitSet.toByteArray()[0]);
    }

    public void test_set_outOfBounds() {
        FastBitSet bitSet = new FastBitSet(8);

        // Setting a bit beyond the array size should not throw exception
        // but should be ignored (based on the implementation)
        bitSet.set(100, true);

        // Verify no exception is thrown and data is unchanged
        byte[] data = bitSet.toByteArray();
        assertEquals(1, data.length);
        assertEquals(0x0, data[0]);
    }

    public void test_constructor_largeBitCount() {
        // Test with large bit count
        FastBitSet bitSet = new FastBitSet(1024);
        assertEquals(1024, bitSet.nbit);
        assertEquals(128, bitSet.data.length);  // 1024 / 8 = 128

        // Set some bits
        bitSet.set(0, true);
        bitSet.set(1023, true);

        byte[] data = bitSet.toByteArray();
        assertEquals(0x1, data[0] & 0x1);           // first bit
        assertEquals(0x80, data[127] & 0x80);       // last bit
    }

    public void test_set_allBitsInByte() {
        FastBitSet bitSet = new FastBitSet(8);

        // Set all bits to true
        for (int i = 0; i < 8; i++) {
            bitSet.set(i, true);
        }

        byte[] data = bitSet.toByteArray();
        assertEquals(1, data.length);
        assertEquals((byte) 0xff, data[0]);
    }

    public void test_set_alternatingBits() {
        FastBitSet bitSet = new FastBitSet(8);

        // Set alternating bits (0, 2, 4, 6)
        bitSet.set(0, true);
        bitSet.set(2, true);
        bitSet.set(4, true);
        bitSet.set(6, true);

        byte[] data = bitSet.toByteArray();
        assertEquals(1, data.length);
        assertEquals(0x55, data[0] & 0xff);  // 01010101 in binary
    }

    public void test_toByteArray_returnsInternalArray() {
        FastBitSet bitSet = new FastBitSet(8);
        bitSet.set(0, true);

        byte[] data1 = bitSet.toByteArray();
        byte[] data2 = bitSet.toByteArray();

        // Should return the same internal array
        assertSame(data1, data2);
    }

    public void test_constructor_boundaryValues() {
        // Test with bit counts at byte boundaries
        FastBitSet bitSet1 = new FastBitSet(1);
        assertEquals(1, bitSet1.data.length);

        FastBitSet bitSet7 = new FastBitSet(7);
        assertEquals(1, bitSet7.data.length);

        FastBitSet bitSet8 = new FastBitSet(8);
        assertEquals(1, bitSet8.data.length);

        FastBitSet bitSet9 = new FastBitSet(9);
        assertEquals(2, bitSet9.data.length);

        FastBitSet bitSet16 = new FastBitSet(16);
        assertEquals(2, bitSet16.data.length);

        FastBitSet bitSet17 = new FastBitSet(17);
        assertEquals(3, bitSet17.data.length);
    }

    public void test_set_samePosition_multipleValues() {
        FastBitSet bitSet = new FastBitSet(8);

        // Set bit 5 to true
        bitSet.set(5, true);
        assertEquals(0x20, bitSet.toByteArray()[0]);

        // Set bit 5 to false
        bitSet.set(5, false);
        assertEquals(0x0, bitSet.toByteArray()[0]);

        // Set bit 5 to true again
        bitSet.set(5, true);
        assertEquals(0x20, bitSet.toByteArray()[0]);

        // Set bit 5 to false again
        bitSet.set(5, false);
        assertEquals(0x0, bitSet.toByteArray()[0]);
    }

    public void test_set_multipleBytes() {
        FastBitSet bitSet = new FastBitSet(24);

        // Set bits in different bytes
        bitSet.set(0, true);   // first byte, bit 0
        bitSet.set(8, true);   // second byte, bit 0
        bitSet.set(16, true);  // third byte, bit 0

        byte[] data = bitSet.toByteArray();
        assertEquals(3, data.length);
        assertEquals(0x1, data[0]);
        assertEquals(0x1, data[1]);
        assertEquals(0x1, data[2]);
    }

    public void test_set_complexPattern() {
        FastBitSet bitSet = new FastBitSet(32);

        // Create a specific pattern: 10101010 11110000 00001111 11111111
        for (int i = 0; i < 8; i++) {
            bitSet.set(i, i % 2 == 0);  // 10101010
        }
        for (int i = 8; i < 12; i++) {
            bitSet.set(i, false);  // 11110000 (first half)
        }
        for (int i = 12; i < 16; i++) {
            bitSet.set(i, true);  // 11110000 (second half)
        }
        for (int i = 16; i < 20; i++) {
            bitSet.set(i, true);  // 00001111 (first half)
        }
        for (int i = 20; i < 24; i++) {
            bitSet.set(i, false);  // 00001111 (second half)
        }
        for (int i = 24; i < 32; i++) {
            bitSet.set(i, true);  // 11111111
        }

        byte[] data = bitSet.toByteArray();
        assertEquals(4, data.length);
    }

    public void test_nbit_field() {
        FastBitSet bitSet1 = new FastBitSet(10);
        assertEquals(10, bitSet1.nbit);

        FastBitSet bitSet2 = new FastBitSet(100);
        assertEquals(100, bitSet2.nbit);

        FastBitSet bitSet3 = new FastBitSet(1);
        assertEquals(1, bitSet3.nbit);
    }
}
