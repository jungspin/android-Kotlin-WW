package com.pinslog.ww.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.pinslog.ww.view.BookmarkFragment
import com.pinslog.ww.view.SearchFragment

const val NUMBERS_OF_TAB = 2
class BookmarkViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return NUMBERS_OF_TAB
    }

    override fun createFragment(position: Int): Fragment {
        when(position){
            0 -> return BookmarkFragment()
            1 -> return SearchFragment()
        }
        return BookmarkFragment()
    }
}
