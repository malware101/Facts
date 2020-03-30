package com.telstra.facts.arch.ui

import android.os.Bundle
import dagger.android.support.DaggerAppCompatActivity

abstract class  BaseActivity : DaggerAppCompatActivity() {

    abstract val layoutResId: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutResId)
    }
}
