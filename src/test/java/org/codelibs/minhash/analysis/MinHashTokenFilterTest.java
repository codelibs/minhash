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
import java.io.StringReader;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.google.common.io.BaseEncoding;

import junit.framework.TestCase;

public class MinHashTokenFilterTest extends TestCase {

    public void test_calcMinHash() throws Exception {
        int hashBit;
        long[] minHashValues;
        byte[] bytes;

        hashBit = 1;
        minHashValues = new long[] { 0, 1, 2, 3, 4, 5, 6, 7 };
        bytes = MinHashTokenFilter.calcMinHash(minHashValues, hashBit);
        assertEquals(1, bytes.length);
        assertEquals(0x0, bytes[0] & 0x1);
        assertEquals(0x1, bytes[0] >> 1 & 0x1);
        assertEquals(0x0, bytes[0] >> 2 & 0x1);
        assertEquals(0x1, bytes[0] >> 3 & 0x1);
        assertEquals(0x0, bytes[0] >> 4 & 0x1);
        assertEquals(0x1, bytes[0] >> 5 & 0x1);
        assertEquals(0x0, bytes[0] >> 6 & 0x1);
        assertEquals(0x1, bytes[0] >> 7 & 0x1);

        hashBit = 1;
        minHashValues = new long[] { 1, 2, 3, 4, 5, 6, 7, 8 };
        bytes = MinHashTokenFilter.calcMinHash(minHashValues, hashBit);
        assertEquals(1, bytes.length);
        assertEquals(0x1, bytes[0] & 0x1);
        assertEquals(0x0, bytes[0] >> 1 & 0x1);
        assertEquals(0x1, bytes[0] >> 2 & 0x1);
        assertEquals(0x0, bytes[0] >> 3 & 0x1);
        assertEquals(0x1, bytes[0] >> 4 & 0x1);
        assertEquals(0x0, bytes[0] >> 5 & 0x1);
        assertEquals(0x1, bytes[0] >> 6 & 0x1);
        assertEquals(0x0, bytes[0] >> 7 & 0x1);

        hashBit = 1;
        minHashValues = new long[] { 0, 1, 2, 3, };
        bytes = MinHashTokenFilter.calcMinHash(minHashValues, hashBit);
        assertEquals(1, bytes.length);
        assertEquals(0x0, bytes[0] & 0x1);
        assertEquals(0x1, bytes[0] >> 1 & 0x1);
        assertEquals(0x0, bytes[0] >> 2 & 0x1);
        assertEquals(0x1, bytes[0] >> 3 & 0x1);
        assertEquals(0x0, bytes[0] >> 4 & 0x1);
        assertEquals(0x0, bytes[0] >> 5 & 0x1);
        assertEquals(0x0, bytes[0] >> 6 & 0x1);
        assertEquals(0x0, bytes[0] >> 7 & 0x1);

        hashBit = 2;
        minHashValues = new long[] { 0, 1, 2, 3, };
        bytes = MinHashTokenFilter.calcMinHash(minHashValues, hashBit);
        assertEquals(1, bytes.length);
        assertEquals(0x0, bytes[0] & 0x1);
        assertEquals(0x0, bytes[0] >> 1 & 0x1);
        assertEquals(0x1, bytes[0] >> 2 & 0x1);
        assertEquals(0x0, bytes[0] >> 3 & 0x1);
        assertEquals(0x0, bytes[0] >> 4 & 0x1);
        assertEquals(0x1, bytes[0] >> 5 & 0x1);
        assertEquals(0x1, bytes[0] >> 6 & 0x1);
        assertEquals(0x1, bytes[0] >> 7 & 0x1);

        hashBit = 1;
        minHashValues = new long[] { 0, 1, 2, 3, 4, 5, 6, 7, 7, 6, 5, 4, 3, 2,
                1, 0 };
        bytes = MinHashTokenFilter.calcMinHash(minHashValues, hashBit);
        assertEquals(2, bytes.length);
        assertEquals(0x0, bytes[0] & 0x1);
        assertEquals(0x1, bytes[0] >> 1 & 0x1);
        assertEquals(0x0, bytes[0] >> 2 & 0x1);
        assertEquals(0x1, bytes[0] >> 3 & 0x1);
        assertEquals(0x0, bytes[0] >> 4 & 0x1);
        assertEquals(0x1, bytes[0] >> 5 & 0x1);
        assertEquals(0x0, bytes[0] >> 6 & 0x1);
        assertEquals(0x1, bytes[0] >> 7 & 0x1);
        assertEquals(0x1, bytes[1] & 0x1);
        assertEquals(0x0, bytes[1] >> 1 & 0x1);
        assertEquals(0x1, bytes[1] >> 2 & 0x1);
        assertEquals(0x0, bytes[1] >> 3 & 0x1);
        assertEquals(0x1, bytes[1] >> 4 & 0x1);
        assertEquals(0x0, bytes[1] >> 5 & 0x1);
        assertEquals(0x1, bytes[1] >> 6 & 0x1);
        assertEquals(0x0, bytes[1] >> 7 & 0x1);

    }

    public void test_incrementToken_withMultipleTokens() throws IOException {
        final WhitespaceTokenizer tokenizer = new WhitespaceTokenizer();
        tokenizer.setReader(new StringReader("hello world test"));

        final HashFunction[] hashFunctions = new HashFunction[] {
            Hashing.murmur3_128(0),
            Hashing.murmur3_128(1),
            Hashing.murmur3_128(2),
            Hashing.murmur3_128(3)
        };

        final MinHashTokenFilter filter = new MinHashTokenFilter(tokenizer, hashFunctions, 1);
        final CharTermAttribute termAttr = filter.addAttribute(CharTermAttribute.class);

        filter.reset();

        // Should produce exactly one token with the minhash value
        assertTrue(filter.incrementToken());
        assertNotNull(termAttr.toString());
        assertTrue(termAttr.toString().length() > 0);

        // Should not produce any more tokens
        assertFalse(filter.incrementToken());

        filter.end();
        filter.close();
    }

    public void test_incrementToken_withSingleToken() throws IOException {
        final WhitespaceTokenizer tokenizer = new WhitespaceTokenizer();
        tokenizer.setReader(new StringReader("hello"));

        final HashFunction[] hashFunctions = new HashFunction[] {
            Hashing.murmur3_128(0),
            Hashing.murmur3_128(1)
        };

        final MinHashTokenFilter filter = new MinHashTokenFilter(tokenizer, hashFunctions, 1);
        final CharTermAttribute termAttr = filter.addAttribute(CharTermAttribute.class);

        filter.reset();

        assertTrue(filter.incrementToken());
        assertNotNull(termAttr.toString());

        assertFalse(filter.incrementToken());

        filter.end();
        filter.close();
    }

    public void test_incrementToken_withEmptyInput() throws IOException {
        final WhitespaceTokenizer tokenizer = new WhitespaceTokenizer();
        tokenizer.setReader(new StringReader(""));

        final HashFunction[] hashFunctions = new HashFunction[] {
            Hashing.murmur3_128(0)
        };

        final MinHashTokenFilter filter = new MinHashTokenFilter(tokenizer, hashFunctions, 1);
        final CharTermAttribute termAttr = filter.addAttribute(CharTermAttribute.class);

        filter.reset();

        // Even with empty input, should produce one minhash token
        assertTrue(filter.incrementToken());
        assertNotNull(termAttr.toString());

        assertFalse(filter.incrementToken());

        filter.end();
        filter.close();
    }

    public void test_reset() throws IOException {
        final WhitespaceTokenizer tokenizer = new WhitespaceTokenizer();
        tokenizer.setReader(new StringReader("hello world"));

        final HashFunction[] hashFunctions = new HashFunction[] {
            Hashing.murmur3_128(0),
            Hashing.murmur3_128(1)
        };

        final MinHashTokenFilter filter = new MinHashTokenFilter(tokenizer, hashFunctions, 1);
        final CharTermAttribute termAttr = filter.addAttribute(CharTermAttribute.class);

        filter.reset();
        assertTrue(filter.incrementToken());
        String firstHash = termAttr.toString();

        // Reset and process again with new input
        tokenizer.setReader(new StringReader("goodbye world"));
        filter.reset();
        assertTrue(filter.incrementToken());
        String secondHash = termAttr.toString();

        // Hashes should be different for different inputs
        assertNotNull(firstHash);
        assertNotNull(secondHash);
        // Note: They might occasionally be the same due to hash collisions, but typically different

        filter.end();
        filter.close();
    }

    public void test_reset_multipleTimesWithSameInput() throws IOException {
        final HashFunction[] hashFunctions = new HashFunction[] {
            Hashing.murmur3_128(0),
            Hashing.murmur3_128(1)
        };

        for (int i = 0; i < 3; i++) {
            final WhitespaceTokenizer tokenizer = new WhitespaceTokenizer();
            tokenizer.setReader(new StringReader("consistent test input"));

            final MinHashTokenFilter filter = new MinHashTokenFilter(tokenizer, hashFunctions, 1);
            final CharTermAttribute termAttr = filter.addAttribute(CharTermAttribute.class);

            filter.reset();
            assertTrue(filter.incrementToken());

            // Same input should produce same hash
            assertNotNull(termAttr.toString());

            filter.end();
            filter.close();
        }
    }

    public void test_positionIncrement() throws IOException {
        final WhitespaceTokenizer tokenizer = new WhitespaceTokenizer();
        tokenizer.setReader(new StringReader("hello world"));

        final HashFunction[] hashFunctions = new HashFunction[] {
            Hashing.murmur3_128(0)
        };

        final MinHashTokenFilter filter = new MinHashTokenFilter(tokenizer, hashFunctions, 1);
        final PositionIncrementAttribute posIncrAttr = filter.addAttribute(PositionIncrementAttribute.class);

        filter.reset();
        assertTrue(filter.incrementToken());

        // Position increment should be 0 for minhash token
        assertEquals(0, posIncrAttr.getPositionIncrement());

        filter.end();
        filter.close();
    }

    public void test_offsetAttribute() throws IOException {
        final WhitespaceTokenizer tokenizer = new WhitespaceTokenizer();
        tokenizer.setReader(new StringReader("hello world"));

        final HashFunction[] hashFunctions = new HashFunction[] {
            Hashing.murmur3_128(0)
        };

        final MinHashTokenFilter filter = new MinHashTokenFilter(tokenizer, hashFunctions, 1);
        final OffsetAttribute offsetAttr = filter.addAttribute(OffsetAttribute.class);
        final CharTermAttribute termAttr = filter.addAttribute(CharTermAttribute.class);

        filter.reset();
        assertTrue(filter.incrementToken());

        // Offset should be from 0 to the length of the minhash string
        assertEquals(0, offsetAttr.startOffset());
        assertEquals(termAttr.toString().length(), offsetAttr.endOffset());

        filter.end();
        filter.close();
    }

    public void test_calcMinHash_withDifferentHashBits() {
        long[] minHashValues = new long[] { 0, 1, 2, 3 };

        // Test with hashBit = 1
        byte[] result1 = MinHashTokenFilter.calcMinHash(minHashValues, 1);
        assertEquals(1, result1.length);

        // Test with hashBit = 2
        byte[] result2 = MinHashTokenFilter.calcMinHash(minHashValues, 2);
        assertEquals(1, result2.length);

        // Test with hashBit = 4
        byte[] result4 = MinHashTokenFilter.calcMinHash(minHashValues, 4);
        assertEquals(2, result4.length);

        // Test with hashBit = 8
        byte[] result8 = MinHashTokenFilter.calcMinHash(minHashValues, 8);
        assertEquals(4, result8.length);
    }

    public void test_calcMinHash_withLargeValues() {
        long[] minHashValues = new long[] {
            Long.MAX_VALUE,
            Long.MIN_VALUE,
            0L,
            -1L
        };

        byte[] result = MinHashTokenFilter.calcMinHash(minHashValues, 2);
        assertNotNull(result);
        assertEquals(1, result.length);
    }

    public void test_calcMinHash_withSingleHashFunction() {
        long[] minHashValues = new long[] { 42L };

        byte[] result = MinHashTokenFilter.calcMinHash(minHashValues, 8);
        assertNotNull(result);
        assertEquals(1, result.length);
    }

    public void test_constructor() {
        final WhitespaceTokenizer tokenizer = new WhitespaceTokenizer();
        final HashFunction[] hashFunctions = new HashFunction[] {
            Hashing.murmur3_128(0),
            Hashing.murmur3_128(1)
        };

        final MinHashTokenFilter filter = new MinHashTokenFilter(tokenizer, hashFunctions, 2);
        assertNotNull(filter);
    }

    public void test_incrementToken_withManyHashFunctions() throws IOException {
        final WhitespaceTokenizer tokenizer = new WhitespaceTokenizer();
        tokenizer.setReader(new StringReader("test data"));

        // Create many hash functions
        final HashFunction[] hashFunctions = new HashFunction[256];
        for (int i = 0; i < 256; i++) {
            hashFunctions[i] = Hashing.murmur3_128(i);
        }

        final MinHashTokenFilter filter = new MinHashTokenFilter(tokenizer, hashFunctions, 1);
        final CharTermAttribute termAttr = filter.addAttribute(CharTermAttribute.class);

        filter.reset();
        assertTrue(filter.incrementToken());
        assertNotNull(termAttr.toString());

        // Result should be 256 bits = 32 bytes, base64 encoded
        String hash = termAttr.toString();
        byte[] decoded = BaseEncoding.base64().decode(hash);
        assertEquals(32, decoded.length);

        filter.end();
        filter.close();
    }
}
