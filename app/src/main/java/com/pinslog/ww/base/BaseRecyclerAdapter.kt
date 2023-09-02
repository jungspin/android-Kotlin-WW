package com.pinslog.ww.base

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * RecyclerView Adapter 의 base 클래스
 *
 * @param VB inflate 할 ViewBinding
 * @param T 출력할 데이터
 * @author jungspin
 * @since 2023-03-15
 */
abstract class BaseRecyclerAdapter<VB : ViewBinding, T> : RecyclerView.Adapter<BaseViewHolder<VB>>() {
    private lateinit var binding: VB
    var dataList = mutableListOf<T>()

    /**
     * inflate 된 ViewBinding 을 반환 합니다
     *
     * @param layoutInflater
     * @return viewBinding
     */
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

    /**
     * 리스트를 셋팅 합니다
     */
    abstract fun setItems(dataList: MutableList<T>)

    /**
     * 리스트를 초기화 합니다
     */
    @SuppressLint("NotifyDataSetChanged")
    fun clearItems() {
        dataList.clear()
        notifyDataSetChanged()
    }

    /**
     * ViewHolder 관련 작업을 핸들링 합니다
     *
     * @param viewHolder 해당 viewHolder
     * @param data       바인딩 할 데이터
     * @param position   위치
     */
    private fun handlingViewHolder(viewHolder: BaseViewHolder<VB>, data: T, position: Int){
        bind(viewHolder, data)
        viewHolder.itemView.setOnClickListener { itemClickListener(viewHolder, data, position) }
    }

    /**
     * ViewHolder 에 데이터를 바인딩 합니다
     *
     * @param viewHolder viewHolder
     * @param data       데이터
     */
    abstract fun bind(viewHolder: BaseViewHolder<VB>, data: T)

    /**
     * ViewHolder 의 클릭 이벤트 발생 시
     * 수행할 행위를 정의 합니다
     *
     * @param viewHolder viewHolder
     * @param data       데이터
     * @param position   위치
     */
    abstract fun itemClickListener(viewHolder: BaseViewHolder<VB>, data: T, position: Int)

}