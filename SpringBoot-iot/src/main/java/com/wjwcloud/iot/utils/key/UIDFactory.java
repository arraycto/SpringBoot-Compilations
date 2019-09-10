package com.wjwcloud.iot.utils.key;


import java.net.InetAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class UIDFactory {
    public static final Logger LOGGER = LoggerFactory.getLogger(UIDFactory.class);
    public static final String UID_GUID = "GUID";
    public static final String UID_UUID = "UUID";
    protected static final long EPOCH = System.currentTimeMillis();
    protected static final long JVMHASH = -2147483648L;
    protected static final long MACHINEID = getMachineID();
    protected static final Random M_RANDOM;
    private static MessageDigest md5;
    private boolean isMd5 = false;

    public UIDFactory() {
    }

    public static UIDFactory getDefault() {
        return UUID.getInstance();
    }

    public static UIDFactory getInstance(String uidfactory) throws Exception {
        if (uidfactory.equalsIgnoreCase("UUID")) {
            return UUID.getInstance();
        } else {
            throw new Exception(uidfactory + " Not Found!");
        }
    }

    public abstract String getNextUID();

    public abstract String getUID();

    public boolean isMD5() {
        return this.isMd5;
    }

    public void setMD5(boolean flag) {
        this.isMd5 = flag;
    }

    public abstract void setUID(String var1) throws Exception;

    public abstract String toPrintableString();

    protected static byte[] toMD5(byte[] bytes) {
        return md5.digest(bytes);
    }

    private static long getMachineID() {
        long i = 0L;

        try {
            InetAddress inetaddress = InetAddress.getLocalHost();
            byte[] abyte0 = inetaddress.getAddress();
            i = (long)toInt(abyte0);
        } catch (Exception var4) {
            LOGGER.info("Get MachineId Failed", var4);
        }

        return i;
    }

    private static int toInt(byte[] abyte0) {
        return abyte0[0] << 24 & -16777216 | abyte0[1] << 16 & 16711680 | abyte0[2] << 8 & '\uff00' | abyte0[3] & 255;
    }

    static {
        M_RANDOM = new Random(EPOCH);

        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException var1) {
            LOGGER.info("Init MessageDigest failed.", var1);
        }

    }
}
