package com.crabgore.moviesDB

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.get
import androidx.navigation.ui.NavigationUI
import com.crabgore.moviesDB.common.hideKeyboard
import com.crabgore.moviesDB.databinding.ActivityMainBinding
import com.crabgore.moviesDB.ui.base.BaseFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    private val navController by lazy { findNavController(R.id.nav_host_fragment) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding?.root
        setContentView(view)
        initUI()
    }

    private fun initUI() {
        setUpNavigation()
    }

    private fun setUpNavigation() {
        NavigationUI.setupWithNavController(binding!!.btv, navController)
        btv.setOnNavigationItemReselectedListener {
            navController.popBackStack(it.itemId, false)
        }
    }

    fun showLoader() {
        binding?.loaderLayout?.visibility = VISIBLE
    }

    fun hideLoader() {
        binding?.loaderLayout?.visibility = GONE
    }
}