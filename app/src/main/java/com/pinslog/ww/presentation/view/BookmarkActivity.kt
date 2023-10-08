package com.pinslog.ww.presentation.view

import android.view.LayoutInflater
import com.google.android.material.tabs.TabLayoutMediator
import com.pinslog.ww.base.BaseActivity
import com.pinslog.ww.databinding.ActivityBookmarkBinding
import com.pinslog.ww.presentation.view.adapter.BookmarkViewPagerAdapter

class BookmarkActivity : BaseActivity<ActivityBookmarkBinding>() {
    override fun getBinding(inflater: LayoutInflater): ActivityBookmarkBinding {
        binding = ActivityBookmarkBinding.inflate(inflater)
        return binding
    }

    override fun initListener() {
        binding.addLocationCloseBtn.setOnClickListener { finish() }
    }

    override fun initSetting() {
        val tabLayout = binding.bookmarkTabLayout
        val viewPager = binding.bookmarkViewPager
        val tabTitleArray = arrayOf("즐겨찾기", "추가하기")

        viewPager.adapter = BookmarkViewPagerAdapter(supportFragmentManager, lifecycle)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabTitleArray[position]
        }.attach()
    }



}