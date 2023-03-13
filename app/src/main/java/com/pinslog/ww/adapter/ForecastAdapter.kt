package com.pinslog.ww.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.pinslog.ww.R
import com.pinslog.ww.databinding.ItemForecastBinding
import com.pinslog.ww.databinding.ItemWearingInfoBinding
import com.pinslog.ww.model.ForecastDO
import com.pinslog.ww.util.Utility

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

    /**
     * item 들을 추가합니다.
     */
    @SuppressLint("NotifyDataSetChanged")
    fun setItems(itemList: MutableList<ForecastDO?>) {
        forecastList.addAll(itemList)
        //notifyItemRangeInserted(0, itemList.size - 1)
        notifyDataSetChanged()
    }

    /**
     * item 들을 삭제합니다.
     */
    @SuppressLint("NotifyDataSetChanged")
    fun clearItems() {
        //notifyItemRangeRemoved(0, forecastList.size - 1)
        forecastList.clear()
        notifyDataSetChanged()
    }


    inner class ItemViewHolder(private val binding: ItemForecastBinding) :
        RecyclerView.ViewHolder(binding.root) {

        var mContext: Context = binding.root.context
        var wearingInfoBinding: ItemWearingInfoBinding = binding.itemWearingInfoRoot

        private val clickListener = View.OnClickListener {
            if (binding.itemWearingRoot.visibility != View.VISIBLE) {
                binding.itemInfoArrowIv.setImageResource(R.drawable.ic_arrow_up)
                TransitionManager.beginDelayedTransition(binding.materialCardView, AutoTransition())
                binding.itemWearingRoot.visibility = View.VISIBLE
            } else {
                binding.itemInfoArrowIv.setImageResource(R.drawable.ic_arrow_down)
                TransitionManager.beginDelayedTransition(binding.itemWearingRoot, AutoTransition())
                binding.itemWearingRoot.visibility = View.GONE
            }

        }


        @SuppressLint("SetTextI18n")
        fun onBind(forecastDO: ForecastDO?) {
            binding.itemWearingRoot.visibility = View.GONE
            binding.itemForecastRoot.setOnClickListener(clickListener)
            binding.itemInfoArrowIv.setOnClickListener(clickListener)
            binding.itemForecastDate.text = "${forecastDO?.dateString}"
            binding.itemForecastDay.text = "${forecastDO?.day}"
            forecastDO?.resourceId?.let { binding.itemForecastIcon.setImageResource(it) }
            binding.itemForecastMaxTemp.text = "${forecastDO?.maxTemp}°"
            binding.itemForecastMinTemp.text = "${forecastDO?.minTemp}°"

            val pop = forecastDO?.pop
            if (pop == 0.0) {
                binding.itemForecastPopRoot.visibility = View.GONE
            } else {
                binding.itemForecastPop.text = "${forecastDO?.pop}"
                binding.itemForecastPopRoot.visibility = View.VISIBLE
            }


            getTemp(forecastDO?.maxTemp.toString())
            binding.itemWearingMaxBtn.setOnClickListener {
                getTemp(forecastDO?.maxTemp.toString())
            }
            binding.itemWearingMinBtn.setOnClickListener {
                getTemp(forecastDO?.minTemp.toString())
            }

        }

        private fun getTemp(temp: String) {
            val wearInfo = Utility.getWearingInfo(mContext, temp)
            val infoList = wearInfo.wearingList
            wearingInfoBinding.itemWearingDescription.text = wearInfo.infoDescription
            wearingInfoBinding.itemWearing1.setImageResource(infoList[0])
            wearingInfoBinding.itemWearing2.setImageResource(infoList[1])
            wearingInfoBinding.itemWearing3.setImageResource(infoList[2])

            wearingInfoBinding.itemWearingDescription.setOnClickListener {
                wearingInfoBinding.itemWearing1.visibility = View.VISIBLE
                wearingInfoBinding.itemWearing2.visibility = View.VISIBLE
                wearingInfoBinding.itemWearing3.visibility = View.VISIBLE
                wearingInfoBinding.itemWearingDescription.visibility = View.INVISIBLE
            }
        }
    }


}