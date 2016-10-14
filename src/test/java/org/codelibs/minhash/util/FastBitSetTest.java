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
