package com.cos.weartogo.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cos.weartogo.R
import com.cos.weartogo.base.BaseFragment
import com.cos.weartogo.databinding.FragmentBookmarkBinding

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