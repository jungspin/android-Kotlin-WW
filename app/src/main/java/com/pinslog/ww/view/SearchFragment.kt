package com.pinslog.ww.view

import android.view.LayoutInflater
import android.view.ViewGroup
import com.pinslog.ww.base.BaseFragment
import com.pinslog.ww.databinding.FragmentSearchBinding


class SearchFragment : BaseFragment<FragmentSearchBinding>() {
    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSearchBinding {
        return FragmentSearchBinding.inflate(inflater)
    }

}