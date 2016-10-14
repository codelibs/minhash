package org.codelibs.minhash;

import java.io.IOException;
import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;
import org.codelibs.minhash.analysis.MinHashTokenFilter;
import org.codelibs.minhash.util.FastBitSet;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.google.common.io.BaseEncoding;

/**
 * This class is MinHash utility.
 * 
 * @author shinsuke
 *
 */
public class MinHash {

    private MinHash() {
    }

    /**
     * Compare base64 strings for MinHash.
     *
     * @param numOfBits The number of MinHash bits
     * @param str1 MinHash base64 string
     * @param str2 MinHash base64 string
     * @return similarity (0 to 1.0f)
     */
    public static float compare(final int numOfBits, final String str1,
            final String str2) {
        return compare(numOfBits, BaseEncoding.base64().decode(str1),
                BaseEncoding.base64().decode(str2));
    }

    /**
     * Compare base64 strings for MinHash.
     *
     * @param str1 MinHash base64 string
     * @param str2 MinHash base64 string
     * @return similarity (0 to 1.0f)
     */
    public static float compare(final String str1, final String str2) {
        return compare(BaseEncoding.base64().decode(str1), BaseEncoding
                .base64().decode(str2));
    }

    /**
     * Compare bytes for MinHash.
     *
     * @param data1 MinHash bytes
     * @param data2 MinHash bytes
     * @return similarity (0 to 1.0f)
     */
    public static float compare(final byte[] data1, final byte[] data2) {
        return compare(data1.length * 8, data1, data2);
    }

    /**
     * Compare bytes for MinHash.
     *
     * @param numOfBits The number of MinHash bits
     * @param data1 MinHash bytes
     * @param data2 MinHash bytes
     * @return similarity (0 to 1.0f)
     */
    public static float compare(final int numOfBits, final byte[] data1,
            final byte[] data2) {
        if (data1.length != data2.length) {
            return 0;
        }
        final int count = countSameBits(data1, data2);
        return (float) count / (float) numOfBits;
    }

    protected static int countSameBits(final byte[] data1, final byte[] data2) {
        int count = 0;
        for (int i = 0; i < data1.length; i++) {
            byte b1 = data1[i];
            byte b2 = data2[i];
            for (int j = 0; j < 8; j++) {
                if ((b1 & 1) == (b2 & 1)) {
                    count++;
                }
                b1 >>= 1;
                b2 >>= 1;
            }
        }
        return count;
    }

    /**
     * Create hash functions.
     * 
     * @param seed a base seed
     * @param num the number of hash functions.
     * @return
     */
    public static HashFunction[] createHashFunctions(final int seed,
            final int num) {
        final HashFunction[] hashFunctions = new HashFunction[num];
        for (int i = 0; i < num; i++) {
            hashFunctions[i] = Hashing.murmur3_128(seed + i);
        }
        return hashFunctions;
    }

    /**
     * Calculates MinHash value.
     * 
     * @param analyzer analyzer to parse a text
     * @param text a target text
     * @return MinHash value
     * @throws IOException
     */
    public static byte[] calculate(final Analyzer analyzer, final String text)
            throws IOException {
        byte[] value = null;
        try (TokenStream stream = analyzer.tokenStream("minhash", text)) {
            final CharTermAttribute termAtt = stream
                    .addAttribute(CharTermAttribute.class);
            stream.reset();
            if (stream.incrementToken()) {
                final String minhashValue = termAtt.toString();
                value = BaseEncoding.base64().decode(minhashValue);
            }
            stream.end();
        }
        return value;
    }

    /**
     * Calculates MinHash value.
     * 
     * @param data data with analyzer, text and the number of bits
     * @return MinHash value
     * @throws IOException
     */
    public static byte[] calculate(final Data data) throws IOException {
        return calculate(data.analyzer, data.text);
    }

    /**
     * Calculates MinHash value.
     * 
     * @param data data with analyzer, text and the number of bits
     * @return MinHash value
     * @throws IOException
     */
    public static byte[] calculate(final Data[] data) throws IOException {
        int bitSize = 0;
        for (final Data target : data) {
            bitSize += target.numOfBits;
        }
        int pos = 0;
        final FastBitSet bitSet = new FastBitSet(bitSize);
        for (final Data target : data) {
            int count = 0;
            final byte[] bytes = calculate(target);
            for (final byte b : bytes) {
                byte bits = b;
                for (int j = 0; j < 8; j++) {
                    bitSet.set(pos, (bits & 0x1) == 0x1);
                    pos++;
                    count++;
                    if (count >= target.numOfBits) {
                        break;
                    }
                    bits >>= 1;
                }
            }
        }
        return bitSet.toByteArray();
    }

    /**
     * Returns a string formatted by bits.
     * 
     * @param data
     * @return
     */
    public static String toBinaryString(final byte[] data) {
        if (data == null) {
            return null;
        }
        final StringBuilder buf = new StringBuilder(data.length * 8);
        for (final byte element : data) {
            byte bits = element;
            for (int j = 0; j < 8; j++) {
                if ((bits & 0x80) == 0x80) {
                    buf.append('1');
                } else {
                    buf.append('0');
                }
                bits <<= 1;
            }
        }
        return buf.toString();
    }

    /**
     * Count the number of true bits.
     * 
     * @param data a target data
     * @return the number of true bits
     */
    public static int bitCount(final byte[] data) {
        int count = 0;
        for (final byte element : data) {
            byte bits = element;
            for (int j = 0; j < 8; j++) {
                if ((bits & 1) == 1) {
                    count++;
                }
                bits >>= 1;
            }
        }
        return count;
    }

    /**
     * <p>Create an analyzer to calculate a minhash.</p>
     * <p>Uses {@link WhitespaceTokenizer} as {@link Tokenizer}</p>
     *
     * @param hashBit the number of hash bits
     * @param seed a base seed for hash function
     * @param num the number of hash functions
     * @return analyzer used by {@link MinHash#calculate(Analyzer, String)}
     */
    public static Analyzer createAnalyzer(final int hashBit, final int seed, final int num) {
        return createAnalyzer(new WhitespaceTokenizer(), hashBit, seed, num);
    }

    /**
     * Create an analyzer to calculate a minhash.
     * 
     * @param tokenizer a tokenizer to parse a text
     * @param hashBit the number of hash bits
     * @param seed a base seed for hash function
     * @param num the number of hash functions
     * @return analyzer used by {@link MinHash#calculate(Analyzer, String)}
     */
    public static Analyzer createAnalyzer(final Tokenizer tokenizer,
            final int hashBit, final int seed, final int num) {
        final HashFunction[] hashFunctions = MinHash.createHashFunctions(seed,
                num);
        final Analyzer minhashAnalyzer = new Analyzer() {
            @Override
            protected TokenStreamComponents createComponents(
                    final String fieldName) {
                final TokenStream stream = new MinHashTokenFilter(
                        tokenizer, hashFunctions, hashBit);
                return new TokenStreamComponents(tokenizer, stream);
            }
        };
        return minhashAnalyzer;
    }

    /**
     * Create a target data which has analyzer, text and the number of bits.
     * 
     * @param analyzer
     * @param text
     * @param numOfBits
     * @return
     */
    public static Data newData(final Analyzer analyzer, final String text,
            final int numOfBits) {
        return new Data(analyzer, text, numOfBits);
    }

    public static class Data {
        final int numOfBits;

        final String text;

        final Analyzer analyzer;

        Data(final Analyzer analyzer, final String text, final int numOfBits) {
            this.numOfBits = numOfBits;
            this.text = text;
            this.analyzer = analyzer;
        }
    }
}
