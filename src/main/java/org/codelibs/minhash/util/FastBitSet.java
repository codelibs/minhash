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

/**
 * Fast bit set implementation for efficient bit manipulation.
 * This class provides a lightweight alternative to {@link java.util.BitSet}
 * with minimal overhead for fixed-size bit arrays.
 *
 * @author shinsuke
 *
 */
public class FastBitSet {
    final byte[] data;

    final int nbit;

    public FastBitSet(final int nbit) {
        this.nbit = nbit;
        if (nbit == 0) {
            throw new IllegalArgumentException("nbit is above 0.");
        }

        data = new byte[(nbit - 1) / 8 + 1];
    }

    public void set(final int bitIndex, final boolean value) {
        final int bytePos = bitIndex / 8;
        final int bitPos = bitIndex % 8;

        if (bytePos >= data.length) {
            return;
        }

        final int mask = 1 << bitPos;
        if (value) {
            data[bytePos] = (byte) (data[bytePos] | mask);
        } else {
            data[bytePos] = (byte) (data[bytePos] & ~mask);
        }
    }

    public byte[] toByteArray() {
        return data;
    }
}
