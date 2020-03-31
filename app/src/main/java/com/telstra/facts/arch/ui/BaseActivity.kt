package com.telstra.facts.arch.ui

import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.telstra.facts.R
import dagger.android.support.DaggerAppCompatActivity

abstract class BaseActivity : DaggerAppCompatActivity() {

    abstract val layoutResId: Int

    lateinit var navController: NavController

    protected open val navControllerId: Int = R.id.nav_host_fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Create view
        setupContentView()
        // Setup navigation
        setupNavigation(savedInstanceState)

    }

    private fun setupNavigation(savedInstanceState: Bundle?) {
        if (navControllerId != 0) {
            navController = Navigation.findNavController(this, navControllerId)
            setupNavigation(navController, savedInstanceState)

        }
    }

    protected open fun setupContentView() {
        setContentView(layoutResId)
    }

    protected open fun setupNavigation(navController: NavController, savedInstanceState: Bundle?) {
    }


}
