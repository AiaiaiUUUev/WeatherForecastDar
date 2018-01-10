package com.karlin.user.weather.models

import io.realm.RealmObject

/**
 *
 * Created by User on 10.01.2018.
 */

open class CitiesWeatherModel(var nameCity: String, var temperature: String?,var data: String?) : RealmObject() {
    constructor() : this(nameCity = "", temperature = "", data = "")
}

open class CitiesRequest(var nameCity: String, var data: String?) : RealmObject() {
    constructor() : this(nameCity = "", data = "")
}
