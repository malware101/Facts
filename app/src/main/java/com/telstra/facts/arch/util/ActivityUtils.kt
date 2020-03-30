package com.telstra.facts.arch.util

import androidx.fragment.app.FragmentActivity
import com.telstra.facts.R
import com.telstra.facts.arch.ui.ErrorDialogFragment

fun FragmentActivity.showAlert(
    msg: CharSequence?,
    title: CharSequence?,
    positiveButton: CharSequence? = getString(R.string.ok),
    negativeButton: CharSequence? = null,
    neutralButton: CharSequence? = null,
    cancelable: Boolean = true,
    listener: ((Int) -> Unit)? = null
) = supportFragmentManager.let { manager ->
    ErrorDialogFragment.show(
        title ?: getString(R.string.title_error_dialog),
        msg ?: getString(R.string.generic_message_error_dialog),
        positiveButton ?: getString(R.string.ok),
        negativeButton,
        neutralButton,
        cancelable,
        manager,
        listener
    )
}