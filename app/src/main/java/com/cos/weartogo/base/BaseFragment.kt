package com.cos.weartogo.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import java.security.Permission
import android.Manifest.permission.*
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat

const val REQUEST_CODE = 1000

abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    protected lateinit var binding: VB
    protected lateinit var mContext: Context
    protected lateinit var inflateView: ViewGroup
    protected var isAllowed = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onStart() {
        super.onStart()
        checkPermission()
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

    // TODO 해결해야함
    private fun checkPermission() {
        val requirePermissions = arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION)
        val rejectPermissionList = ArrayList<String>()

        requestPermissionLauncher.launch(requirePermissions)
        for (permission in requirePermissions) {
            if (ActivityCompat.checkSelfPermission(
                    mContext,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                isAllowed = false
                rejectPermissionList.add(permission)
            }
        }

        if (rejectPermissionList.isNotEmpty()) {
            val array = arrayOfNulls<String>(rejectPermissionList.size)
            ActivityCompat.requestPermissions(
                requireActivity(), rejectPermissionList.toArray(array),
                REQUEST_CODE
            )
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results ->
            for (result in results.values) {
                isAllowed = result
            }
//            if (isAllowed) {
//                // 하던거 하면됨
//            } else {
//                // 말려야됨
//            }
        }
}