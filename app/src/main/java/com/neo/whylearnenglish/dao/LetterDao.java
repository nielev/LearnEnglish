package com.neo.whylearnenglish.dao;

import com.neo.whylearnenglish.bean.Letter;


import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 单词逻辑处理接口
 * Created by Neo on 2016/12/15.
 */

public interface LetterDao {
    @GET("dictionary.php")
    Observable<String> getLetter(@Query("w") String w, @Query("key") String key);
}
