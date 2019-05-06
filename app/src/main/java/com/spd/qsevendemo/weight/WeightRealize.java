package com.spd.qsevendemo.weight;

import android.content.Context;
import android.serialport.DeviceControlSpd;
import android.serialport.SerialPortSpd;
import android.util.Log;

import com.speedata.libutils.DataConversionUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;

/**
 * ----------Dragon be here!----------/
 * 　　　┏┓　　　┏┓
 * 　　┏┛┻━━━┛┻┓
 * 　　┃　　　　　　　┃
 * 　　┃　　　━　　　┃
 * 　　┃　┳┛　┗┳　┃
 * 　　┃　　　　　　　┃
 * 　　┃　　　┻　　　┃
 * 　　┃　　　　　　　┃
 * 　　┗━┓　　　┏━┛
 * 　　　　┃　　　┃神兽保佑
 * 　　　　┃　　　┃代码无BUG！
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 * ━━━━━━神兽出没━━━━━━
 *
 * @author :孙天伟 in  2018/4/10   17:47.
 * 联系方式:QQ:420401567
 * 功能描述:
 */
public class WeightRealize implements WeightInterface {
    private SerialPortSpd serialPort = null;
    private ReadThread readThread;
    private DisplayWeightDatasListener displayWeightDatasListener;
    private Queue<Double> queue = new ArrayDeque<>();//电子秤队列
    private int fd = -1;
    private String TAG = "stw";
    //命令执行成功
//02 41 30 37 31 03
    private final String CMD_SUCCESE = "024130373103";
    //通讯失败
//02 41 31 37 30 03
    private final String MESSAGE_FAILD = "024131373003";
    //命令执行失败
//02 41 32 37 33 03
    private final String CMD_FAILD = "024132373303";

    //精度过高
//02 41 33 37 32 03
    private final String JINGDU_UP = "024133373203";
    private DeviceControlSpd deviceControlSpd;

    //#########################################################
    public WeightRealize(Context context) {

        PlaySound.initSoundPool(context);
    }

    @Override
    public void initWeight() {
        startWeight();
    }

    @Override
    public void setWeightStatas(DisplayWeightDatasListener displayWeightDatasListener) {
        this.displayWeightDatasListener = displayWeightDatasListener;
    }

    private byte[] cmd;

    @Override
    public void sendCmd(byte[] cmd) {
        this.cmd = cmd;
        btnCmd(cmd);
        Log.e("tests", "sendCmd: " + DataConversionUtils.byteArrayToString(cmd));
    }

    @Override
    public void releaseWeightDev() {
        stopWeight();
    }

    class ReadThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (!interrupted()) {
                try {
                  //  byte[] bytes = serialPort.ReadSerial(fd, 24, 15);
                    //现天伟优化
                    byte[] bytes = serialPort.ReadSerial(fd, 24, 15);
//                    Log.e("tests", "run: " + DataConversionUtils.byteArrayToString(bytes));
                    if (bytes != null) {
                        Log.i(TAG, ":按键返回:" + DataConversionUtils.byteArrayToString(bytes));
                        byte[] bytesR = null;

                        for (int i = 0; i < bytes.length; i++) {
                            if (bytes[i] == 0x02) {
                                if (bytes[i + 1] == (byte) 0x41) {
                                    bytesR = CheckClass.cutBytes(bytes, i, 6);
                                    break;
                                }
                                byte[] weightBytes = CheckClass.cutBytes(bytes, i, 12);
                                Log.i(TAG, "run: 正确重量数据： " + DataConversionUtils.byteArrayToString(weightBytes));
                                final byte[] fuhao = {weightBytes[1]};
                                final byte[] data = CheckClass.cutBytes(weightBytes, 1, 7);
                                final byte[] xiaoshudian = {weightBytes[8]};
                                double dws = Double.parseDouble(DataConversionUtils.byteArrayToAscii(data));
                                if (Integer.parseInt(DataConversionUtils.byteArrayToAscii(xiaoshudian)) == 2) {
                                    dws = dws / 100;
                                } else if (Integer.parseInt(DataConversionUtils.byteArrayToAscii(xiaoshudian)) == 3) {
                                    dws = dws / 1000;
                                }
                                weightStability(dws);
                                Log.i("weighr", "run:" + DataConversionUtils.byteArrayToAscii(fuhao) + DataConversionUtils.byteArrayToAscii(data) + "小数点：" + DataConversionUtils.byteArrayToAscii(xiaoshudian));
                                break;
                            }
                        }
                        if (bytesR != null) {
                            Log.i(TAG, ": 按键返回正确数据: " + DataConversionUtils.byteArrayToString(bytesR));
                            if (Arrays.equals(bytesR, DataConversionUtils.HexString2Bytes(CMD_SUCCESE))) {
                                PlaySound.play(PlaySound.PASS, PlaySound.NO_CYCLE);
                            } else if (Arrays.equals(bytesR, DataConversionUtils.HexString2Bytes(CMD_FAILD)) || Arrays.equals(bytesR, DataConversionUtils.HexString2Bytes(MESSAGE_FAILD))) {
                                PlaySound.play(PlaySound.FAILD, PlaySound.NO_CYCLE);
//                                btnCmd(cmd);
                            } else if (Arrays.equals(bytesR, DataConversionUtils.HexString2Bytes(CMD_FAILD)) || Arrays.equals(bytesR, DataConversionUtils.HexString2Bytes(JINGDU_UP))) {
                                displayWeightDatasListener.WeightStatas(3, 0);
                            }
                        } else {
                            Log.i(TAG, ": 按键返回无数据:");
                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("接收错误", "run: 接收错误" + e.toString());
                    displayWeightDatasListener.WeightStatas(0, 0);
                }
            }
        }
    }

    /**
     * 判断数据是否稳定
     *
     * @param weight 重量数据 double类型
     */
    private void weightStability(double weight) {
        if (weight != 0) {
            queue.offer(weight);
            Log.i("duilie", "mainEvent: 开始 ");
            if (queue.size() > 3) {
                Object[] s = queue.toArray();
                for (int j = 1; j < s.length; j++) {
                    if ((double) s[s.length - 1] - (double) s[s.length - j - 1] < 0.02 && (double) s[s.length - 1] - (double) s[0] < 0.02) {//稳定返回
                        System.out.print((double) s[j] + "dafdafasdfas");
                        displayWeightDatasListener.WeightStatas(1, (double) s[s.length - 1]);
                        queue.poll();
                    } else {//不稳定返回
                        queue.poll();
                        displayWeightDatasListener.WeightStatas(2, (double) s[s.length - 1]);
                        break;
                    }
                }
            }
        } else {//等于0返回当不稳定处理
            queue.clear();
            displayWeightDatasListener.WeightStatas(2, weight);
        }
    }


    private void btnCmd(final byte[] b) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (fd == -1) {
                        return;
                    }
                    serialPort.WriteSerialByte(fd, b);
                    byte[] resultBytes = serialPort.ReadSerial(fd, 3072);
                    if (resultBytes != null) {
                        Log.i("tests", ":按键返回:" + DataConversionUtils.byteArrayToString(resultBytes));
                        byte[] bytes = null;
                        for (int i = 0; i < resultBytes.length; i++) {
                            if (i > 3000 || resultBytes.length == 1) {
                                break;
                            }
                            if (resultBytes[i] == (byte) 0x02 && resultBytes[1 + i] == (byte) 0x41) {
                                bytes = CheckClass.cutBytes(resultBytes, i, 6);
                                break;
                            }
                        }

                        if (bytes != null) {
                            Log.i(TAG, ": 按键返回正确数据: " + DataConversionUtils.byteArrayToString(bytes));
                            if (Arrays.equals(bytes, DataConversionUtils.HexString2Bytes(CMD_SUCCESE))) {
                                PlaySound.play(PlaySound.PASS, PlaySound.NO_CYCLE);
                            } else if (Arrays.equals(bytes, DataConversionUtils.HexString2Bytes(CMD_FAILD))) {
                                PlaySound.play(PlaySound.FAILD, PlaySound.NO_CYCLE);
//
                            } else if (Arrays.equals(bytes, DataConversionUtils.HexString2Bytes(MESSAGE_FAILD))) {
                                Log.e("tests", "run: 通讯失败重发");
//                                btnCmd(b);
                            } else if (Arrays.equals(bytes, DataConversionUtils.HexString2Bytes(CMD_FAILD)) || Arrays.equals(bytes, DataConversionUtils.HexString2Bytes(JINGDU_UP))) {
                                displayWeightDatasListener.WeightStatas(3, 0);
                            }
                        } else {
                            Log.i(TAG, ": 按键返回无数据:");
                            Log.e("tests", "run: 无数据重发重发");
//                            btnCmd(b);
                        }
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void startWeight() {
        try {
            serialPort = new SerialPortSpd();
//            serialPort.OpenSerial("/dev/ttyMT1", 9600);
//            deviceControlSpd = new DeviceControlSpd(DeviceControlSpd.PowerType.NEW_MAIN, 75);
            serialPort.OpenSerial("/dev/ttyMT7", 9600);
//            serialPort.OpenSerial("/dev/ttyXRM1", 9600);
//            deviceControlSpd = new DeviceControlSpd(DeviceControlSpd.PowerType.EXPAND);
//            deviceControlSpd.PowerOnDevice();
            fd = serialPort.getFd();
            if (fd == -1) {
                displayWeightDatasListener.WeightStatas(0, 0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        readThread = new ReadThread();
        readThread.start();
    }

    public void stopWeight() {
        if (serialPort != null) {
            serialPort.CloseSerial(fd);
        }
//        try {
//            deviceControlSpd.PowerOffDevice();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        if (readThread != null) {
            readThread.interrupt();
        }
    }


}
