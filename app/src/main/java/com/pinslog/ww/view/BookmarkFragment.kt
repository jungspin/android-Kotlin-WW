package com.pinslog.ww.view

import android.view.LayoutInflater
import android.view.ViewGroup
import com.pinslog.ww.base.BaseFragment
import com.pinslog.ww.databinding.FragmentBookmarkBinding

/**
 * @since 2022-11-01
 */
class BookmarkFragment : BaseFragment<FragmentBookmarkBinding>() {


    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentBookmarkBinding {
        binding = FragmentBookmarkBinding.inflate(inflater, container, false)
        return binding
    }
}