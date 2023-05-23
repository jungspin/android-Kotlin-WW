package com.pinslog.ww.base

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

const val REQUEST_CODE = 1000
private const val TAG = "BaseFragment"

abstract class BaseFragment<VB : ViewBinding> : Fragment() {
    protected var isAllowed : Boolean = false

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

        checkPermission()
        initSetting()
        initListener()

        return inflateView
    }

    override fun onResume() {
        super.onResume()
        initViewModel()
        initData()
    }

    protected abstract fun getBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    protected open fun initSetting() {}
    protected open fun initListener() {}
    protected open fun initViewModel() {}
    protected open fun initData() {}
    protected open fun doNextAfterGranted(){}

    /**
     * 권한을 확인합니다.
     * 필요 권한
     * - ACCESS_FINE_LOCATION
     * - ACCESS_COARSE_LOCATION
     */
    protected fun checkPermission(){
        val requirePermissions = arrayOf(
            ACCESS_FINE_LOCATION,
            ACCESS_COARSE_LOCATION
        )
        val rejectPermissionList = ArrayList<String>()

        for (permission in requirePermissions) {
            if (ActivityCompat.checkSelfPermission(
                    mContext,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                rejectPermissionList.add(permission)
            }
        }
        requestPermission(requirePermissions, rejectPermissionList)
    }

    private fun requestPermission(requireArrays: Array<String>, rejectList: ArrayList<String>){
        requestPermissionLauncher.launch(requireArrays)
        if (rejectList.isNotEmpty()) {
            val array = arrayOfNulls<String>(rejectList.size)
            ActivityCompat.requestPermissions(
                requireActivity(), rejectList.toArray(array), REQUEST_CODE
            )
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results ->
            val rejectList = mutableListOf<Boolean>()
            for (result in results.values) {
                if (!result) rejectList.add(result)
            }
            if (rejectList.isNotEmpty()) {
                showDialogForGrant(mContext)
            } else {
                doNextAfterGranted()
            }
        }

    /**
     * 권한이 부여되지 않았을 경우
     * 다이얼로그를 띄웁니다.
     */
    private fun showDialogForGrant(mContext: Context) {
        val alertDialogBuilder = AlertDialog.Builder(mContext)
        alertDialogBuilder.setMessage("권한을 허용하지 않을 경우\n해당 앱을 사용할 수 없습니다.")
            .setPositiveButton("허용하러 가기") { dialog, _ ->
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                val uri = Uri.fromParts("package", mContext.packageName, null)
                intent.data = uri
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                dialog.dismiss()
            }
            .setNegativeButton("허용 안함") { _, _ ->
                requireActivity().finish()
            }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}