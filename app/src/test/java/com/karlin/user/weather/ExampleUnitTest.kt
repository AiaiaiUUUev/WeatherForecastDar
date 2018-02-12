package com.karlin.user.weather

import com.karlin.user.weather.app.App.Companion.api
import io.reactivex.Observable
import io.realm.RealmObject
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class ExampleUnitTest {

    @Test
    fun addition_isCorrect() {
        val objects = listOf<SomeObject>(
                SomeObject(listOf(Price(1.0), Price(0.0))),
                SomeObject(listOf(Price(3.0), Price(4.0))),
                SomeObject(listOf(Price(5.0), Price(6.0)))
        )
        objects.stream()
                .flatMap { it.prices.stream() }
                .map { it.price }
//                .filter(it.com > 2)
                .forEach { println(it) }


    }

    class Price(val price: Double)
    class SomeObject(val prices: List<Price>)


//    @Test
//    fun zip() {
//        Observable.zip(api!!.getCities("https://maps.googleapis.com/maps/api/place/autocomplete/json"
//                , "as", "(cities)", "ru", "country:kz"
//                , "AIzaSyCJRNkELEbvJXW1fXOQEy5B0oU5Ca7sjtU"),
//                api!!.getWeather("https://api.openweathermap.org/data/2.5/weather",
//                        "Astana", "metric", "ru",
//                        "fcb1781d7b858573e652ddab5ef5f01b")
//        ) { s,s  ->  }
//    }

}