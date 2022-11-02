package com.cos.weartogo.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cos.weartogo.data.openweather.OpenWeather
import com.cos.weartogo.databinding.ItemForecastBinding
import com.cos.weartogo.util.Utility

private const val TAG = "ForecastAdapter"
class ForecastAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var forecastList = mutableListOf<OpenWeather>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemForecastBinding.inflate(layoutInflater, parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ItemViewHolder).onBind(forecastList[position])
    }

    override fun getItemCount(): Int {
        return forecastList.size
    }

    fun setItems(itemList: MutableList<OpenWeather>) {
        forecastList.addAll(itemList)
        notifyDataSetChanged()
    }


    inner class ItemViewHolder(private val binding: ItemForecastBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {


        }

        @SuppressLint("SetTextI18n")
        fun onBind(openWeather: OpenWeather) {
//            var date = openWeather.dt_txt.split(" ")
//            date = date[0].split("-")
//            binding.itemForecastDate.text = "${date[1]}/${date[2]}"
            val dateTxt = openWeather.dt_txt.split(" ")
            val day = dateTxt[0]
            val date = dateTxt[1]
            Log.d(TAG, "day : $day, date : $date")


            val tempMax = openWeather.main.temp_max
            val tempMin = openWeather.main.temp_min
            binding.itemForecastMaxTemp.text = Utility.getRealTemp(tempMax)
            binding.itemForecastMinTemp.text = Utility.getRealTemp(tempMin)
        }
    }


}