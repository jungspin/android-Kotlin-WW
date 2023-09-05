package com.pinslog.ww.presentation.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.pinslog.ww.base.BaseRecyclerAdapter
import com.pinslog.ww.base.BaseViewHolder
import com.pinslog.ww.databinding.ItemForecastTimeBinding
import com.pinslog.ww.presentation.model.HourlyForecast

class HourlyForecastAdapter : BaseRecyclerAdapter<ItemForecastTimeBinding, HourlyForecast>() {
    private lateinit var mContext: Context
    override fun getViewBinding(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): ItemForecastTimeBinding {
        mContext = parent.context
        return ItemForecastTimeBinding.inflate(layoutInflater, parent, false)
    }

    override fun itemClickListener(
        viewHolder: BaseViewHolder<ItemForecastTimeBinding>,
        data: HourlyForecast,
        position: Int
    ) {
        //
    }

    override fun bind(viewHolder: BaseViewHolder<ItemForecastTimeBinding>, data: HourlyForecast) {
        viewHolder.vb?.run {
            hourlyForecast = data
        }
    }

    override fun setItems(dataList: MutableList<HourlyForecast>) {
        this.dataList.addAll(dataList)
        notifyItemRangeInserted(0, dataList.size - 1)
    }
}