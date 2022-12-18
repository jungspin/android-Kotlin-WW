package com.pinslog.ww.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<VB: ViewBinding>: AppCompatActivity() {
    protected lateinit var binding: VB
    protected lateinit var mContext: Context
    protected lateinit var inflateView : ViewGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getBinding(layoutInflater)
        inflateView = binding.root as ViewGroup
        setContentView(inflateView)

        mContext = this
        initSetting()
        initData()
        initListener()


    }

    protected abstract fun getBinding(inflater: LayoutInflater): VB

    protected open fun initSetting() {}
    protected open fun initData() {}
    protected open fun initListener() {}
}