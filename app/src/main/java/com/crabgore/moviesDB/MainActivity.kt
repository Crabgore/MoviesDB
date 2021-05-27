package com.crabgore.moviesDB

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.get
import androidx.navigation.ui.NavigationUI
import com.crabgore.moviesDB.common.hideKeyboard
import com.crabgore.moviesDB.ui.base.BaseFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val navController by lazy { findNavController(R.id.nav_host_fragment) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUI()
    }

    override fun onBackPressed() {
        hideKeyboard()
        when (navController.currentDestination?.id) {
            navController.graph[R.id.moviesFragment].id -> fragmentBackPressed()
            else -> super.onBackPressed()
        }
    }

    private fun initUI() {
        setUpNavigation()
    }

    private fun setUpNavigation() {
        NavigationUI.setupWithNavController(btv, navController)
        btv.setOnNavigationItemReselectedListener {
            navController.popBackStack(it.itemId, false)
        }
    }

    private fun fragmentBackPressed() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val fragment = navHostFragment.childFragmentManager.fragments[0] as BaseFragment
        fragment.backPressed()
    }
}