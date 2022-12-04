package com.pinslog.ww.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pinslog.ww.model.ForecastDO
import com.pinslog.ww.databinding.ItemForecastBinding

private const val TAG = "ForecastAdapter"

class ForecastAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var forecastList = mutableListOf<ForecastDO?>()

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

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(itemList: MutableList<ForecastDO?>) {
        forecastList.addAll(itemList)
        notifyDataSetChanged()
    }


    inner class ItemViewHolder(private val binding: ItemForecastBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {


        }

        @SuppressLint("SetTextI18n")
        fun onBind(forecastDO: ForecastDO?) {
            binding.itemForecastDate.text = "${forecastDO?.dateString}"
            binding.itemForecastDay.text = "${forecastDO?.day}"
            forecastDO?.resourceId?.let { binding.itemForecastIcon.setImageResource(it) }
            binding.itemForecastMaxTemp.text = "${forecastDO?.maxTemp}"
            binding.itemForecastMinTemp.text = "${forecastDO?.minTemp}"
        }
    }


}