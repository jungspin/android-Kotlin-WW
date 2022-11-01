package com.cos.weartogo.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    protected lateinit var binding: VB
    protected lateinit var mContext: Context
    protected lateinit var inflateView: ViewGroup

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = getBinding(inflater, container)
        inflateView = binding.root as ViewGroup

        initSetting()
        initListener()
        return inflateView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        initData()
    }

    protected abstract fun getBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    protected open fun initSetting() {}
    protected open fun initListener() {}
    protected open fun initViewModel() {}
    protected open fun initData() {}
}