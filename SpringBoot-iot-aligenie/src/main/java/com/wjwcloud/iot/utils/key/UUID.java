package com.wjwcloud.iot.utils.key;


public class UUID extends UIDFactory {
    protected static final int BITS8 = 8;
    protected static final int BYTELEN = 16;
    protected static final int HIMASK = 240;
    protected static final int LO8BITMASK = 255;
    protected static final int LOMASK = 15;
    protected static final long MAX_INT = 32767L;
    protected static final long MAX_LONG = 2147483647L;
    protected long mhiTag;
    protected long mloTag;
    protected String muuid = null;

    protected UUID(long highTag, long loTag) {
        this.mhiTag = highTag;
        this.mloTag = loTag;
        this.muuid = toString(this.toByteArray());
    }

    protected UUID() {
        this.next();
        this.muuid = toString(this.toByteArray());
    }

    private static byte hiNibble(byte b) {
        return (byte)(b >> 4 & 15);
    }

    private static byte loNibble(byte b) {
        return (byte)(b & 15);
    }

    public boolean equals(Object obj) {
        try {
            if (obj == null) {
                return false;
            } else {
                UUID uuid = (UUID)obj;
                boolean flag = uuid.mhiTag == this.mhiTag && uuid.mloTag == this.mloTag;
                return flag;
            }
        } catch (ClassCastException var4) {
            return false;
        }
    }

    public int hashCode() {
        int result = (int)(this.mhiTag ^ this.mhiTag >>> 32);
        result = 31 * result + (int)(this.mloTag ^ this.mloTag >>> 32);
        return result;
    }

    public String getNextUID() {
        this.next();
        return this.muuid;
    }

    public String getUID() {
        return this.muuid;
    }

    public void setUID(String uidStr) throws Exception {
        long loTag = 0L;
        long hiTag = 0L;
        int len = uidStr.length();
        if (32 != len) {
            throw new Exception("bad string format");
        } else {
            int i = 0;

            for(int idx = 0; i < 2; ++i) {
                loTag = 0L;

                for(int j = 0; j < len / 2; ++j) {
                    String s = uidStr.substring(idx++, idx);
                    int val = Integer.parseInt(s, 16);
                    loTag <<= 4;
                    loTag |= (long)val;
                }

                if (i == 0) {
                    hiTag = loTag;
                }
            }

            this.mhiTag = hiTag;
            this.mloTag = loTag;
            this.muuid = toString(this.toByteArray());
        }
    }

    public String toPrintableString() {
        byte[] bytes = this.toByteArray();
        if (16 != bytes.length) {
            return "** Bad UUID Format/Value **";
        } else {
            StringBuffer buf = new StringBuffer();

            int i;
            for(i = 0; i < 4; ++i) {
                buf.append(Integer.toHexString(hiNibble(bytes[i])));
                buf.append(Integer.toHexString(loNibble(bytes[i])));
            }

            while(i < 10) {
                buf.append('-');

                for(int j = 0; j < 2; ++j) {
                    buf.append(Integer.toHexString(hiNibble(bytes[i])));
                    buf.append(Integer.toHexString(loNibble(bytes[i++])));
                }
            }

            buf.append('-');

            while(i < 16) {
                buf.append(Integer.toHexString(hiNibble(bytes[i])));
                buf.append(Integer.toHexString(loNibble(bytes[i])));
                ++i;
            }

            return buf.toString();
        }
    }

    public String toString() {
        return this.muuid;
    }

    protected static UIDFactory getInstance() {
        return new UUID();
    }

    protected static String toString(byte[] bytes) {
        if (16 != bytes.length) {
            return "** Bad UUID Format/Value **";
        } else {
            StringBuffer buf = new StringBuffer();

            for(int i = 0; i < 16; ++i) {
                buf.append(Integer.toHexString(hiNibble(bytes[i])));
                buf.append(Integer.toHexString(loNibble(bytes[i])));
            }

            return buf.toString();
        }
    }

    protected void next() {
        this.mhiTag = System.currentTimeMillis() + -9223372036854775808L ^ MACHINEID;
        this.mloTag = EPOCH + Math.abs(M_RANDOM.nextLong());
        this.muuid = toString(this.toByteArray());
    }

    protected byte[] toByteArray() {
        byte[] bytes = new byte[16];
        int idx = 15;
        long val = this.mloTag;

        int i;
        for(i = 0; i < 8; ++i) {
            bytes[idx--] = (byte)((int)(val & 255L));
            val >>= 8;
        }

        val = this.mhiTag;

        for(i = 0; i < 8; ++i) {
            bytes[idx--] = (byte)((int)(val & 255L));
            val >>= 8;
        }

        return !this.isMD5() ? bytes : toMD5(bytes);
    }
}
