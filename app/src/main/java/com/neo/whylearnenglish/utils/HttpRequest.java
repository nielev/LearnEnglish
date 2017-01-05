package com.neo.whylearnenglish.utils;

import android.os.Environment;
import android.util.Log;

import com.neo.whylearnenglish.base.WhyLearnApplicationLike;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 封装okhttp
 * Created by Neo on 2016/12/27.
 */

public class HttpRequest {

    public static final String TAG = "HttpRequest";

    public static final String GET = "GET";
    public static final String POST = "POST";
    public final OkHttpClient mOkhttpClient;

    private HttpRequest(){
        mOkhttpClient = new OkHttpClient();
    }

    public static HttpRequest getInstance(){
        return SingleHolder.httpRequest;
    }

    private static class SingleHolder {
        private static final HttpRequest httpRequest = new HttpRequest();
    }

    public void request(String url, ResponseListener res){
        this.request(GET, url, null, null, res);
    }

    public void request(String url, Map<String,String> params, ResponseListener res) {
        this.request(GET, url, params, null, res);
    }

    public void request(String method, String url, Map<String,String> params, Map<String, String> headers, ResponseListener res) {
        if (GET.equalsIgnoreCase(method)) {
            getRequest(url, params, headers, res);
        } else if (POST.equalsIgnoreCase(method)) {
            postRequest(url, params, headers, res);
        }
    }

    private void postRequest(String url, Map<String,String> params, Map<String, String> headers, final ResponseListener res) {
        FormBody.Builder builder = new FormBody.Builder();
        FormBody formBody = null;
        if(null != params && params.size() > 0) {
            for (String key : params.keySet()) {
                builder.add(key,params.get(key));
            }
        }
        formBody = builder.build();
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.post(formBody);
        requestBuilder.url(url);
        if(null != headers && headers.size() > 0) {
            for (String key : headers.keySet()) {
                requestBuilder.addHeader(key, headers.get(key));
            }
        }
        Request request = requestBuilder.build();
        LogUtil.i(TAG, "url:"+request.url().toString());

        Call call = mOkhttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                res.onFailure(e);
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {

                WhyLearnApplicationLike.mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        res.onResponse(response);
                    }
                });
            }
        });
    }

    private void getRequest(String url, Map<String,String> params, Map<String, String> headers, final ResponseListener res) {
        if(null != params && params.size() > 0) {
            if(!url.contains("?")){
                url+="?";
                for (String key : params.keySet()) {
                    url += key + "=" + params.get(key) + "&";
                }
                url = url.substring(0, url.length()-1);
            }
        }

        final Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        LogUtil.i(TAG, "url:"+request.url().toString());

        Call call = mOkhttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                res.onFailure(e);
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {

                WhyLearnApplicationLike.mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        res.onResponse(response);
                    }
                });
            }
        });
    }

    public interface ResponseListener {
        /**
         * Response in MainThread
         * @param response
         */
        void onResponse(Response response);

        /**
         * error in childThread
         * @param e
         */
        void onFailure(Exception e);
    }
    private String SDPath = Environment.getExternalStorageDirectory().getAbsolutePath();
    private boolean isStop;
    private long stopPoint;
    public void downloadStop(){
        isStop = true;
    }
    public long getStopPoint(){
        return stopPoint;
    }
    public void downloadRequest(String downloadUrl, final String fileName){
        isStop = false;
        Request request = new Request.Builder().url(downloadUrl).build();

        mOkhttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                try {
                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    File file = new File(SDPath,fileName);
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        Log.i(TAG,"progress=" + progress);
                        if(isStop){
                            stopPoint = sum;
                            Log.i(TAG,"stop..."+stopPoint);
                            break;
                        }
                    }
                    fos.flush();;
                    Log.i(TAG, "文件下载成功");
                } catch (Exception e) {
                    Log.i(TAG,"文件下载失败");
                    e.printStackTrace();
                } finally {
                    CloseUtils.closeQuietly(is,fos);
                }
            }
        });
    }

    public void downloadContinueRequest(String downloadUrl, final String fileName, final long startsPoint) {
        Request request = new Request.Builder().url(downloadUrl).header("RANGE","bytes="+ startsPoint +"-").build();
        mOkhttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
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
                    int sum = 0;
                    while ((len = in.read(buffer)) != -1) {
                        sum += len;
                        Log.i(TAG,"sum:"+sum);
                        mappedBuffer.put(buffer, 0, len);
                    }
                    Log.i(TAG, "文件下载成功");
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    CloseUtils.closeQuietly(in, channelOut, randomAccessFile);
                }
            }
        });
    }
}
