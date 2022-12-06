package com.pinslog.ww.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.pinslog.ww.R
import com.pinslog.ww.model.ForecastDO
import com.pinslog.ww.databinding.ItemForecastBinding
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

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(itemList: MutableList<ForecastDO?>) {
        forecastList.addAll(itemList)
        notifyDataSetChanged()
    }


    inner class ItemViewHolder(private val binding: ItemForecastBinding) :
        RecyclerView.ViewHolder(binding.root) {

        var mContext: Context
        var isMaxButtonClicked = true

        init {
            mContext = binding.root.context
        }

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

        private val toggleButtonClickListener = View.OnClickListener {
            isMaxButtonClicked =
                binding.itemWearingToggleRoot.checkedButtonId != binding.itemWearingMaxBtn.id
        }

        @SuppressLint("SetTextI18n")
        fun onBind(forecastDO: ForecastDO?) {
            binding.itemForecastRoot.setOnClickListener(clickListener)
            binding.itemInfoArrowIv.setOnClickListener(clickListener)
//            val front = binding.itemForecastFront
//            val back = binding.itemForecastBack.itemInfo
            binding.itemForecastDate.text = "${forecastDO?.dateString}"
            binding.itemForecastDay.text = "${forecastDO?.day}"
            forecastDO?.resourceId?.let { binding.itemForecastIcon.setImageResource(it) }
            binding.itemForecastMaxTemp.text = "${forecastDO?.maxTemp}"
            binding.itemForecastMinTemp.text = "${forecastDO?.minTemp}"
            
            getTemp(forecastDO?.maxTemp.toString())
            binding.itemWearingMaxBtn.setOnClickListener {
                getTemp(forecastDO?.maxTemp.toString())
            }
            binding.itemWearingMinBtn.setOnClickListener {
                getTemp(forecastDO?.minTemp.toString())
            }

        }

        fun getTemp(temp : String){
            val wearInfo = Utility.getWearingInfo(mContext, temp)
            val infoList = wearInfo.wearingList
            binding.itemWearingDescription.text = wearInfo.infoDescription
            binding.itemWearing1.setImageResource(infoList[0])
            binding.itemWearing2.setImageResource(infoList[1])
            binding.itemWearing3.setImageResource(infoList[2])

            binding.itemWearingDescription.setOnClickListener {
                binding.itemWearing1.visibility = View.VISIBLE
                binding.itemWearing2.visibility = View.VISIBLE
                binding.itemWearing3.visibility = View.VISIBLE
                binding.itemWearingDescription.visibility = View.INVISIBLE
            }
        }
    }


}