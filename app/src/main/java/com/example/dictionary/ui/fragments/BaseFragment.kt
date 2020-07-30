package com.example.dictionary.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment

/**
 * Created by PraNeeTh on 4/7/20
 */
abstract class BaseFragment: Fragment() {
    /**
     * The Id of the resource file to be inflated in the fragment.
     */
    protected abstract val layoutResourceId: Int

    /**
     * Screen title used for the fragment
     */
    protected abstract val screenTitle: String

    /**
     * Boolean whether to setHasOptionsMenu or not
     */
    protected abstract val shouldShowOptionsMenu: Boolean

    /**
     * Helper method that overrides onCreateView to automatically inflate the layout provided within layoutResourceId
     */
    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().title = screenTitle
        setHasOptionsMenu(shouldShowOptionsMenu)
        return inflater.inflate(layoutResourceId, container, false)
    }

    /**
     * Hide the soft keyboard
     */
    protected fun hideKeyboard() {
        requireActivity().currentFocus?.let {
            val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager? ?: return
            imm.hideSoftInputFromWindow(it.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }
}