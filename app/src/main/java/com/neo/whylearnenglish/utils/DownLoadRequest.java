package com.neo.whylearnenglish.utils;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 下载文件请求
 * Created by Neo on 2017/1/5.
 */

public class DownLoadRequest {

    private final OkHttpClient mOkhttpClient;
    private String SDPath = Environment.getExternalStorageDirectory().getAbsolutePath();
    private static boolean isStop;
    private long stopPoint;
    private long mTotal;

    public DownLoadRequest(OkHttpClient okHttpClient){
        if(null != okHttpClient) {
            mOkhttpClient = okHttpClient;
        } else {
            mOkhttpClient = new OkHttpClient();
        }
    }

    public void downloadStop(){
        isStop = true;
    }
    public long getStopPoint(){
        return stopPoint;
    }
    public void downloadRequest(String downloadUrl, final String fileName){
        isStop = false;
        final Request request = new Request.Builder().url(downloadUrl).build();
//        List<Cookie> cookies = mOkhttpClient.cookieJar().loadForRequest(request.url());

        mOkhttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response){
                Log.i(HttpRequest.TAG, "code:"+response.code()+",message:" + response.message());
                if(response.code() >= 200 && response.code() <=206){
                    InputStream is = null;
                    byte[] buf = new byte[2048];
                    int len = 0;
                    FileOutputStream fos = null;
                    try {
                        is = response.body().byteStream();
                        mTotal = response.body().contentLength();
                        File file = new File(SDPath,fileName);
                        fos = new FileOutputStream(file);
                        long sum = 0;
                        while ((len = is.read(buf)) != -1) {
                            fos.write(buf, 0, len);
                            sum += len;
                            int progress = (int) (sum * 1.0f / mTotal * 100);
                            Log.i(HttpRequest.TAG,"progress=" + progress);
                            if(isStop){
                                stopPoint = sum;
                                Log.i(HttpRequest.TAG,"stop..."+stopPoint);
                                break;
                            }
                        }
                        fos.flush();
                        Log.i(HttpRequest.TAG, "文件下载成功");
                    } catch (Exception e) {
                        Log.i(HttpRequest.TAG,"文件下载失败");
                        e.printStackTrace();
                    } finally {
                        CloseUtils.closeQuietly(is,fos);
                    }
                }
            }
        });
    }
    private long start;
    public void downloadContinueRequest(String downloadUrl, final String fileName, final long startsPoint) {
        isStop = false;
        if(startsPoint <= 0) {
            File file = new File(SDPath,fileName);
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
                start = fis.available();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            start = startsPoint;
        }
        Request request = new Request.Builder().url(downloadUrl).header("RANGE","bytes="+ start +"-").build();

        mOkhttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) {
//                if(response.code() == 200) {//206
                    ResponseBody body = response.body();
                    InputStream in = body.byteStream();
                    FileChannel channelOut = null;
                    // 随机访问文件，可以指定断点续传的起始位置
                    RandomAccessFile randomAccessFile = null;
                    File file = new File(SDPath,fileName);

                    try {
                        randomAccessFile = new RandomAccessFile(file, "rwd");
                        //Chanel NIO中的用法，由于RandomAccessFile没有使用缓存策略，直接使用会使得下载速度变慢，
                        // 亲测缓存下载3.3秒的文件，用普通的RandomAccessFile需要20多秒。
                        channelOut = randomAccessFile.getChannel();
                        // 内存映射，直接使用RandomAccessFile，是用其seek方法指定下载的起始位置，使用缓存下载，在这里指定下载位置。
                        MappedByteBuffer mappedBuffer = channelOut.map(FileChannel.MapMode.READ_WRITE, startsPoint, body.contentLength());
                        byte[] buffer = new byte[1024];
                        int len;
                        long sum = startsPoint;
                        while ((len = in.read(buffer)) != -1) {
                            sum += len;
                            int progress = (int) (sum * 1.0f / mTotal * 100);
                            Log.i(HttpRequest.TAG,"sum:"+sum+",progress:"+progress);
                            mappedBuffer.put(buffer, 0, len);
                            if(isStop){
                                stopPoint = sum;
                                Log.i(HttpRequest.TAG,"stop..."+stopPoint);
                                break;
                            }
                        }
                        Log.i(HttpRequest.TAG, "文件下载成功");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }finally {
                        CloseUtils.closeQuietly(in, channelOut, randomAccessFile);
                    }
//                } else {
//                    Log.i(HttpRequest.TAG, "code:"+response.code()+",error:" + response.message());
//                }

            }
        });
    }
}
