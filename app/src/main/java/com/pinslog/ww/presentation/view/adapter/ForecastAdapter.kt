package com.pinslog.ww.presentation.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.pinslog.ww.R
import com.pinslog.ww.base.BaseRecyclerAdapter
import com.pinslog.ww.base.BaseViewHolder
import com.pinslog.ww.databinding.ItemForecastBinding
import com.pinslog.ww.model.ForecastDO
import com.pinslog.ww.util.Utility


class ForecastAdapter : BaseRecyclerAdapter<ItemForecastBinding, ForecastDO?>() {

    private lateinit var mContext: Context

    override fun getViewBinding(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): ItemForecastBinding {
        mContext = parent.context
        return ItemForecastBinding.inflate(layoutInflater, parent, false)
    }

    override fun itemClickListener(
        viewHolder: BaseViewHolder<ItemForecastBinding>,
        data: ForecastDO?,
        position: Int
    ) {
        viewHolder.vb?.run {
            setInfoVisibility(this)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun bind(viewHolder: BaseViewHolder<ItemForecastBinding>, data: ForecastDO?) {
        viewHolder.vb?.run {
            data?.let {
                this.item = it
                this.itemWearingInfoRoot.wearInfo = Utility.getWearingInfo(data.maxTemp.toDouble())
                this.executePendingBindings()
                this.itemWearingMaxBtn.setOnClickListener {
                    this.itemWearingInfoRoot.wearInfo = Utility.getWearingInfo(data.maxTemp.toDouble())
                }
                this.itemWearingMinBtn.setOnClickListener {
                    this.itemWearingInfoRoot.wearInfo = Utility.getWearingInfo(data.minTemp.toDouble())
                }
            }

        }
    }

    private fun setInfoVisibility(binding: ItemForecastBinding) {
        if (!binding.itemWearingRoot.isVisible) {
            binding.itemInfoArrowIv.setImageResource(R.drawable.ic_arrow_up)
            TransitionManager.beginDelayedTransition(binding.materialCardView, AutoTransition())
            binding.itemWearingRoot.visibility = View.VISIBLE
        } else {
            binding.itemInfoArrowIv.setImageResource(R.drawable.ic_arrow_down)
            TransitionManager.beginDelayedTransition(binding.itemWearingRoot, AutoTransition())
            binding.itemWearingRoot.visibility = View.GONE
        }
    }

    override fun setItems(dataList: MutableList<ForecastDO?>) {
        this.dataList.addAll(dataList)
        notifyItemRangeInserted(0, dataList.size - 1)
    }


}