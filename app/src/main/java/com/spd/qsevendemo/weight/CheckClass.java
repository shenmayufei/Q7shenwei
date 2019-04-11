package com.spd.qsevendemo.weight;

import android.util.Log;

import com.speedata.libutils.DataConversionUtils;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.regex.Pattern;

public class CheckClass {
    static byte[] ss = {0x41, 0x75, 0x2B, 0x30, 0x30, 0x32, 0x30, 0x30, 0x30, 0x33};
    private static String TAG = "stw";

    public static void main(String[] args) {
        byte[] gg = getCheckRuslut(DataConversionUtils.byteArrayToString(ss));
        Log.i("ta", "main: " + gg);
    }
// byte转char


    public static char[] getChars(byte[] bytes) {

        Charset cs = Charset.forName("UTF-8");

        ByteBuffer bb = ByteBuffer.allocate(bytes.length);

        bb.put(bytes);

        bb.flip();


        CharBuffer cb = cs.decode(bb);

        return cb.array();

    }

    public static String byteToChar(char[] b) {
        StringBuffer hex = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
//            char c = (char) (((b[0] & 0xFF) << 8) | (b[1] & 0xFF));
            hex.append((char) (byte) b[i]);
        }
        return hex.toString();
    }

    public static byte[] checkWight(String weight, String weight2) {
        if (!weight.equals("") && !weight2.equals("")) {


        }
        if (!weight.equals("")) {
//            String weightCheck = "";
//            StringBuilder weightBuilder = null;
//            if (isInteger(weight)) {
//                weightCheck = convertStringToHex(weight);
//                weightBuilder = new StringBuilder(weightCheck);
//                weightBuilder.append("30").append("30");
//                if (weight.length() < 4) {
//                    String s = weightBuilder.toString();
//                    int lengths = s.length();
//                    for (int i = 0; i < lengths; i++) {
//                        weightBuilder.insert(0, 30);
//                    }
//                }
//                Log.i(TAG, "onClick:整数数 ");
//            } else {
//                weight = stringDeleSymbol(weight);//删除小数点
//                weightCheck = convertStringToHex(weight);
//                weightBuilder = new StringBuilder(weightCheck);
//                if (weight.length() < 6) {
//                    int lengths = 6 - weight.length();
//                    for (int i = 0; i < lengths; i++) {
//                        weightBuilder.insert(0, 30);
//                    }
//                }
//            }

//            Log.i(TAG, "onClick:" + weightBuilder.toString());
            byte[] check = getCheckRuslut(("41752B" + convertStringToHex(weight) + convertStringToHex(weight2) + "32"));
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("0241752B").append(convertStringToHex(weight) + convertStringToHex(weight2)).append("32").append(DataConversionUtils.byteArrayToString(check)).append("03");
            return DataConversionUtils.HexString2Bytes(stringBuilder.toString());
        } else {
            return null;
        }
    }

    public static byte[] intToByteArray1(int i) {
        byte[] result = new byte[4];
//        result[0] = (byte) ((i >> 32) & 0xFF);
        result[0] = (byte) ((i >> 24) & 0xFF);
        result[1] = (byte) ((i >> 16) & 0xFF);
        result[2] = (byte) ((i >> 8) & 0xFF);
        result[3] = (byte) (i & 0xFF);
        return result;
    }

    /**
     * 获取异或校验结果
     *
     * @param s
     * @return
     */
    public static byte[] getCheckRuslut(String s) {
        byte result = ehuo(DataConversionUtils.HexString2Bytes(s));
        int h = getHeight4(result);
        if (h <= 9) {
            h = h + 48;
        } else {
            h += 55;
        }
        int l = getLow4(result);
        if (l <= 9) {
            l += 48;
        } else {
            l += 55;
        }
        return new byte[]{(byte) h, (byte) l};
    }

    /**
     * 去掉字符串的标点符号
     *
     * @param s
     * @return
     */
    public static String stringDeleSymbol(String s) {
        return s.replaceAll("[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……& amp;*（）——+|{}【】‘；：”“’。，、？|-]", "");//字符串去标点符号
    }

    /**
     * 截取数组
     *
     * @param bytes  被截取数组
     * @param start  被截取数组开始截取位置
     * @param length 新数组的长度
     * @return 新数组
     */
    public static byte[] cutBytes(byte[] bytes, int start, int length) {
        byte[] res = new byte[length];
        System.arraycopy(bytes, start, res, 0, length);
        return res;
    }

    /**
     * 异或 算法
     *
     * @param cmd 要异或的数据
     * @return
     */
    public static byte ehuo(byte[] cmd) {
        byte result = 0;
        for (int i = 0; i < cmd.length; i++) {
            result ^= (byte) cmd[i];
        }
        return result;
    }

    /**
     * 获取byte高四位
     *
     * @param data
     * @return
     */
    public static int getHeight4(byte data) {//获取高四位
        int height;
        height = ((data & 0xf0) >> 4);
        return height;
    }

    /**
     * 获取byte低四位
     *
     * @param data
     * @return
     */
    public static int getLow4(byte data) {//获取低四位
        int low;
        low = (data & 0x0f);
        return low;
    }

    /**
     * 判断整数（int）
     *
     * @param str
     * @return
     */
    public static boolean isInteger(String str) {
        if (null == str || "".equals(str)) {
            return false;
        }
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    /**
     * ascii 码字符串转16进制字符串
     *
     * @param str
     * @return
     */
    public static String convertStringToHex(String str) {
        char[] chars = str.toCharArray();
        StringBuffer hex = new StringBuffer();
        for (int i = 0; i < chars.length; i++) {
            hex.append(Integer.toHexString((int) chars[i]));
        }
        return hex.toString();
    }

    public static String convertStringToHex2(byte[] str) {
        StringBuffer hex = new StringBuffer();
        for (int i = 0; i < str.length; i++) {
            hex.append(Integer.toHexString((char) str[i]));
        }
        return hex.toString();
    }

    /**
     * 十六进制字符转ASCII
     *
     * @param hex
     * @return
     */
    public static String convertHexToString(String hex) {

        StringBuilder sb = new StringBuilder();
        StringBuilder temp = new StringBuilder();

        //49204c6f7665204a617661 split into two characters 49, 20, 4c...
        for (int i = 0; i < hex.length() - 1; i += 2) {

            //grab the hex in pairs
            String output = hex.substring(i, (i + 2));
            //convert hex to decimal
            int decimal = Integer.parseInt(output, 16);
            //convert the decimal to character
            sb.append((char) decimal);

            temp.append(decimal);
        }

        return sb.toString();
    }

    /**
     * 字符串转ASCII
     *
     * @param value
     * @return
     */
    public static String stringToAscii(String value) {
        StringBuffer sbu = new StringBuffer();
        char[] chars = value.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (i != chars.length - 1) {
                sbu.append((int) chars[i]).append(",");
            } else {
                sbu.append((int) chars[i]);
            }
        }
        return sbu.toString();
    }
}
