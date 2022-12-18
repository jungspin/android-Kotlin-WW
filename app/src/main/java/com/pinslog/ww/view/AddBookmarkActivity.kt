package com.pinslog.ww.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.pinslog.ww.R
import com.pinslog.ww.base.BaseActivity
import com.pinslog.ww.databinding.ActivityAddBookmarkBinding

class AddBookmarkActivity : BaseActivity<ActivityAddBookmarkBinding>() {
    override fun getBinding(inflater: LayoutInflater): ActivityAddBookmarkBinding {
        return ActivityAddBookmarkBinding.inflate(layoutInflater)
    }

}