package com.crabgore.moviesDB

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.get
import androidx.navigation.ui.NavigationUI
import com.crabgore.moviesDB.common.hide
import com.crabgore.moviesDB.common.hideKeyboard
import com.crabgore.moviesDB.common.show
import com.crabgore.moviesDB.databinding.ActivityMainBinding
import com.crabgore.moviesDB.ui.base.BaseFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    private val navController by lazy { findNavController(R.id.nav_host_fragment) }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_MoviesDB)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding?.root
        setContentView(view)
        initUI()
    }

    override fun onBackPressed() {
        hideKeyboard()
        when (navController.currentDestination?.id) {
            navController.graph[R.id.peopleDetailsFragment].id,
            navController.graph[R.id.movieDetailsFragment].id,
            navController.graph[R.id.TVDetailsFragment].id -> fragmentBackPressed()
            else -> super.onBackPressed()
        }
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
        binding?.loaderLayout?.show()
    }

    fun hideLoader() {
        binding?.loaderLayout?.hide()
    }

    private fun fragmentBackPressed() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val fragment = navHostFragment.childFragmentManager.fragments[0] as BaseFragment
        fragment.backPressed()
    }
}