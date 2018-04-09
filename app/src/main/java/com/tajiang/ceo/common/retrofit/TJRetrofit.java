package com.tajiang.ceo.common.retrofit;

import com.tajiang.ceo.common.application.TJApp;
import com.tajiang.ceo.common.constant.TJCst;
import com.tajiang.ceo.common.retrofit.cookieJar.ClearableCookieJar;
import com.tajiang.ceo.common.retrofit.cookieJar.PersistentCookieJar;
import com.tajiang.ceo.common.retrofit.cookieJar.cache.SetCookieCache;
import com.tajiang.ceo.common.retrofit.cookieJar.persistence.SharedPrefsCookiePersistor;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2016/6/6.
 */
public class TJRetrofit {

    private static RetrofitService service;

    public static RetrofitService createRetrofitService() {
        if(service == null) {
            synchronized (TJRetrofit.class) {
                ClearableCookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(TJApp.getInstance()));
                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).cookieJar(cookieJar).build();
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(TJCst.BASE_URL)
                        .client(client)
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                service = retrofit.create(RetrofitService.class);
            }
        }
        return service;
    }

}
