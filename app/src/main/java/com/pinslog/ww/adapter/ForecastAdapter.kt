package com.pinslog.ww.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.pinslog.ww.R
import com.pinslog.ww.base.BaseRecyclerAdapter
import com.pinslog.ww.base.BaseViewHolder
import com.pinslog.ww.databinding.ItemForecastBinding
import com.pinslog.ww.model.ForecastDO
import com.pinslog.ww.util.Utility

private const val TAG = "ForecastAdapter"
class ForecastAdapter : BaseRecyclerAdapter<ItemForecastBinding, ForecastDO?>() {

    private lateinit var mContext: Context

    /**
     * item 들을 삭제합니다.
     */
    @SuppressLint("NotifyDataSetChanged")
    fun clearItems() {
        notifyItemRangeRemoved(0, dataList.size - 1)
        dataList.clear()
        notifyDataSetChanged()
    }

    override fun getViewBinding(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): ItemForecastBinding {
        mContext = parent.context
        return ItemForecastBinding.inflate(layoutInflater, parent, false)
    }

    override fun itemClickListener(
        viewHolder: BaseViewHolder<ItemForecastBinding>,
        t: ForecastDO?,
        position: Int
    ) {
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
    override fun bind(viewHolder: BaseViewHolder<ItemForecastBinding>, data: ForecastDO?) {
        viewHolder.vb?.run {
            this.itemForecastRoot.setOnClickListener(callClickListener(viewHolder))
            this.itemInfoArrowIv.setOnClickListener(callClickListener(viewHolder))
            this.itemForecastDate.text = "${data?.dateString}"
            this.itemForecastDay.text = "${data?.day}"
            data?.resourceId.let {
                if (it != null) {
                    this.itemForecastIcon.setImageResource(it)
                }
            }
            this.itemForecastMaxTemp.text = "${data?.maxTemp}°"
            this.itemForecastMinTemp.text = "${data?.minTemp}°"

            val pop = data?.pop
            if (pop == 0.0) {
                this.itemForecastPopRoot.visibility = View.GONE
            } else {
                this.itemForecastPop.text = "${data?.pop}"
                this.itemForecastPopRoot.visibility = View.VISIBLE
            }


            getTemp(data?.maxTemp.toString())
            this.itemWearingMaxBtn.setOnClickListener {
                getTemp(data?.maxTemp.toString())
            }
            this.itemWearingMinBtn.setOnClickListener {
                getTemp(data?.minTemp.toString())
            }
        }
        

        
    }

    private fun callClickListener(viewHolder: BaseViewHolder<ItemForecastBinding>): View.OnClickListener {
        val binding = viewHolder.vb!!
        return View.OnClickListener {
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
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun setItems(dataList: MutableList<ForecastDO?>) {
        this.dataList.addAll(dataList)
        notifyItemRangeInserted(0, dataList.size - 1)
        //notifyDataSetChanged()
    }

    private fun getTemp(temp: String) {
        val wearInfo = Utility.getWearingInfo(mContext, temp)
        val infoList = wearInfo.wearingList
        val wearingInfoBinding = binding.itemWearingInfoRoot
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