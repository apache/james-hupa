package org.apache.hupa.shared.algorithms;

public class RC4 {
    private byte state[] = new byte[256];

    public RC4(String key) throws NullPointerException {
        this(key.getBytes());
    }

    public RC4(byte[] key) throws NullPointerException {
        for (int i = 0; i < 256; i++) {
            state[i] = (byte) i;
        }

        int index1 = 0, index2 = 0;

        byte tmp;
        if (key == null || key.length == 0) {
            throw new NullPointerException();
        }

        for (int i = 0; i < 256; i++) {
            index2 = ((key[index1] & 0xff) + (state[i] & 0xff) + index2) & 0xff;

            tmp = state[i];
            state[i] = state[index2];
            state[index2] = tmp;

            index1 = (index1 + 1) % key.length;
        }
    }

    public byte[] rc4(String data) {
        return data == null ? null : this.rc4(data.getBytes());
    }

    public byte[] rc4(byte[] buf) {
        int xorIndex;
        byte tmp;
        int x = 0, y = 0;

        if (buf == null) {
            return null;
        }

        byte[] result = new byte[buf.length];

        for (int i = 0; i < buf.length; i++) {
            x = (x + 1) & 0xff;
            y = ((state[x] & 0xff) + y) & 0xff;

            tmp = state[x];
            state[x] = state[y];
            state[y] = tmp;

            xorIndex = ((state[x] & 0xff) + (state[y] & 0xff)) & 0xff;
            result[i] = (byte) (buf[i] ^ state[xorIndex]);
            // System.out.printf("%02d %03d %03d %c %03d %03d %03d %03d\n", i ,
            // (buf[i] & 0xFF), (result[i] & 0xFF), buf[i], x, (state[x] &
            // 0xFF), y, (state[y] & 0xFF));
        }

        return result;
    }
}
