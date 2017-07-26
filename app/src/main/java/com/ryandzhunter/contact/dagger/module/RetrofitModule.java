package com.ryandzhunter.contact.dagger.module;

import android.app.Application;
import android.app.Service;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ryandzhunter.contact.http.RetrofitService;

import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

/**
 * Created by aryandi on 7/1/17.
 */

@Module
public class RetrofitModule {

    private static final String BASE_URL = "http://gojek-contacts-app.herokuapp.com/";
    private static final String API_ENDPOINT = "api_endpoint";

    @Provides
    @Singleton
    @Named(API_ENDPOINT)
    public String providesEndpoint() {
        return BASE_URL;
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(@Named(API_ENDPOINT) String endpoint, OkHttpClient okHttpClient, Gson gson) {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(endpoint)
                .client(okHttpClient)
                //converts Retrofit response into Observable
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build();
        return retrofit;
    }

    @Provides
    @Singleton
    public Interceptor providesNetworkInterceptor() {
        return chain -> {
            Request.Builder requestBuilder = chain.request()
                    .newBuilder()
                    .header("Content-Type", "application/json");
            return chain.proceed(requestBuilder.build());
        };
    }


    @Provides
    @Singleton
    Cache provideOkHttpCache(Application application) {
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(application.getCacheDir(), cacheSize);
        return cache;
    }

    @Provides
    @Singleton
    public OkHttpClient providesOkHttp(Interceptor networkInterceptor, Cache cache) {
        HttpLoggingInterceptor logger = new HttpLoggingInterceptor(message -> {
            Timber.d("HttpLog", message);
        });
        logger.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(30, TimeUnit.SECONDS);
        builder.readTimeout(30, TimeUnit.SECONDS);
        builder.addNetworkInterceptor(networkInterceptor);
        builder.addInterceptor(networkInterceptor);
        builder.addNetworkInterceptor(logger);
        builder.addInterceptor(logger);
        builder.cache(cache);
        return builder.build();
    }

    @Provides
    @Singleton
    Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        return gsonBuilder.create();
    }

    @Provides
    @Singleton
    public RetrofitService providesUserService(Retrofit retrofit) {
        return retrofit.create(RetrofitService.class);
    }

}
