package com.pinslog.ww.view

import android.view.LayoutInflater
import com.pinslog.ww.base.BaseActivity
import com.pinslog.ww.databinding.ActivityLookupBookmarkBinding

class LookupBookmarkActivity : BaseActivity<ActivityLookupBookmarkBinding>() {
    override fun getBinding(inflater: LayoutInflater): ActivityLookupBookmarkBinding {
        binding = ActivityLookupBookmarkBinding.inflate(inflater)
        return binding
    }

    override fun initListener() {
        binding.addLocationCloseBtn.setOnClickListener { finish() }
    }

}