package com.telstra.facts.ui

import android.os.Bundle
import androidx.navigation.NavController
import com.telstra.facts.R
import com.telstra.facts.arch.ui.BaseActivity

class FactsActivity : BaseActivity() {

    override val layoutResId = R.layout.activity_facts

    override fun setupNavigation(navController: NavController, savedInstanceState: Bundle?) {
        navController.setGraph(R.navigation.facts_nav_graph)
    }

    fun setActionBarTitle(title: String?) {
        supportActionBar?.title = title
    }
}
