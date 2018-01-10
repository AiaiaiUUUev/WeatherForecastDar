package com.karlin.user.weather.mainAct

import com.karlin.user.weather.app.App
import com.karlin.user.weather.models.GetWeatherModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import android.util.Log
import com.karlin.user.weather.models.CitiesWeatherModel


/**
 *
 * Created by User on 09.01.2018.
 */

class MainActivityPresenter : MainActivityContract.Presenter {
    private var view: MainActivityContract.View? = null
    private val disposable = CompositeDisposable()
    private var subject = PublishSubject.create<String>()


    override fun pushCity(city: CharSequence) {
        view!!.showProgress(true)
        subject.onNext(city.toString())
    }

    override fun unBind() {
        disposable.clear()
    }

    override fun bind() {
        disposable.add(subject
                .debounce(300, TimeUnit.MILLISECONDS)
                .filter({ it -> return@filter !it.isBlank() })
                .distinctUntilChanged()
                .subscribe({
                    Log.d(tag, "finding: ${it}")
                    loadCities(it)
                }, {
                    Log.d(tag, "error:${it.message}")
                }))
    }


    private val tag = "qwerty"

    private fun loadCities(city: String) {

        disposable.add(App.api!!.getCities("https://maps.googleapis.com/maps/api/place/autocomplete/json"
                , city, "(cities)", "ru", "country:kz"
                , "AIzaSyCJRNkELEbvJXW1fXOQEy5B0oU5Ca7sjtU")
                .switchMap { Observable.fromIterable(it.predictions) }
                .map { CitiesWeatherModel(it.structuredFormatting?.mainText!!, null, null) }
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            view!!.saveQueryET(city)
                            view?.showProgress(false)
                            view?.showCities(it)
                            findWeather(it)
                        },
                        {
                            view?.showProgress(false)
                        }))
    }

    private fun findWeather(cities: List<CitiesWeatherModel>) {
        Observable
                .fromIterable(cities)
                .flatMap { c ->
                    App.api?.getWeather("https://api.openweathermap.org/data/2.5/weather",
                            c.nameCity, "metric", "ru", "fcb1781d7b858573e652ddab5ef5f01b")!!
                            .onErrorReturnItem(GetWeatherModel())
                            .doOnNext {
                                it.city = CitiesWeatherModel(c.nameCity, "", "")
                            }
                }
                .subscribeOn(Schedulers.io())
                .map {
                    if (it.main?.temp != null) {
                        it.city?.temperature = it.main?.temp
                    } else {
                        it.city?.temperature = "погода не найдена"
                    }
                    it.city!!
                }
                .observeOn(AndroidSchedulers.mainThread())
                .toList()
                .subscribe(
                        {
                            view?.updateTemp(it)
                            view?.saveDataQuery(it)
                        },
                        { Log.d(tag, "error: ${it.message}") }
                )

    }

    override fun setView(view: MainActivityContract.View) {
        this.view = view
    }


    /*override fun loadCitiesWeather() {
        list.clear()
        subject!!
                .debounce(300, TimeUnit.MILLISECONDS)
                .filter({ it -> return@filter !it.isBlank() })
                .distinctUntilChanged()
                .switchMap { t: String ->
                    App.api!!.getCities("https://maps.googleapis.com/maps/api/place/autocomplete/json"
                            , t, "(cities)", "ru", "country:kz"
                            , "AIzaSyCJRNkELEbvJXW1fXOQEy5B0oU5Ca7sjtU")
                }
                .flatMap { t: GetCitiesModel ->
                    Observable.fromIterable(t.predictions)
                }
                .doOnComplete {
                    Log.d("FFFFFFF", "закончил итерацию")
                }
                .flatMap { t: Prediction ->
                    App.api!!.getWeather("https://api.openweathermap.org/data/2.5/weather",
                            t.structuredFormatting!!.mainText!!, "metric", "ru",
                            "fcb1781d7b858573e652ddab5ef5f01b")
                            .doOnError({ error -> error.take(1).delay(1, TimeUnit.SECONDS) })
                }
                .doOnNext { t -> list.add(t) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ t -> successLoadCities(t) },
                        { t -> failedLoadCities(t) },
                        { completeMethod() })


    }*/


}

