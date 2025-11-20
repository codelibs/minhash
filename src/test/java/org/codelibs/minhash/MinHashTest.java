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
package org.codelibs.minhash;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;

import junit.framework.TestCase;

public class MinHashTest extends TestCase {

    public void test_usercase1() throws IOException {
        String text = "Fess is very powerful and easily deployable Enterprise Search Server.";

        // The number of bits for each hash value.
        int hashBit = 1;
        // A base seed for hash functions.
        int seed = 0;
        // The number of hash functions.
        int num = 128;
        // Analyzer for 1-bit 128 hash.
        Analyzer analyzer = MinHash.createAnalyzer(hashBit, seed,
                num);

        // Calculate a minhash value. The size is hashBit*num.
        byte[] minhash = MinHash.calculate(analyzer, text);

        assertEquals(
                "00101010000100110011000101100000001101011101111010100010111101101000000011100010100100001111110110011101111101001010001110010101",
                MinHash.toBinaryString(minhash));

        // Compare a similar text.
        String text1 = "Fess is very powerful and easily deployable Search Server.";
        byte[] minhash1 = MinHash.calculate(analyzer, text1);
        assertEquals(0.953125f, MinHash.compare(minhash, minhash1));

        // Compare a different text.
        String text2 = "Solr is the popular, blazing fast open source enterprise search platform";
        byte[] minhash2 = MinHash.calculate(analyzer, text2);
        assertEquals(0.453125f, MinHash.compare(minhash, minhash2));

    }

    public void test_calculate_1bit_128funcs_seed0() throws IOException {

        final int hashBit = 1;
        final int seed = 0;
        final int num = 128;
        final Analyzer minhashAnalyzer = MinHash.createAnalyzer(hashBit,
                seed, num);
        final StringBuilder[] texts = createTexts();
        final byte[][] data = createMinHashes(minhashAnalyzer, texts);

        assertEquals(1.0f, MinHash.compare(data[0], data[0]));
        assertEquals(0.890625f, MinHash.compare(data[0], data[1]));
        assertEquals(0.7890625f, MinHash.compare(data[0], data[2]));
        assertEquals(0.7421875f, MinHash.compare(data[0], data[3]));
        assertEquals(0.6953125f, MinHash.compare(data[0], data[4]));
        assertEquals(0.609375f, MinHash.compare(data[0], data[5]));
        assertEquals(0.578125f, MinHash.compare(data[0], data[6]));
        assertEquals(0.546875f, MinHash.compare(data[0], data[7]));
        assertEquals(0.546875f, MinHash.compare(data[0], data[8]));
        assertEquals(0.5625f, MinHash.compare(data[0], data[9]));
    }

    public void test_calculate_1bit_128funcs_seed100() throws IOException {

        final int hashBit = 1;
        final int seed = 100;
        final int num = 128;
        final Analyzer minhashAnalyzer = MinHash.createAnalyzer(hashBit,
                seed, num);
        final StringBuilder[] texts = createTexts();
        final byte[][] data = createMinHashes(minhashAnalyzer, texts);

        assertEquals(1.0f, MinHash.compare(data[0], data[0]));
        assertEquals(0.9296875f, MinHash.compare(data[0], data[1]));
        assertEquals(0.8515625f, MinHash.compare(data[0], data[2]));
        assertEquals(0.8046875f, MinHash.compare(data[0], data[3]));
        assertEquals(0.7265625f, MinHash.compare(data[0], data[4]));
        assertEquals(0.640625f, MinHash.compare(data[0], data[5]));
        assertEquals(0.640625f, MinHash.compare(data[0], data[6]));
        assertEquals(0.5703125f, MinHash.compare(data[0], data[7]));
        assertEquals(0.53125f, MinHash.compare(data[0], data[8]));
        assertEquals(0.484375f, MinHash.compare(data[0], data[9]));
    }

    public void test_calculate_2bit_128funcs_seed0() throws IOException {

        final int hashBit = 2;
        final int seed = 0;
        final int num = 128;
        final Analyzer minhashAnalyzer = MinHash.createAnalyzer(hashBit,
                seed, num);
        final StringBuilder[] texts = createTexts();
        final byte[][] data = createMinHashes(minhashAnalyzer, texts);

        assertEquals(1.0f, MinHash.compare(data[0], data[0]));
        assertEquals(0.89453125f, MinHash.compare(data[0], data[1]));
        assertEquals(0.80859375f, MinHash.compare(data[0], data[2]));
        assertEquals(0.7734375f, MinHash.compare(data[0], data[3]));
        assertEquals(0.7265625f, MinHash.compare(data[0], data[4]));
        assertEquals(0.66015625f, MinHash.compare(data[0], data[5]));
        assertEquals(0.625f, MinHash.compare(data[0], data[6]));
        assertEquals(0.59765625f, MinHash.compare(data[0], data[7]));
        assertEquals(0.5859375f, MinHash.compare(data[0], data[8]));
        assertEquals(0.55078125f, MinHash.compare(data[0], data[9]));
    }

    public void test_calculate_1bit_256funcs_seed0() throws IOException {

        final int hashBit = 1;
        final int seed = 0;
        final int num = 256;
        final Analyzer minhashAnalyzer = MinHash.createAnalyzer(hashBit,
                seed, num);
        final StringBuilder[] texts = createTexts();
        final byte[][] data = createMinHashes(minhashAnalyzer, texts);

        assertEquals(1.0f, MinHash.compare(data[0], data[0]));
        assertEquals(0.90625f, MinHash.compare(data[0], data[1]));
        assertEquals(0.82421875f, MinHash.compare(data[0], data[2]));
        assertEquals(0.76953125f, MinHash.compare(data[0], data[3]));
        assertEquals(0.703125f, MinHash.compare(data[0], data[4]));
        assertEquals(0.625f, MinHash.compare(data[0], data[5]));
        assertEquals(0.6015625f, MinHash.compare(data[0], data[6]));
        assertEquals(0.55078125f, MinHash.compare(data[0], data[7]));
        assertEquals(0.53125f, MinHash.compare(data[0], data[8]));
        assertEquals(0.51171875f, MinHash.compare(data[0], data[9]));
    }

    public void test_calculate_1bit_128funcs_seed0_moreSimilar()
            throws IOException {

        final int hashBit = 1;
        final int seed = 0;
        final int num = 128;
        final Analyzer minhashAnalyzer = MinHash.createAnalyzer(hashBit,
                seed, num);
        final StringBuilder[] texts = createMoreSimilarTexts();
        final byte[][] data = createMinHashes(minhashAnalyzer, texts);

        assertEquals(1.0f, MinHash.compare(data[0], data[0]));
        assertEquals(0.984375f, MinHash.compare(data[0], data[1]));
        assertEquals(0.9765625f, MinHash.compare(data[0], data[2]));
        assertEquals(0.96875f, MinHash.compare(data[0], data[3]));
        assertEquals(0.953125f, MinHash.compare(data[0], data[4]));
        assertEquals(0.9375f, MinHash.compare(data[0], data[5]));
        assertEquals(0.9296875f, MinHash.compare(data[0], data[6]));
        assertEquals(0.921875f, MinHash.compare(data[0], data[7]));
        assertEquals(0.921875f, MinHash.compare(data[0], data[8]));
        assertEquals(0.921875f, MinHash.compare(data[0], data[9]));
    }

    private byte[][] createMinHashes(final Analyzer minhashAnalyzer,
            final StringBuilder[] texts) throws IOException {
        final byte[][] data = new byte[10][];
        for (int i = 0; i < 10; i++) {
            // System.out.println("texts" + i + ": " + texts[i]);
            data[i] = MinHash.calculate(minhashAnalyzer, texts[i].toString());
        }
        return data;
    }

    private StringBuilder[] createTexts() {
        final StringBuilder[] texts = new StringBuilder[10];
        for (int i = 0; i < 10; i++) {
            texts[i] = new StringBuilder();
        }
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 10; j++) {
                if (i - j * 10 >= 0) {
                    texts[j].append(" aaa" + i);
                } else {
                    texts[j].append(" bbb" + i);
                }
            }
        }
        return texts;
    }

    private StringBuilder[] createMoreSimilarTexts() {
        final StringBuilder[] texts = new StringBuilder[10];
        for (int i = 0; i < 10; i++) {
            texts[i] = new StringBuilder();
        }
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 10; j++) {
                if (i - 90 - j >= 0) {
                    texts[j].append(" aaa" + i);
                } else {
                    texts[j].append(" bbb" + i);
                }
            }
        }
        return texts;
    }

    public void test_compare() {
        assertEquals(1f,
                MinHash.compare(new byte[] { 0x1 }, new byte[] { 0x1 }));
        assertEquals(1f, MinHash.compare(new byte[] { 0x1, 0x1 }, new byte[] {
                0x1, 0x1 }));

        assertEquals(0.5f,
                MinHash.compare(new byte[] { 0xf }, new byte[] { 0x0 }));
        assertEquals(0.5f, MinHash.compare(new byte[] { 0xf, 0x0 }, new byte[] {
                0x0, 0xf }));

        assertEquals(0.0f,
                MinHash.compare(new byte[] { 0xf }, new byte[] { (byte) 0xf0 }));
        assertEquals(
                0.0f,
                MinHash.compare(new byte[] { 0xf, (byte) 0xf0 }, new byte[] {
                        (byte) 0xf0, 0xf }));
    }

    public void test_countSameBits() {
        assertEquals(8,
                MinHash.countSameBits(new byte[] { 0x0 }, new byte[] { 0x0 }));
        assertEquals(7,
                MinHash.countSameBits(new byte[] { 0x1 }, new byte[] { 0x0 }));
        assertEquals(7,
                MinHash.countSameBits(new byte[] { 0x2 }, new byte[] { 0x0 }));
        assertEquals(6,
                MinHash.countSameBits(new byte[] { 0x3 }, new byte[] { 0x0 }));
        assertEquals(7,
                MinHash.countSameBits(new byte[] { 0x4 }, new byte[] { 0x0 }));
        assertEquals(6,
                MinHash.countSameBits(new byte[] { 0x5 }, new byte[] { 0x0 }));
        assertEquals(6,
                MinHash.countSameBits(new byte[] { 0x6 }, new byte[] { 0x0 }));
        assertEquals(5,
                MinHash.countSameBits(new byte[] { 0x7 }, new byte[] { 0x0 }));
        assertEquals(7,
                MinHash.countSameBits(new byte[] { 0x8 }, new byte[] { 0x0 }));
        assertEquals(6,
                MinHash.countSameBits(new byte[] { 0x9 }, new byte[] { 0x0 }));
        assertEquals(6,
                MinHash.countSameBits(new byte[] { 0xa }, new byte[] { 0x0 }));
        assertEquals(5,
                MinHash.countSameBits(new byte[] { 0xb }, new byte[] { 0x0 }));
        assertEquals(6,
                MinHash.countSameBits(new byte[] { 0xc }, new byte[] { 0x0 }));
        assertEquals(5,
                MinHash.countSameBits(new byte[] { 0xd }, new byte[] { 0x0 }));
        assertEquals(5,
                MinHash.countSameBits(new byte[] { 0xe }, new byte[] { 0x0 }));
        assertEquals(4,
                MinHash.countSameBits(new byte[] { 0xf }, new byte[] { 0x0 }));
        assertEquals(4,
                MinHash.countSameBits(new byte[] { 0x0f }, new byte[] { 0x0 }));
        assertEquals(3,
                MinHash.countSameBits(new byte[] { 0x1f }, new byte[] { 0x0 }));
        assertEquals(3,
                MinHash.countSameBits(new byte[] { 0x2f }, new byte[] { 0x0 }));
        assertEquals(2,
                MinHash.countSameBits(new byte[] { 0x3f }, new byte[] { 0x0 }));
        assertEquals(3,
                MinHash.countSameBits(new byte[] { 0x4f }, new byte[] { 0x0 }));
        assertEquals(2,
                MinHash.countSameBits(new byte[] { 0x5f }, new byte[] { 0x0 }));
        assertEquals(2,
                MinHash.countSameBits(new byte[] { 0x6f }, new byte[] { 0x0 }));
        assertEquals(1,
                MinHash.countSameBits(new byte[] { 0x7f }, new byte[] { 0x0 }));
        assertEquals(3, MinHash.countSameBits(new byte[] { (byte) 0x8f },
                new byte[] { 0x0 }));
        assertEquals(2, MinHash.countSameBits(new byte[] { (byte) 0x9f },
                new byte[] { 0x0 }));
        assertEquals(2, MinHash.countSameBits(new byte[] { (byte) 0xaf },
                new byte[] { 0x0 }));
        assertEquals(1, MinHash.countSameBits(new byte[] { (byte) 0xbf },
                new byte[] { 0x0 }));
        assertEquals(2, MinHash.countSameBits(new byte[] { (byte) 0xcf },
                new byte[] { 0x0 }));
        assertEquals(1, MinHash.countSameBits(new byte[] { (byte) 0xdf },
                new byte[] { 0x0 }));
        assertEquals(1, MinHash.countSameBits(new byte[] { (byte) 0xef },
                new byte[] { 0x0 }));
        assertEquals(0, MinHash.countSameBits(new byte[] { (byte) 0xff },
                new byte[] { 0x0 }));

        assertEquals(
                16,
                MinHash.countSameBits(new byte[] { 0x0, 0x0 }, new byte[] {
                        0x0, 0x0 }));
        assertEquals(
                15,
                MinHash.countSameBits(new byte[] { 0x0, 0x0 }, new byte[] {
                        0x0, 0x1 }));

    }

    public void test_calculate_toBinaryString() throws IOException {
        assertEquals("00000000", MinHash.toBinaryString(new byte[] { 0 }));
        assertEquals("00000001", MinHash.toBinaryString(new byte[] { 1 }));
        assertEquals("00000010", MinHash.toBinaryString(new byte[] { 2 }));
        assertEquals("00000011", MinHash.toBinaryString(new byte[] { 3 }));
        assertEquals("00000100", MinHash.toBinaryString(new byte[] { 4 }));
        assertEquals("00000101", MinHash.toBinaryString(new byte[] { 5 }));
        assertEquals("00000110", MinHash.toBinaryString(new byte[] { 6 }));
        assertEquals("00000111", MinHash.toBinaryString(new byte[] { 7 }));
        assertEquals("00001000", MinHash.toBinaryString(new byte[] { 8 }));
        assertEquals("00001001", MinHash.toBinaryString(new byte[] { 9 }));
        assertEquals("00001010", MinHash.toBinaryString(new byte[] { 10 }));
        assertEquals("00001011", MinHash.toBinaryString(new byte[] { 11 }));
        assertEquals("00001100", MinHash.toBinaryString(new byte[] { 12 }));
        assertEquals("00001101", MinHash.toBinaryString(new byte[] { 13 }));
        assertEquals("00001110", MinHash.toBinaryString(new byte[] { 14 }));
        assertEquals("00001111", MinHash.toBinaryString(new byte[] { 15 }));
        assertEquals("00010000", MinHash.toBinaryString(new byte[] { 16 }));

        assertEquals("0000000100000000",
                MinHash.toBinaryString(new byte[] { 1, 0 }));
    }

    public void test_calculate() throws IOException {

        final Analyzer minhashAnalyzer4 = MinHash.createAnalyzer(1, 0, 4);
        final Analyzer minhashAnalyzer8 = MinHash.createAnalyzer(1, 0, 8);
        final Analyzer minhashAnalyzer12 = MinHash.createAnalyzer( 1, 0,
                12);
        final Analyzer minhashAnalyzer16 = MinHash.createAnalyzer(1, 0,
                16);
        final StringBuilder[] texts = createTexts();

        assertEquals("00000011", MinHash.toBinaryString(MinHash.calculate(
                minhashAnalyzer4, texts[0].toString())));
        assertEquals("11010011", MinHash.toBinaryString(MinHash.calculate(
                minhashAnalyzer8, texts[0].toString())));
        assertEquals("1101001100000000", MinHash.toBinaryString(MinHash
                .calculate(minhashAnalyzer12, texts[0].toString())));
        assertEquals("1101001101000000", MinHash.toBinaryString(MinHash
                .calculate(minhashAnalyzer16, texts[0].toString())));
    }

    public void test_calculate_multiple_data() throws IOException {

        final Analyzer minhashAnalyzer4 = MinHash.createAnalyzer(1, 0, 4);
        final Analyzer minhashAnalyzer8 = MinHash.createAnalyzer(1, 0, 8);
        final Analyzer minhashAnalyzer12 = MinHash.createAnalyzer(1, 0,
                12);
        final Analyzer minhashAnalyzer16 = MinHash.createAnalyzer(1, 0,
                16);
        final StringBuilder[] texts = createTexts();

        assertEquals("00000011", MinHash.toBinaryString(MinHash
                .calculate(new MinHash.Data[] { MinHash.newData(
                        minhashAnalyzer4, texts[0].toString(), 4) })));
        assertEquals("00000011", MinHash.toBinaryString(MinHash
                .calculate(new MinHash.Data[] { MinHash.newData(
                        minhashAnalyzer4, texts[0].toString(), 8) })));
        assertEquals("0000001100000000", MinHash.toBinaryString(MinHash
                .calculate(new MinHash.Data[] { MinHash.newData(
                        minhashAnalyzer4, texts[0].toString(), 12) })));
        assertEquals("0000001100000000", MinHash.toBinaryString(MinHash
                .calculate(new MinHash.Data[] { MinHash.newData(
                        minhashAnalyzer4, texts[0].toString(), 16) })));

        assertEquals("00000011", MinHash.toBinaryString(MinHash
                .calculate(new MinHash.Data[] { MinHash.newData(
                        minhashAnalyzer8, texts[0].toString(), 4) })));
        assertEquals("11010011", MinHash.toBinaryString(MinHash
                .calculate(new MinHash.Data[] { MinHash.newData(
                        minhashAnalyzer8, texts[0].toString(), 8) })));
        assertEquals("1101001100000000", MinHash.toBinaryString(MinHash
                .calculate(new MinHash.Data[] { MinHash.newData(
                        minhashAnalyzer8, texts[0].toString(), 12) })));
        assertEquals("1101001100000000", MinHash.toBinaryString(MinHash
                .calculate(new MinHash.Data[] { MinHash.newData(
                        minhashAnalyzer8, texts[0].toString(), 16) })));

        assertEquals("00000011", MinHash.toBinaryString(MinHash
                .calculate(new MinHash.Data[] { MinHash.newData(
                        minhashAnalyzer12, texts[0].toString(), 4) })));
        assertEquals("11010011", MinHash.toBinaryString(MinHash
                .calculate(new MinHash.Data[] { MinHash.newData(
                        minhashAnalyzer12, texts[0].toString(), 8) })));
        assertEquals("1101001100000000", MinHash.toBinaryString(MinHash
                .calculate(new MinHash.Data[] { MinHash.newData(
                        minhashAnalyzer12, texts[0].toString(), 12) })));
        assertEquals("1101001100000000", MinHash.toBinaryString(MinHash
                .calculate(new MinHash.Data[] { MinHash.newData(
                        minhashAnalyzer12, texts[0].toString(), 16) })));

        assertEquals("00000011", MinHash.toBinaryString(MinHash
                .calculate(new MinHash.Data[] { MinHash.newData(
                        minhashAnalyzer16, texts[0].toString(), 4) })));
        assertEquals("11010011", MinHash.toBinaryString(MinHash
                .calculate(new MinHash.Data[] { MinHash.newData(
                        minhashAnalyzer16, texts[0].toString(), 8) })));
        assertEquals("1101001100000000", MinHash.toBinaryString(MinHash
                .calculate(new MinHash.Data[] { MinHash.newData(
                        minhashAnalyzer16, texts[0].toString(), 12) })));
        assertEquals("1101001101000000", MinHash.toBinaryString(MinHash
                .calculate(new MinHash.Data[] { MinHash.newData(
                        minhashAnalyzer16, texts[0].toString(), 16) })));
    }

    public void test_bitCount() {
        // Test with all zeros
        assertEquals(0, MinHash.bitCount(new byte[] { 0x0 }));

        // Test with single bits
        assertEquals(1, MinHash.bitCount(new byte[] { 0x1 }));
        assertEquals(1, MinHash.bitCount(new byte[] { 0x2 }));
        assertEquals(2, MinHash.bitCount(new byte[] { 0x3 }));
        assertEquals(1, MinHash.bitCount(new byte[] { 0x4 }));
        assertEquals(2, MinHash.bitCount(new byte[] { 0x5 }));
        assertEquals(2, MinHash.bitCount(new byte[] { 0x6 }));
        assertEquals(3, MinHash.bitCount(new byte[] { 0x7 }));
        assertEquals(1, MinHash.bitCount(new byte[] { 0x8 }));

        // Test with all ones
        assertEquals(8, MinHash.bitCount(new byte[] { (byte) 0xff }));

        // Test with multiple bytes
        assertEquals(16, MinHash.bitCount(new byte[] { (byte) 0xff, (byte) 0xff }));
        assertEquals(8, MinHash.bitCount(new byte[] { (byte) 0xff, 0x0 }));
        assertEquals(12, MinHash.bitCount(new byte[] { (byte) 0xf, (byte) 0xff }));

        // Test with empty array
        assertEquals(0, MinHash.bitCount(new byte[] {}));
    }

    public void test_toBinaryString_null() {
        assertNull(MinHash.toBinaryString(null));
    }

    public void test_toBinaryString_emptyArray() {
        assertEquals("", MinHash.toBinaryString(new byte[] {}));
    }

    public void test_createHashFunctions() {
        // Test with different seeds and numbers
        com.google.common.hash.HashFunction[] funcs1 = MinHash.createHashFunctions(0, 10);
        assertNotNull(funcs1);
        assertEquals(10, funcs1.length);

        com.google.common.hash.HashFunction[] funcs2 = MinHash.createHashFunctions(100, 5);
        assertNotNull(funcs2);
        assertEquals(5, funcs2.length);

        // Test with seed 0 and num 1
        com.google.common.hash.HashFunction[] funcs3 = MinHash.createHashFunctions(0, 1);
        assertNotNull(funcs3);
        assertEquals(1, funcs3.length);

        // Verify hash functions produce different results with different seeds
        String testString = "test";
        long hash1 = funcs1[0].hashUnencodedChars(testString).asLong();
        long hash2 = funcs2[0].hashUnencodedChars(testString).asLong();
        assertFalse(hash1 == hash2);
    }

    public void test_calculate_emptyText() throws IOException {
        final Analyzer analyzer = MinHash.createAnalyzer(1, 0, 8);
        final byte[] result = MinHash.calculate(analyzer, "");
        assertNotNull(result);
    }

    public void test_calculate_singleWord() throws IOException {
        final Analyzer analyzer = MinHash.createAnalyzer(1, 0, 8);
        final byte[] result = MinHash.calculate(analyzer, "hello");
        assertNotNull(result);
        assertEquals(1, result.length);
    }

    public void test_calculate_multiple_data_with_different_analyzers() throws IOException {
        final Analyzer analyzer1 = MinHash.createAnalyzer(1, 0, 8);
        final Analyzer analyzer2 = MinHash.createAnalyzer(1, 0, 16);
        final String text1 = "hello world";
        final String text2 = "goodbye world";

        final byte[] result = MinHash.calculate(new MinHash.Data[] {
            MinHash.newData(analyzer1, text1, 8),
            MinHash.newData(analyzer2, text2, 16)
        });

        assertNotNull(result);
        assertEquals(3, result.length); // 8 + 16 = 24 bits = 3 bytes
    }


    public void test_newData() {
        final Analyzer analyzer = MinHash.createAnalyzer(1, 0, 8);
        final String text = "test text";
        final int numOfBits = 8;

        final MinHash.Data data = MinHash.newData(analyzer, text, numOfBits);

        assertNotNull(data);
        assertEquals(analyzer, data.analyzer());
        assertEquals(text, data.text());
        assertEquals(numOfBits, data.numOfBits());
    }

    public void test_compare_differentLengths() {
        // When comparing byte arrays of different lengths, should return 0
        assertEquals(0.0f, MinHash.compare(new byte[] { 0x1 }, new byte[] { 0x1, 0x1 }));
        assertEquals(0.0f, MinHash.compare(new byte[] { 0x1, 0x2, 0x3 }, new byte[] { 0x1, 0x2 }));
    }

    public void test_compare_base64Strings() {
        final String base64_1 = "AA==";  // byte array [0x00]
        final String base64_2 = "AA==";  // byte array [0x00]
        final String base64_3 = "/w==";  // byte array [0xff]

        // Same strings should have similarity 1.0
        assertEquals(1.0f, MinHash.compare(base64_1, base64_2));

        // Different strings should have lower similarity
        float similarity = MinHash.compare(base64_1, base64_3);
        assertTrue(similarity >= 0.0f && similarity <= 1.0f);
    }

    public void test_compare_base64Strings_withNumOfBits() {
        final String base64_1 = "AA==";  // byte array [0x00]
        final String base64_2 = "AA==";  // byte array [0x00]

        // When both strings are identical, similarity should be 1.0
        float similarity = MinHash.compare(8, base64_1, base64_2);
        assertTrue(similarity >= 0.0f && similarity <= 1.0f);
    }

    public void test_createAnalyzer_withDefaultTokenizer() {
        final Analyzer analyzer = MinHash.createAnalyzer(1, 0, 8);
        assertNotNull(analyzer);
    }

    public void test_createAnalyzer_withCustomTokenizer() throws IOException {
        final org.apache.lucene.analysis.core.WhitespaceTokenizer tokenizer =
            new org.apache.lucene.analysis.core.WhitespaceTokenizer();
        final Analyzer analyzer = MinHash.createAnalyzer(tokenizer, 1, 0, 8);
        assertNotNull(analyzer);
    }

    public void test_calculate_with_different_hashBits() throws IOException {
        final Analyzer analyzer1 = MinHash.createAnalyzer(1, 0, 128);
        final Analyzer analyzer2 = MinHash.createAnalyzer(2, 0, 128);
        final Analyzer analyzer4 = MinHash.createAnalyzer(4, 0, 128);

        final String text = "hello world test";

        final byte[] result1 = MinHash.calculate(analyzer1, text);
        final byte[] result2 = MinHash.calculate(analyzer2, text);
        final byte[] result4 = MinHash.calculate(analyzer4, text);

        // 1 bit * 128 = 128 bits = 16 bytes
        assertEquals(16, result1.length);

        // 2 bits * 128 = 256 bits = 32 bytes
        assertEquals(32, result2.length);

        // 4 bits * 128 = 512 bits = 64 bytes
        assertEquals(64, result4.length);
    }

    public void test_countSameBits_multipleBytes() {
        // All bits are the same when comparing identical arrays
        assertEquals(
            24,
            MinHash.countSameBits(
                new byte[] { 0x0, 0x0, 0x0 },
                new byte[] { 0x0, 0x0, 0x0 }
            )
        );

        // First two bytes differ completely, third byte matches
        // 0xff vs 0x0 = 0 matching bits per byte
        // 0x0 vs 0x0 = 8 matching bits
        // Total: 0 + 0 + 8 = 8
        assertEquals(
            8,
            MinHash.countSameBits(
                new byte[] { (byte) 0xff, (byte) 0xff, 0x0 },
                new byte[] { 0x0, 0x0, 0x0 }
            )
        );

        // All three bytes differ completely (0xff vs 0x0)
        // 0 + 0 + 0 = 0 matching bits
        assertEquals(
            0,
            MinHash.countSameBits(
                new byte[] { (byte) 0xff, (byte) 0xff, (byte) 0xff },
                new byte[] { 0x0, 0x0, 0x0 }
            )
        );
    }

    // ========== Tests for null safety improvements ==========

    public void test_compare_bytes_withNull() {
        final byte[] data = new byte[] { 0x01, 0x02, 0x03 };

        // Null first argument
        assertEquals(0.0f, MinHash.compare((byte[]) null, data));

        // Null second argument
        assertEquals(0.0f, MinHash.compare(data, (byte[]) null));

        // Both null
        assertEquals(0.0f, MinHash.compare((byte[]) null, (byte[]) null));
    }

    public void test_compare_bytes_withNumOfBits_withNull() {
        final byte[] data = new byte[] { 0x01, 0x02, 0x03 };
        final int numOfBits = 24;

        // Null first argument
        assertEquals(0.0f, MinHash.compare(numOfBits, (byte[]) null, data));

        // Null second argument
        assertEquals(0.0f, MinHash.compare(numOfBits, data, (byte[]) null));

        // Both null
        assertEquals(0.0f, MinHash.compare(numOfBits, (byte[]) null, (byte[]) null));
    }

    public void test_compare_strings_withNull() {
        final String encoded = "AQID"; // Base64 for {0x01, 0x02, 0x03}

        // Null first argument
        assertEquals(0.0f, MinHash.compare((String) null, encoded));

        // Null second argument
        assertEquals(0.0f, MinHash.compare(encoded, (String) null));

        // Both null
        assertEquals(0.0f, MinHash.compare((String) null, (String) null));
    }

    public void test_compare_strings_withNumOfBits_withNull() {
        final String encoded = "AQID"; // Base64 for {0x01, 0x02, 0x03}
        final int numOfBits = 24;

        // Null first argument
        assertEquals(0.0f, MinHash.compare(numOfBits, (String) null, encoded));

        // Null second argument
        assertEquals(0.0f, MinHash.compare(numOfBits, encoded, (String) null));

        // Both null
        assertEquals(0.0f, MinHash.compare(numOfBits, (String) null, (String) null));
    }

    public void test_bitCount_withNull() {
        // Null input should return 0
        assertEquals(0, MinHash.bitCount(null));
    }

    public void test_bitCount_withEmptyArray() {
        // Empty array should return 0
        assertEquals(0, MinHash.bitCount(new byte[0]));
    }

    public void test_bitCount_optimized_implementation() {
        // Test that the optimized Integer.bitCount() implementation works correctly
        // Test various bit patterns

        // All zeros
        assertEquals(0, MinHash.bitCount(new byte[] { 0x00 }));

        // All ones (8 bits)
        assertEquals(8, MinHash.bitCount(new byte[] { (byte) 0xFF }));

        // Mixed patterns
        assertEquals(1, MinHash.bitCount(new byte[] { 0x01 })); // 00000001
        assertEquals(2, MinHash.bitCount(new byte[] { 0x03 })); // 00000011
        assertEquals(4, MinHash.bitCount(new byte[] { 0x0F })); // 00001111
        assertEquals(4, MinHash.bitCount(new byte[] { (byte) 0xF0 })); // 11110000
        assertEquals(4, MinHash.bitCount(new byte[] { (byte) 0xAA })); // 10101010
        assertEquals(4, MinHash.bitCount(new byte[] { 0x55 })); // 01010101

        // Multiple bytes
        assertEquals(16, MinHash.bitCount(new byte[] { (byte) 0xFF, (byte) 0xFF }));
        assertEquals(8, MinHash.bitCount(new byte[] { (byte) 0xFF, 0x00 }));
        assertEquals(12, MinHash.bitCount(new byte[] { (byte) 0xAA, (byte) 0xAA, (byte) 0xAA }));
    }

    // ========== Tests for Java 21 record implementation ==========

    public void test_data_record_creation() throws IOException {
        final Analyzer analyzer = MinHash.createAnalyzer(1, 0, 128);
        final String text = "test text";
        final int numOfBits = 128;

        final MinHash.Data data = MinHash.newData(analyzer, text, numOfBits);

        assertNotNull(data);
        assertEquals(analyzer, data.analyzer());
        assertEquals(text, data.text());
        assertEquals(numOfBits, data.numOfBits());
    }

    public void test_data_record_equality() throws IOException {
        final Analyzer analyzer1 = MinHash.createAnalyzer(1, 0, 128);
        final Analyzer analyzer2 = MinHash.createAnalyzer(1, 0, 128);
        final String text = "test text";
        final int numOfBits = 128;

        final MinHash.Data data1 = MinHash.newData(analyzer1, text, numOfBits);
        final MinHash.Data data2 = MinHash.newData(analyzer1, text, numOfBits);
        final MinHash.Data data3 = MinHash.newData(analyzer2, "different", numOfBits);

        // Same instance
        assertEquals(data1, data1);

        // Same values, same analyzer instance
        assertEquals(data1, data2);

        // Different values
        assertFalse(data1.equals(data3));
    }

    public void test_data_record_hashCode() throws IOException {
        final Analyzer analyzer = MinHash.createAnalyzer(1, 0, 128);
        final String text = "test text";
        final int numOfBits = 128;

        final MinHash.Data data1 = MinHash.newData(analyzer, text, numOfBits);
        final MinHash.Data data2 = MinHash.newData(analyzer, text, numOfBits);

        // Same values should produce same hash code
        assertEquals(data1.hashCode(), data2.hashCode());
    }

    public void test_data_record_toString() throws IOException {
        final Analyzer analyzer = MinHash.createAnalyzer(1, 0, 128);
        final String text = "test text";
        final int numOfBits = 128;

        final MinHash.Data data = MinHash.newData(analyzer, text, numOfBits);
        final String toString = data.toString();

        // Record toString should contain the field values
        assertNotNull(toString);
        assertTrue(toString.contains("test text"));
        assertTrue(toString.contains("128"));
    }

    public void test_data_record_with_calculate() throws IOException {
        final Analyzer analyzer = MinHash.createAnalyzer(1, 0, 128);
        final String text = "test text for minhash";
        final int numOfBits = 128;

        final MinHash.Data data = MinHash.newData(analyzer, text, numOfBits);

        // Should work with MinHash.calculate(Data)
        final byte[] result = MinHash.calculate(data);

        assertNotNull(result);
        assertEquals(16, result.length); // 128 bits = 16 bytes
    }

    public void test_data_record_with_calculate_array() throws IOException {
        final Analyzer analyzer1 = MinHash.createAnalyzer(1, 0, 64);
        final Analyzer analyzer2 = MinHash.createAnalyzer(1, 0, 64);

        final MinHash.Data data1 = MinHash.newData(analyzer1, "first text", 64);
        final MinHash.Data data2 = MinHash.newData(analyzer2, "second text", 64);

        final MinHash.Data[] dataArray = new MinHash.Data[] { data1, data2 };

        // Should work with MinHash.calculate(Data[])
        final byte[] result = MinHash.calculate(dataArray);

        assertNotNull(result);
        assertEquals(16, result.length); // 64 + 64 bits = 128 bits = 16 bytes
    }

    // ========== Tests for optimized countSameBits implementation ==========

    public void test_countSameBits_optimized_allSame() {
        // Test XOR-based implementation with identical bytes
        final byte[] data1 = new byte[] { (byte) 0xAA, (byte) 0x55, (byte) 0xFF };
        final byte[] data2 = new byte[] { (byte) 0xAA, (byte) 0x55, (byte) 0xFF };

        // All 24 bits should match
        assertEquals(24, MinHash.countSameBits(data1, data2));
    }

    public void test_countSameBits_optimized_allDifferent() {
        // Test XOR-based implementation with completely different bytes
        // 0xFF XOR 0x00 = 0xFF (all bits differ, 0 matching)
        final byte[] data1 = new byte[] { (byte) 0xFF, (byte) 0xFF, (byte) 0xFF };
        final byte[] data2 = new byte[] { 0x00, 0x00, 0x00 };

        // 0 bits should match
        assertEquals(0, MinHash.countSameBits(data1, data2));
    }

    public void test_countSameBits_optimized_partialMatch() {
        // Test XOR-based implementation with partial matches
        // 0xF0 = 11110000
        // 0x0F = 00001111
        // XOR  = 11111111 (all 8 bits differ, 0 matching)
        final byte[] data1 = new byte[] { (byte) 0xF0 };
        final byte[] data2 = new byte[] { 0x0F };

        assertEquals(0, MinHash.countSameBits(data1, data2));

        // 0xF0 = 11110000
        // 0xF0 = 11110000
        // XOR  = 00000000 (all 8 bits match)
        final byte[] data3 = new byte[] { (byte) 0xF0 };
        final byte[] data4 = new byte[] { (byte) 0xF0 };

        assertEquals(8, MinHash.countSameBits(data3, data4));

        // 0xAA = 10101010
        // 0x55 = 01010101
        // XOR  = 11111111 (all 8 bits differ, 0 matching)
        final byte[] data5 = new byte[] { (byte) 0xAA };
        final byte[] data6 = new byte[] { 0x55 };

        assertEquals(0, MinHash.countSameBits(data5, data6));
    }

    public void test_countSameBits_optimized_mixedPatterns() {
        // Test various patterns to ensure XOR-based implementation is correct
        // 0x80 = 10000000
        // 0x00 = 00000000
        // XOR  = 10000000 (1 bit differs, 7 bits match)
        final byte[] data1 = new byte[] { (byte) 0x80 };
        final byte[] data2 = new byte[] { 0x00 };

        assertEquals(7, MinHash.countSameBits(data1, data2));

        // 0x81 = 10000001
        // 0x01 = 00000001
        // XOR  = 10000000 (1 bit differs, 7 bits match)
        final byte[] data3 = new byte[] { (byte) 0x81 };
        final byte[] data4 = new byte[] { 0x01 };

        assertEquals(7, MinHash.countSameBits(data3, data4));

        // 0xFF = 11111111
        // 0x7F = 01111111
        // XOR  = 10000000 (1 bit differs, 7 bits match)
        final byte[] data5 = new byte[] { (byte) 0xFF };
        final byte[] data6 = new byte[] { 0x7F };

        assertEquals(7, MinHash.countSameBits(data5, data6));
    }

    public void test_countSameBits_optimized_multipleBytes_complex() {
        // Test XOR-based implementation with complex multi-byte patterns
        // byte 1: 0xFF vs 0x00 = 0 matching bits
        // byte 2: 0xAA vs 0xAA = 8 matching bits
        // byte 3: 0x0F vs 0x1F = 0x10 XOR = 00010000 = 1 bit differs, 7 match
        // Total: 0 + 8 + 7 = 15
        final byte[] data1 = new byte[] { (byte) 0xFF, (byte) 0xAA, 0x0F };
        final byte[] data2 = new byte[] { 0x00, (byte) 0xAA, 0x1F };

        assertEquals(15, MinHash.countSameBits(data1, data2));
    }

    public void test_countSameBits_optimized_edgeCases() {
        // Test with single byte arrays
        final byte[] single1 = new byte[] { 0x00 };
        final byte[] single2 = new byte[] { 0x00 };
        assertEquals(8, MinHash.countSameBits(single1, single2));

        // Test with large arrays
        final byte[] large1 = new byte[100];
        final byte[] large2 = new byte[100];
        for (int i = 0; i < 100; i++) {
            large1[i] = (byte) 0xFF;
            large2[i] = (byte) 0xFF;
        }
        assertEquals(800, MinHash.countSameBits(large1, large2)); // 100 bytes * 8 bits

        // Test with alternating patterns
        final byte[] alt1 = new byte[10];
        final byte[] alt2 = new byte[10];
        for (int i = 0; i < 10; i++) {
            alt1[i] = (byte) (i % 2 == 0 ? 0xAA : 0x55);
            alt2[i] = (byte) (i % 2 == 0 ? 0xAA : 0x55);
        }
        assertEquals(80, MinHash.countSameBits(alt1, alt2)); // All bits match
    }
}
