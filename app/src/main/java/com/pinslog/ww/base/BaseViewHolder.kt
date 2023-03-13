package com.pinslog.ww.base

import android.view.View
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * ViewHolder Base Class
 *
 * @param <VB> ViewBinding
 * @author jungspin
 * @since 2023-03-13
 *
 */
class BaseViewHolder<VB : ViewBinding?> : RecyclerView.ViewHolder {
    var vb: VB? = null

    constructor(itemView: View?) : super(itemView!!)
    constructor(itemView: View?, vb: VB) : super(itemView!!) {
        this.vb = vb
    }
}