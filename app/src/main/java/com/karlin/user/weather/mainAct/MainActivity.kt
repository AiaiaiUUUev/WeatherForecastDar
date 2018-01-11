package com.karlin.user.weather.mainAct

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.karlin.user.weather.R
import kotlinx.android.synthetic.main.activity_main.*
import com.jakewharton.rxbinding2.widget.RxTextView
import com.karlin.user.weather.models.CitiesRequest
import com.karlin.user.weather.models.CitiesWeatherModel
import io.realm.Realm
import java.text.SimpleDateFormat
import java.util.*
import org.joda.time.DateTime
import org.joda.time.Hours


class MainActivity : AppCompatActivity(), MainActivityContract.View {


    override fun saveDataQuery(it: MutableList<CitiesWeatherModel>) {
        val list = mRealm!!.where(CitiesWeatherModel::class.java).findAll()
        if (!list.isEmpty()) {
            mRealm!!.beginTransaction()
            list.deleteAllFromRealm()
            mRealm!!.commitTransaction()
        }
        it.forEach({
            mRealm!!.beginTransaction()
            val cW = mRealm!!.createObject(CitiesWeatherModel::class.java)
            cW.nameCity = it.nameCity
            cW.temperature = it.temperature
            mRealm!!.commitTransaction()
        })
    }

    override fun saveQueryET(query: String) {
        val list = mRealm!!.where(CitiesRequest::class.java).findAll()
        if (!list.isEmpty()) {
            mRealm!!.beginTransaction()
            list.deleteAllFromRealm()
            mRealm!!.commitTransaction()
        }

        mRealm!!.beginTransaction()
        val cN = mRealm!!.createObject(CitiesRequest::class.java)
        cN.nameCity = query
        cN.data = SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.US).format(Calendar.getInstance().time)
        mRealm!!.commitTransaction()
    }

    private val presenter = MainActivityPresenter()
    private var mRealm: Realm? = null
    private var firstLaunch = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter.setView(this)
        mRealm = Realm.getDefaultInstance()

        recycler_view!!.layoutManager = LinearLayoutManager(this)

        checkAvailability()


        RxTextView
                .textChanges(mainET)
                .subscribe({ city ->
                    if (city.length >= 2 && firstLaunch) {
                        presenter.pushCity(city.toString())
                    } else if (city.isBlank()) {
                        recycler_view.visibility = View.INVISIBLE
                    } else if (!firstLaunch) {
                        firstLaunch = true
                    }
                })

    }

    override fun updateTemp(list: List<CitiesWeatherModel>) {
        (recycler_view.adapter as AdapterCities).updateTemp(list)
    }

    override fun showCities(cities: MutableList<CitiesWeatherModel>) {
        recycler_view.visibility = View.VISIBLE
        recycler_view!!.adapter = AdapterCities(cities, this)

    }

    override fun showProgress(show: Boolean) {
        if (show) {
            progressBar.visibility = View.VISIBLE
//            loadingIV.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
//            loadingIV.visibility = View.GONE
        }
    }

    private fun hideKeyboard(ctx: Context) {
        val inputManager = ctx
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        // check if no view has focus:
        val v = (ctx as Activity).currentFocus ?: return

        inputManager.hideSoftInputFromWindow(v.windowToken, 0)
    }

    override fun updateModel(temp: String?) {

    }

    override fun onResume() {
        super.onResume()
        presenter.bind()
    }

    override fun onDestroy() {
        super.onDestroy()

    }

    override fun onPause() {
        super.onPause()
        presenter.unBind()
//        mRealm!!.close()

    }

    private val format = SimpleDateFormat("MM/dd/yyyy HH:mm:ss")
    @SuppressLint("SimpleDateFormat")
    private fun checkAvailability() {

        val list = mRealm!!.where(CitiesWeatherModel::class.java).findAll()
        val query = mRealm!!.where(CitiesRequest::class.java).findAll()
        if (list.size != 0) {


            recycler_view!!.adapter = AdapterCities(mRealm!!.copyFromRealm(list), this)
            mainET.setText(query[0]?.nameCity)
//            Handler().postDelayed({},250)

            val currentTime = SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.US).format(Calendar.getInstance().time)

            val d1 = format.parse(currentTime)
            val d2 = format.parse(query[0]?.data)

            val dt1 = DateTime(d1)
            val dt2 = DateTime(d2)

            if ((Hours.hoursBetween(dt1, dt2).hours % 24) > 1) {
                presenter.pushCity(query[0]?.nameCity!!)
            }
        }

    }
}

