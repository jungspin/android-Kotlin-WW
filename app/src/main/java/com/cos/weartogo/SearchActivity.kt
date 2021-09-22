package com.cos.weartogo

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.SearchView
import com.cos.weartogo.databinding.ActivityMainBinding
import com.cos.weartogo.databinding.ActivitySearchBinding

private const val TAG = "SearchActivity"

class SearchActivity : AppCompatActivity() {

    private var mContext: Context = this
    private lateinit var binding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.svSearch.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {

                if (p0 != null) {
                    Log.d(TAG, "onQueryTextSubmit: $p0")
                    var intent = Intent()
                    intent.putExtra("data", p0)
                    setResult(RESULT_OK, intent)
                    finish()
                }
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                //Log.d(TAG, "onQueryTextChange: ")
                return false
            }

        })
    }
}