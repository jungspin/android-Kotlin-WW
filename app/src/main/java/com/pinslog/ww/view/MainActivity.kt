package com.pinslog.ww.view


import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.pinslog.ww.R
import com.pinslog.ww.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    private lateinit var mContext: Context


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mContext = this
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initAppbar()

    }

    // TODO 해결하기
    private fun initAppbar() {
    }


}
