package org.codelibs.minhash;

import java.io.IOException;

import junit.framework.TestCase;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;
import org.codelibs.minhash.MinHash;

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
}
