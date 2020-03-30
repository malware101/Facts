package com.telstra.facts.arch.util

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import com.telstra.facts.R

fun Fragment.showAlert(
    msg: CharSequence?,
    title: CharSequence?,
    positiveButton: CharSequence? = getString(R.string.ok),
    negativeButton: CharSequence? = null,
    neutralButton: CharSequence? = null,
    cancelable: Boolean = true,
    listener: ((Int) -> Unit)? = null
) = requireActivity().showAlert(
    msg,
    title,
    positiveButton,
    negativeButton,
    neutralButton,
    cancelable,
    listener
)

fun Fragment.showAlert(
    msg: CharSequence?,
    @StringRes titleResId: Int = R.string.title_error_dialog,
    @StringRes positiveButtonResId: Int = R.string.ok,
    @StringRes negativeButtonResId: Int = 0,
    listener: ((Int) -> Unit)? = null
) = showAlert(
    msg,
    getString(titleResId),
    getString(positiveButtonResId),
    if (negativeButtonResId != 0) getString(negativeButtonResId) else null,
    listener = listener
)

fun Fragment.showAlert(
    @StringRes msgResId: Int = 0,
    @StringRes titleResId: Int = R.string.title_error_dialog,
    @StringRes positiveButtonResId: Int = R.string.ok,
    @StringRes negativeButtonResId: Int = 0,
    listener: ((Int) -> Unit)? = null
) {
    if (msgResId != 0) {
        showAlert(
            getString(msgResId),
            getString(titleResId),
            getString(positiveButtonResId),
            if (negativeButtonResId != 0) getString(negativeButtonResId) else null,
            listener = listener
        )
    }
}

fun Fragment.showException(throwable: Throwable) = showAlert(throwable.localizedMessage)


fun NavController.navigateTo(currentNavId: Int, navId: Int, bundle: Bundle? = null,
                             navOptions: NavOptions? = null) {
    if (currentDestination?.id == currentNavId) {
        navigate(navId, bundle, navOptions)
    } else {
        // This can happen only when user tapped the current item twice or again,
        // in that case current destination id will be mismatched with nav id
    }
}

fun NavController.navigateTo(direction: NavDirections, navExtras: Navigator.Extras? = null) {
    with(this) {
        currentDestination?.getAction(direction.actionId)?.let {
            navExtras?.let { extras ->
                navigate(direction, extras)
            } ?: navigate(direction)
        }
    }
}
