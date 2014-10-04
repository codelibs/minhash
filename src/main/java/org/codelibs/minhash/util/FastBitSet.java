package org.codelibs.minhash.util;

/**
 * Immutable bits set.
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

        switch (bitPos) {
        case 0:
            data[bytePos] = (byte) (data[bytePos] & 0xfe);
            if (value) {
                data[bytePos] = (byte) (data[bytePos] | 0x01);
            }
            break;
        case 1:
            data[bytePos] = (byte) (data[bytePos] & 0xfd);
            if (value) {
                data[bytePos] = (byte) (data[bytePos] | 0x02);
            }
            break;
        case 2:
            data[bytePos] = (byte) (data[bytePos] & 0xfb);
            if (value) {
                data[bytePos] = (byte) (data[bytePos] | 0x04);
            }
            break;
        case 3:
            data[bytePos] = (byte) (data[bytePos] & 0xf7);
            if (value) {
                data[bytePos] = (byte) (data[bytePos] | 0x08);
            }
            break;
        case 4:
            data[bytePos] = (byte) (data[bytePos] & 0xef);
            if (value) {
                data[bytePos] = (byte) (data[bytePos] | 0x10);
            }
            break;
        case 5:
            data[bytePos] = (byte) (data[bytePos] & 0xdf);
            if (value) {
                data[bytePos] = (byte) (data[bytePos] | 0x20);
            }
            break;
        case 6:
            data[bytePos] = (byte) (data[bytePos] & 0xbf);
            if (value) {
                data[bytePos] = (byte) (data[bytePos] | 0x40);
            }
            break;
        case 7:
            data[bytePos] = (byte) (data[bytePos] & 0x7f);
            if (value) {
                data[bytePos] = (byte) (data[bytePos] | 0x80);
            }
            break;
        default:
            break;
        }
    }

    public byte[] toByteArray() {
        return data;
    }
}
