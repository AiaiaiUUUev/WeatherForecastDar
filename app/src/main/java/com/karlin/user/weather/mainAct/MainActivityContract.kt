package com.karlin.user.weather.mainAct

import com.karlin.user.weather.models.CitiesWeatherModel
import io.reactivex.Observable
import java.util.*

/**
 *
 * Created by User on 09.01.2018.
 */

class MainActivityContract {
    interface View {
        fun showProgress(show: Boolean)
        fun showCities(cities: MutableList<CitiesWeatherModel>)
        fun updateModel(temp: String?)
        fun updateTemp(list: List<CitiesWeatherModel>)
        fun saveQueryET(query: String)
        fun saveDataQuery(it: MutableList<CitiesWeatherModel>)
    }

    interface Presenter {
        //        fun loadCitiesWeather(subject: PublishSubject<String>?)
        fun setView(view: MainActivityContract.View)
        fun zip()
        fun pushCity(city: CharSequence)
        fun bind()
        fun unBind()
    }

}