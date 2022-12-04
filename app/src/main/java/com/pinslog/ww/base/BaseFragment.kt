package com.pinslog.ww.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import android.Manifest.permission.*
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat

const val REQUEST_CODE = 1000
private const val TAG = "BaseFragment"

abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    protected lateinit var binding: VB
    protected lateinit var mContext: Context
    protected lateinit var inflateView: ViewGroup
    protected var isAllowed : Boolean = false

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

        checkPermission()
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
            } else {
                isAllowed = true
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
        }

    /**
     * 권한이 부여되지 않았을 경우
     * 다이얼로그를 띄웁니다.
     */
    private fun showWarning() {
        val alertDialogBuilder = AlertDialog.Builder(mContext)
        alertDialogBuilder.setMessage("권한을 허용하지 않을 경우\n해당 앱을 사용할 수 없습니다.")
            .setPositiveButton("허용하러 가기") { dialog, id ->
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                val uri = Uri.fromParts("package", mContext.packageName, null)
                intent.data = uri
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                mContext.startActivity(intent)
                dialog.dismiss()
            }
            .setNegativeButton("허용 안함") { _, _ ->
                requireActivity().finish()
            }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}