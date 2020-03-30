package com.telstra.facts.arch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.telstra.facts.R
import com.telstra.facts.arch.viewmodel.BaseResultViewModel
import dagger.android.support.DaggerFragment
import javax.inject.Inject

/**
 * Base fragment class
 * Provides basic functionality inherent to all fragments in the app: lifecycle events logging, result propagation, etc.
 *
 * @param R Type of ResultViewModel class this fragment uses to notify about result of its work.
 */
abstract class BaseFragment<V : BaseResultViewModel<*>> : DaggerFragment() {

    protected abstract val layoutResId: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = if (layoutResId != 0) inflater.inflate(layoutResId, container, false) else null
        view?.let {
            val newParent = FrameLayout(inflater.context)
            newParent.id = R.id.base_fragment_content
            newParent.layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            newParent.addView(it)
            return newParent
        }

        return null
    }
}
