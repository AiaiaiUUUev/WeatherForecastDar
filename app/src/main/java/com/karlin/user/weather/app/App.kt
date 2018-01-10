package com.karlin.user.weather.app

import android.app.Application
import com.google.gson.GsonBuilder
import io.realm.Realm
import io.realm.RealmConfiguration
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 *
 * Created by User on 09.01.2018.
 */

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        Realm.init(this)
        val config = RealmConfiguration.Builder()
                .build()
        Realm.setDefaultConfiguration(config)

        val gson = GsonBuilder()
                .setLenient()
                .create()
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val httpClient = OkHttpClient.Builder()
                .connectTimeout(15000, TimeUnit.MILLISECONDS)
                .readTimeout(10000, TimeUnit.MILLISECONDS)
//                .connectTimeout(6 * 1000, TimeUnit.SECONDS)
//                .readTimeout(30 * 1000, TimeUnit.SECONDS)
                .addInterceptor(logging).build()

        val retrofit = Retrofit.Builder()
                .baseUrl("https://your.api.url/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClient)
                .build()
        api = retrofit.create<ApiService>(ApiService::class.java)



    }
    companion object {

        var api: ApiService? = null

    }
}