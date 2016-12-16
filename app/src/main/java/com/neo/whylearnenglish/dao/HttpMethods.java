package com.neo.whylearnenglish.dao;

import android.content.Context;

import com.neo.whylearnenglish.bean.Letter;
import com.neo.whylearnenglish.bean.Xml2Letter;
import com.neo.whylearnenglish.function.StringConverterFactory;
import com.neo.whylearnenglish.global.Constant;
import com.neo.whylearnenglish.utils.LogUtil;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * 请求过程封装类
 *
 * Created by Neo on 2016/12/15.
 */

public class HttpMethods {
    private static final int DEFAULT_TIMEOUT = 5;
    private final Retrofit mRetrofit;
    private final LetterDao mLetterDao;

    private HttpMethods() {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient().newBuilder();
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        mRetrofit = new Retrofit.Builder()
                .client(httpClientBuilder.build())
                .addConverterFactory(StringConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(Constant.URL_DIC_SEARCH)
                .build();
        mLetterDao = mRetrofit.create(LetterDao.class);
    }

    /**
     * 访问HttpMethods时生成单例
     */
    private static class SingletonHolder {
        private static final HttpMethods INSTANCE = new HttpMethods();
    }

    /**
     * 获取单例
     * @return
     */
    public static HttpMethods getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void getLetter(Subscriber<Letter> subscriber, String w){
        Observable<String> letter = mLetterDao.getLetter(w, Constant.DIC_KEY);
        letter.map(new HttpResultFunc<Letter>())
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(subscriber);
    }

    private class HttpResultFunc<Letter> implements Func1<String, Letter>{

        @Override
        public Letter call(String s) {
            LogUtil.i("LETTER",s);
            Letter letter = (Letter) Xml2Letter.parseImageXml(s);
            return letter;
        }
    }
}
