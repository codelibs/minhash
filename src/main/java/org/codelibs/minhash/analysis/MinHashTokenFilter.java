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
package org.codelibs.minhash.analysis;

import java.io.IOException;
import java.util.Arrays;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.codelibs.minhash.util.FastBitSet;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.io.BaseEncoding;

/**
 * This class is a token filter to calculate MinHash value.
 *
 * @author shinsuke
 *
 */
public class MinHashTokenFilter extends TokenFilter {

    private final CharTermAttribute termAttr = addAttribute(CharTermAttribute.class);

    private final PositionIncrementAttribute posIncrAttr = addAttribute(PositionIncrementAttribute.class);

    private final OffsetAttribute offsetAttr = addAttribute(OffsetAttribute.class);

    private final HashFunction[] hashFunctions;

    private final int hashBit;

    private final long[] minHashValues;

    private String minHash;

    public MinHashTokenFilter(final TokenStream input, final HashFunction[] hashFunctions, final int hashBit) {
        super(input);
        this.hashFunctions = hashFunctions;
        this.hashBit = hashBit;
        minHashValues = new long[hashFunctions.length];
    }

    @Override
    public final boolean incrementToken() throws IOException {
        final int funcSize = hashFunctions.length;
        while (input.incrementToken()) {
            final String term = termAttr.toString();
            for (int i = 0; i < funcSize; i++) {
                final HashCode hashCode = hashFunctions[i].hashUnencodedChars(term);
                final long value = hashCode.asLong();
                if (value < minHashValues[i]) {
                    minHashValues[i] = value;
                }
            }
        }

        if (minHash != null) {
            return false;
        }

        minHash = BaseEncoding.base64().encode(calcMinHash(minHashValues, hashBit));
        termAttr.setEmpty().append(minHash);
        posIncrAttr.setPositionIncrement(0);
        offsetAttr.setOffset(0, minHash.length());

        return true;
    }

    @Override
    public void reset() throws IOException {
        super.reset();
        Arrays.fill(minHashValues, Long.MAX_VALUE);
        minHash = null;
    }

    protected static byte[] calcMinHash(final long[] minHashValues, final int hashBit) {
        final int shift = 1;
        final int radix = 1 << shift;
        final long mask = radix - 1;
        int pos = 0;
        final int nbits = minHashValues.length * hashBit;
        final FastBitSet bitSet = new FastBitSet(nbits);
        for (long i : minHashValues) {
            for (int j = 0; j < hashBit; j++) {
                bitSet.set(pos, (int) (i & mask) == 1);
                pos++;
                i >>>= shift;
            }
        }
        return bitSet.toByteArray();
    }

}
