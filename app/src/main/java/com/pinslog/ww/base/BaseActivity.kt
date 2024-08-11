package com.pinslog.ww.base

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
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
        checkPermission()
        initSetting()
        initData()
        initListener()


    }

    protected abstract fun getBinding(inflater: LayoutInflater): VB

    protected open fun initSetting() {}
    protected open fun initData() {}
    protected open fun initListener() {}

    protected open fun doNextAfterGranted(){}

    /**
     * 권한을 확인합니다.
     * 필요 권한
     * - ACCESS_FINE_LOCATION
     * - ACCESS_COARSE_LOCATION
     */
    private fun checkPermission(){
        val requirePermissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
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
                this, rejectList.toArray(array), BaseFragment.REQUEST_CODE
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
                finish()
            }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}