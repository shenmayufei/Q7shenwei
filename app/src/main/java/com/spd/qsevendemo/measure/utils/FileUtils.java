package com.spd.qsevendemo.measure.utils;

import android.os.Environment;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @作者：jtl
 * @时间：
 * @描述：文件工具类
 */
public class FileUtils {
    private static String mSDCardFolderPath;
    private static String mImgFolderPath;
    private static String mLogFolderPath;

    private FileUtils () {}

    public static FileUtils getInstance() {
        return FileUtilsHolder.fileUtils;
    }

    private static class FileUtilsHolder {
        private static FileUtils fileUtils = new FileUtils();
    }

    public void init() {
        mSDCardFolderPath = getSDCardFolderPath();
        mImgFolderPath = getImgFolderPath();
        mLogFolderPath = getLogFolderPath();

        mkdirs(mSDCardFolderPath);
        mkdirs(mImgFolderPath);
        mkdirs(mLogFolderPath);
    }


    public String getSDCardFolderPath() {
        if (TextUtils.isEmpty(mSDCardFolderPath)) {
            mSDCardFolderPath = Environment.getExternalStorageDirectory().getPath() + "/IMI";
        }

        return mSDCardFolderPath;
    }



    public String getImgFolderPath() {
        if (TextUtils.isEmpty(mImgFolderPath)) {
            mImgFolderPath = getSDCardFolderPath() + "/Img/";
        }

        mkdirs(mImgFolderPath);

        return mImgFolderPath;
    }

    public String getLogFolderPath() {
        if (TextUtils.isEmpty(mLogFolderPath)) {
            mLogFolderPath = getSDCardFolderPath() + "/Log/";
        }

        return mLogFolderPath;
    }

    public File mkdirs(@NonNull String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }

        return file;
    }

    //true为存在，false为不存在
    public boolean mkdir(String path){
        File file = new File(path);

        return file.exists();
    }
    /**
     * 定义文件保存的方法，写入到文件中，所以是输出流
     */
    public void writeStr2Txt(String content) {
        try {
            File file = new File(getLogFolderPath()+"measure.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream outputStream = new FileOutputStream(file, true);
            outputStream.write(content.getBytes("gbk"));
            outputStream.write("\r\n".getBytes());//写入换行
            outputStream.flush();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 定义文件保存的方法，写入到文件中，所以是输出流
     */
    private boolean isHasWrite=false;
    private File file;
    private FileOutputStream outputStream;
    public void writeStr2TxtStart() {
        try {
            if (!isHasWrite){
                isHasWrite=true;
            }
            file = new File(getLogFolderPath()+"measure"+ System.currentTimeMillis()+".txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            outputStream = new FileOutputStream(file, true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeStr2TxtIn(String content) {
        if (!isHasWrite){
            return;
        }
        try {
            outputStream.write(content.getBytes("gbk"));
            outputStream.write("\r\n".getBytes());//写入换行
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeStr2TxtEnd() {
        if (!isHasWrite){
            return;
        }
        try {
            outputStream.flush();
            outputStream.close();

            isHasWrite=false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
