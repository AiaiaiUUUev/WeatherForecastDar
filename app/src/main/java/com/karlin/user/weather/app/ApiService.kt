package com.karlin.user.weather.app

import com.karlin.user.weather.models.GetCitiesModel
import com.karlin.user.weather.models.GetWeatherModel
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url
import okhttp3.ResponseBody


/**
 *
 * Created by User on 09.01.2018.
 */

interface ApiService {

    @GET
    fun getCities(@Url url: String, @Query("input") input: String, @Query("types") types: String,
                  @Query("language") language: String, @Query("components") components: String,
                  @Query("key") key: String): Observable<GetCitiesModel>

    @GET
    fun getWeather(@Url url: String, @Query("q") q: String, @Query("units") units: String,
                   @Query("lang") lang: String, @Query("APPID") APPID: String
                   ): Observable<GetWeatherModel>


}