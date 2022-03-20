 package com.cos.weartogo.view


 import android.os.Bundle
 import android.widget.Toolbar
 import androidx.appcompat.app.AppCompatActivity
 import androidx.navigation.Navigation
 import androidx.navigation.findNavController
 import androidx.navigation.fragment.NavHostFragment
 import androidx.navigation.fragment.findNavController
 import androidx.navigation.ui.setupWithNavController
 import com.cos.weartogo.R
 import com.cos.weartogo.databinding.ActivityMainBinding
 import com.google.android.material.bottomnavigation.BottomNavigationMenuView
 import com.google.android.material.bottomnavigation.BottomNavigationView


 class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        val navController = navHostFragment.findNavController()
        binding.bottomNav.setupWithNavController(navController)




    }

}