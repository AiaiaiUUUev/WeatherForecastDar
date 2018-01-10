package com.karlin.user.weather.mainAct

import android.content.Context
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.karlin.user.weather.R
import com.karlin.user.weather.models.CitiesWeatherModel


/**
 *
 * Created by User on 10.01.2018.
 */

class AdapterCities(private val documents: MutableList<CitiesWeatherModel>, private val context: Context, private val recyclerView: RecyclerView? = null)
    : RecyclerView.Adapter<AdapterCities.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_city_weather, parent, false)

        return ViewHolder(view as View)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val city = documents[position]
        holder.nameCity.text = city.nameCity
        if (city.temperature != null) {
            if (city.temperature == "погода не найдена") {
                holder.temperature.textSize = 12f
                holder.temperature.text = city.temperature

            } else {
                holder.temperature.text = String.format(context.resources.getString(R.string.temperature), city.temperature, "C")
            }
        }
        holder.progressBar.visibility = if (city.temperature.isNullOrBlank()) View.VISIBLE else View.INVISIBLE
    }

    override fun getItemCount(): Int {
        return documents.size
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        internal var nameCity: TextView = v.findViewById<TextView>(R.id.nameCity)
        internal var temperature: TextView = v.findViewById<TextView>(R.id.temperatureCity)
        internal var progressBar: ProgressBar = v.findViewById<ProgressBar>(R.id.progressBar)


    }

    fun updateTemp(list: List<CitiesWeatherModel>) {
        val result = DiffUtil.calculateDiff(MyDiff(this.documents, list))
        this.documents.clear()
        this.documents.addAll(list)

//        notifyDataSetChanged()
        result.dispatchUpdatesTo(this)

    }

    class MyDiff(private var oldList: List<CitiesWeatherModel>, private var newList: List<CitiesWeatherModel>) : DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldCities = oldList[oldItemPosition]
            val newCities = newList[newItemPosition]
            return oldCities.nameCity == newCities.nameCity

        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldCities = oldList[oldItemPosition]
            val newCities = newList[newItemPosition]
            return oldCities.temperature.equals(newCities.temperature)
                    && oldCities.nameCity == newCities.nameCity

        }
    }


}
