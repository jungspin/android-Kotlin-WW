package com.pinslog.ww.base

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseRecyclerAdapter<VB : ViewBinding, T> : RecyclerView.Adapter<BaseViewHolder<VB>>() {
    protected lateinit var binding: VB
    protected val dataList = mutableListOf<T>()

    abstract fun getViewBinding(layoutInflater: LayoutInflater, parent: ViewGroup): VB
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<VB> {
        val inflater = LayoutInflater.from(parent.context)
        binding = getViewBinding(inflater, parent)
        return BaseViewHolder(binding.root, binding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<VB>, position: Int) {
        handlingViewHolder(holder, dataList[holder.adapterPosition], position)
    }

    override fun getItemCount(): Int = dataList.size

    abstract fun setItems(dataList: MutableList<T>)

    private fun handlingViewHolder(viewHolder: BaseViewHolder<VB>, data: T, position: Int){
        bind(viewHolder, data)
        viewHolder.itemView.setOnClickListener { itemClickListener(viewHolder, data, position) }
    }

    abstract fun bind(viewHolder: BaseViewHolder<VB>, data: T)
    abstract fun itemClickListener(viewHolder: BaseViewHolder<VB>, t: T, position: Int)

}