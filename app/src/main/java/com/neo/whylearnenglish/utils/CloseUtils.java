package com.neo.whylearnenglish.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * 关闭流的工具类
 * Created by Neo on 2016/9/29.
 */

public class CloseUtils {
    private CloseUtils() {}

    /**
     * 关闭closeble对象
     * @param closeable
     */
    public static void closeQuietly(Closeable closeable) {
        if(null != closeable){
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 传入多个closeble对象关闭
     * @param closeables
     */
    public static void closeQuietly(Closeable... closeables){
        if(null != closeables && closeables.length > 0){
            for (Closeable closeable : closeables) {
                try {
                    closeable.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
