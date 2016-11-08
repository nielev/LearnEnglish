package com.neo.whylearnenglish.utils;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.Date;

public class LogUtil extends Object {
    private static String TAG = "TAG";
    /**
     * 标识, debug switch, flase 关闭所有调试信息
     */
    public final static boolean LOG = false;

    /**
     * 标识, 是否要把与服务器通信的数据写到文件里, 方便调试
     */
    public static final boolean WRITE_DATA_TOFILE = LOG && true;

    /**
     * LOG路径, 把一些必要的信息写到文件里, 方便调试查看
     */
    public final static String LOG_PATH = Environment
            .getExternalStorageDirectory().getPath()
            + File.separator
            + "sunlogin" + File.separator + "log" + File.separator;

    /**
     * 程序Fc后, 保存日志的文件全名
     */
    public final static String LOG_FC = LOG_PATH + "fc.txt";

    /**
     * 记录c<->s通信数据的文件全名, 并不总是记录到文件.
     */
    public final static String LOG_DATA = LOG_PATH + "data.txt";

	/*------------------------------ 以下方法ltl添加-------------------------------- */
    /**
     * print verbose log
     */
    public static void v(String tag, String msg) {
        if (LOG) {
            Log.v(tag, msg);
        }
    }

    /**
     * print debug log
     */
    public static void d(String tag, String msg) {
        if (LOG) {
            Log.d(tag, msg);
        }
    }

    /**
     * print info log
     */
    public static void i(String tag, String msg) {
        Log.i(tag, msg);
    }

    /**
     * print warn log
     */
    public static void w(String tag, String msg) {
        Log.w(tag, msg);
    }

    /**
     * print error log
     */
    public static void e(String tag, String msg) {
        Log.e(tag, msg);
    }

    /*
     * 把向服务器发出的数据和服务器返回的数据写到文件中
     */
    public static void writeDataToFile(String sendMsg, String responseMsg) {
        if (LogUtil.WRITE_DATA_TOFILE) {
            synchronized (LogUtil.class) {
                OutputStream os = null;
                try {
                    StringBuffer data = new StringBuffer();

                    // 时间
                    data.append(new Date().toLocaleString());
                    data.append("\r\n\r\n\r\n");

                    // 请求的数据
                    data.append(sendMsg);
                    data.append("\r\n\r\n\r\n\r\n");

                    // 响应的数据
                    data.append(responseMsg);
                    data.append("\r\n\r\n\r\n\r\n");

                    File saveFile = new File(LogUtil.LOG_PATH);
                    if (!saveFile.exists()) {
                        saveFile.mkdirs();
                    }
                    os = new FileOutputStream(LogUtil.LOG_DATA, true);
                    os.write(data.toString().getBytes("utf-8"));
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (null != os) {
                        try {
                            os.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                            // ignore exception
                        }
                    }
                }
            }
        }
    }

    /**
     * write FC message to file
     *
     * @param msg
     *            Fc message
     */
    public static void writeFcToFile(String msg) {
        if (LogUtil.LOG) {
            OutputStream os = null;
            try {
                LogUtil.v(TAG, "write fc message to file");
                File saveFile = new File(LogUtil.LOG_PATH);
                if (!saveFile.exists()) {
                    saveFile.mkdirs();
                }
                os = new FileOutputStream(LogUtil.LOG_FC, true);
                msg = new Date().toLocaleString() + "\r\n"
                        + msg.replace("\n", "\r\n") + "\r\n\r\n\r\n";
                os.write(msg.getBytes("utf-8"));
                os.flush();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (null != os) {
                    try {
                        os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        // ignore exception
                    }
                }
            }
        }
    }

    /**
     * write FC message to file
     *
     * @param writer
     *            错误记录writer
     */
    public static void writeFcToFile(StringWriter writer) {
        writeFcToFile(writer.toString());
    }
}

