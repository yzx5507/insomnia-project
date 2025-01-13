package com.insomnia.multiCountry.utils;


import static com.insomnia.multiCountry.utils.DES3.CS;

/**
 * @author Jay  zhixin.yuan@transsnet.com
 * @project: insomnia-project
 * @description:
 * @date 2024/10/25
 */
public class ByteUtils {

    public static String toHex(byte[] bs) {
        char[] rs = new char[bs.length * 2];

        for(int i = 0; i < bs.length; ++i) {
            rs[i + i] = CS[bs[i] >> 4 & 15];
            rs[i + i + 1] = CS[bs[i] & 15];
        }

        return new String(rs);
    }

    public static byte[] hexString2Bytes(String data) {
        if (data == null) {
            return null;
        } else {
            byte[] result = new byte[(data.length() + 1) / 2];
            if ((data.length() & 1) == 1) {
                data = data + "0";
            }

            for(int i = 0; i < result.length; ++i) {
                result[i] = (byte)(hex2byte(data.charAt(i * 2 + 1)) | hex2byte(data.charAt(i * 2)) << 4);
            }

            return result;
        }
    }

    public static byte hex2byte(char hex) {
        if (hex <= 'f' && hex >= 'a') {
            return (byte)(hex - 97 + 10);
        } else if (hex <= 'F' && hex >= 'A') {
            return (byte)(hex - 65 + 10);
        } else {
            return hex <= '9' && hex >= '0' ? (byte)(hex - 48) : 0;
        }
    }

    public static String bytesToHexString(byte[] src){
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString().toUpperCase();
    }

    public static byte[] merage(byte[][] data) {
        int len = 0;

        for(int i = 0; i < data.length; ++i) {
            if (data[i] == null) {
                throw new IllegalArgumentException("");
            }

            len += data[i].length;
        }

        byte[] newData = new byte[len];
        len = 0;
        byte[][] arrayOfByte = data;
        int j = data.length;

        for(int i = 0; i < j; ++i) {
            byte[] d = arrayOfByte[i];
            System.arraycopy(d, 0, newData, len, d.length);
            len += d.length;
        }

        return newData;
    }

    public static String bytesToHex(byte[] bs) {
        char[] cs = new char[bs.length * 2];
        int io = 0;
        byte[] var6 = bs;
        int var5 = bs.length;

        for(int var4 = 0; var4 < var5; ++var4) {
            byte n = var6[var4];
            cs[io++] = CS[n >> 4 & 15];
            cs[io++] = CS[n >> 0 & 15];
        }

        return new String(cs);
    }

    public static byte[] hexToBytes(String s) {
        s = s.toUpperCase();
        int len = s.length() / 2;
        int ii = 0;
        byte[] bs = new byte[len];

        for(int i = 0; i < len; ++i) {
            char c = s.charAt(ii++);
            int h;
            if (c <= '9') {
                h = c - 48;
            } else {
                h = c - 65 + 10;
            }

            h <<= 4;
            c = s.charAt(ii++);
            if (c <= '9') {
                h |= c - 48;
            } else {
                h |= c - 65 + 10;
            }

            bs[i] = (byte)h;
        }

        return bs;
    }

    public static String bytes2HexString(byte[] data) {
        if (data == null) {
            return "";
        } else {
            StringBuilder buffer = new StringBuilder();
            byte[] var2 = data;
            int var3 = data.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                byte b = var2[var4];
                String hex = Integer.toHexString(b & 255);
                if (hex.length() == 1) {
                    buffer.append('0');
                }

                buffer.append(hex);
            }

            return buffer.toString().toUpperCase();
        }
    }

    public static byte[] subBytes(byte[] data, int offset, int len) {
        if (offset >= 0 && data.length > offset) {
            if (len < 0 || data.length < offset + len) {
                len = data.length - offset;
            }

            byte[] ret = new byte[len];
            System.arraycopy(data, offset, ret, 0, len);
            return ret;
        } else {
            return null;
        }
    }

    public static String bytesToHex(byte[] bs, int pos, int len) {
        char[] cs = new char[len * 2];
        int io = 0;

        for(int i = pos; i < pos + len; ++i) {
            byte n = bs[i];
            cs[io++] = CS[n >> 4 & 15];
            cs[io++] = CS[n >> 0 & 15];
        }

        return new String(cs);
    }

    public static byte[] intToBytes(int intValue) {
        byte[] bytes = new byte[4];

        for(int i = 0; i < bytes.length; ++i) {
            bytes[i] = (byte)(intValue >> (3 - i << 3) & 255);
        }

        return bytes;
    }

    public static int bytes2Int(byte[] bytes) {
        int value = 0;
        // 由高位到低位  
        for (int i = 0; i < bytes.length; i++) {
          int shift = (bytes.length - 1 - i) * 8;
          value += (bytes[i] & 0x000000FF) << shift;// 往高位游  
        }
        return value;
    }

}
