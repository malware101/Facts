package com.telstra.facts.arch.ui

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ErrorDialogFragment : DialogFragment() {
    companion object {
        private val TAG = ErrorDialogFragment::class.java.simpleName
        private const val ARG_MESSAGE = "ARG_MESSAGE"
        private const val ARG_TITLE = "ARG_TITLE"
        private const val ARG_POSITIVE_BUTTON = "ARG_POSITIVE_BUTTON"
        private const val ARG_NEGATIVE_BUTTON = "ARG_NEGATIVE_BUTTON"
        private const val ARG_NEUTRAL_BUTTON = "ARG_NEUTRAL_BUTTON"
        private const val ARG_CANCELABLE = "ARG_CANCELABLE"

        fun show(
            title: CharSequence,
            message: CharSequence,
            positiveButton: CharSequence,
            negativeButton: CharSequence?,
            neutralButton: CharSequence?,
            cancellable: Boolean,
            fragmentManager: FragmentManager,
            listener: ((Int) -> Unit)?
        ) = newInstance(
            title,
            message,
            positiveButton,
            negativeButton,
            neutralButton,
            cancellable,
            listener
        ).apply {
            isCancelable = cancellable
            show(fragmentManager,
                TAG
            )
        }

        private fun newInstance(
            title: CharSequence,
            message: CharSequence,
            positiveButton: CharSequence,
            negativeButton: CharSequence?,
            neutralButton: CharSequence?,
            cancellable: Boolean,
            listener: ((Int) -> Unit)?
        ): ErrorDialogFragment = ErrorDialogFragment()
            .apply {
            setButtonListener(listener)
//            arguments = bundleOf(
//                ARG_TITLE to title,
//                ARG_MESSAGE to message,
//                ARG_POSITIVE_BUTTON to positiveButton,
//                ARG_NEGATIVE_BUTTON to negativeButton,
//                ARG_NEUTRAL_BUTTON to neutralButton,
//                ARG_CANCELABLE to cancellable
//            )
        }
    }

    private var listener: ((Int) -> Unit)? = null
    private val title by lazy { arguments?.getCharSequence(ARG_TITLE, null) ?: "" }
    private val message by lazy { arguments?.getCharSequence(ARG_MESSAGE, null) ?: "" }
    private val positiveButton by lazy { arguments?.getCharSequence(ARG_POSITIVE_BUTTON, null) ?: "" }
    private val negativeButton by lazy { arguments?.getCharSequence(ARG_NEGATIVE_BUTTON, null) ?: "" }
    private val neutralButton by lazy { arguments?.getCharSequence(ARG_NEUTRAL_BUTTON, null) ?: "" }
    private val cancelable by lazy { arguments?.getBoolean(ARG_CANCELABLE, true) ?: true }

    fun setButtonListener(listener: ((Int) -> Unit)?) {
        this.listener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveButton) { dialog, index ->
                listener?.invoke(index)
                dialog.dismiss()
            }
            .apply {
                setCancelable(cancelable)
                if (negativeButton.isNotEmpty()) setNegativeButton(negativeButton) { dialog, index ->
                    listener?.invoke(index)
                    dialog.dismiss()
                }
                if (neutralButton.isNotEmpty()) setNeutralButton(neutralButton) { _, index ->
                    listener?.invoke(index)
                }
            }
            .setOnDismissListener { dialog ->
                listener?.invoke(if (negativeButton.isNotEmpty()) {
                    DialogInterface.BUTTON_NEGATIVE
                } else {
                    DialogInterface.BUTTON_POSITIVE
                })
                dialog.dismiss()
            }
            .create()
}
