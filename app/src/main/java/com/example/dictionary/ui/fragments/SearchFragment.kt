package com.example.dictionary.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dictionary.R
import com.example.dictionary.domain.model.DefinitionModel
import com.example.dictionary.ui.adapters.DefinitionAdapter
import com.example.dictionary.ui.viewmodels.SearchViewModel
import com.example.dictionary.util.SingleLiveDataEvent
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.layout_search.*
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.find
import org.jetbrains.anko.support.v4.toast
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Created by PraNeeTh on 4/7/20
 */
class SearchFragment : BaseFragment(), View.OnClickListener {

    private var keyWord = ""
    private lateinit var definitionList: List<DefinitionModel>

    private val viewModel by viewModel<SearchViewModel>()

    private val responseObserver = Observer<List<DefinitionModel>> { response ->
        serviceProgress.visibility = ProgressBar.INVISIBLE
        if (response.isNullOrEmpty()) {
            showDialogAndHideRecyclerView(response)
        } else {
            definitionList = response
            initRecyclerViewAndPopulateList()
        }
    }

    private val dialogTextObserver = Observer<SingleLiveDataEvent<String>> {
        it.getContentIfNotHandled()?.let { dialogText ->
            alert(dialogText) {
                positiveButton(requireContext().getString(R.string.positive_dialog_button_text))
                { dialog -> dialog.dismiss() }
            }.show()
        }
    }

    override val layoutResourceId: Int = R.layout.layout_search

    override val screenTitle: String = "Definitions"

    override val shouldShowOptionsMenu: Boolean = true



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainLayout.requestFocus()
        initSearchAndButton()

        viewModel.definitions.observe(viewLifecycleOwner, responseObserver)
        viewModel.toastString.observe(viewLifecycleOwner, dialogTextObserver)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_item, menu)
        menu.findItem(R.id.menu_sort).setIcon(R.drawable.ic_sort)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val adapter = list_results.adapter as DefinitionAdapter
        if (item.itemId == R.id.menuSortLike) {
            adapter.setData(definitionList?.sortedByDescending { it.thumbsUp })
        }
        if (item.itemId == R.id.menuSortDislike) {
            adapter.setData(definitionList?.sortedByDescending { it.thumbsDown })
        }
        return super.onOptionsItemSelected(item)

    }

    private fun initSearchAndButton() {
        find<TextInputEditText>(R.id.et_searchWord).run {
            hint = context.getString(R.string.search_a_word)
            addTextChangedListener(getTextWatcher())
        }

        find<MaterialButton>(R.id.btn_search).run {
            backgroundTintList = ContextCompat.getColorStateList(
                context,
                R.color.customButtonColor
            )
            setOnClickListener(this@SearchFragment)
        }
    }

    private fun initRecyclerViewAndPopulateList() {
        list_results?.run {
            visibility = View.VISIBLE
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            val definitionAdapter = DefinitionAdapter()
            definitionAdapter.setData(definitionList)
            adapter = definitionAdapter
        }
    }

    private fun showDialogAndHideRecyclerView(list: List<DefinitionModel>?) {
        list_results?.visibility = View.INVISIBLE

        if (list == null)
            return

        if (list.isEmpty()) {
            alert(requireContext().getString(R.string.search_for_valid_word)) {
                positiveButton(requireContext().getString(R.string.positive_dialog_button_text))
                { dialog -> dialog.dismiss() }
            }.show()
        }
    }

    private fun getTextWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //no op
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //no op
            }

            override fun afterTextChanged(s: Editable?) {
                keyWord = if (!s.isNullOrEmpty() && !et_searchWord.text.isNullOrEmpty()) {
                    s.toString().trim()
                } else {
                    ""
                }
            }
        }
    }

    override fun onClick(view: View?) {
        if (view?.id == R.id.btn_search) {
            performClick()
        }
    }

    private fun performClick() {
        hideKeyboard()
        if (keyWord.isBlank()) {
            toast("Please enter a word")
            return
        }
        serviceProgress.visibility = ProgressBar.VISIBLE
        viewModel.searchForWord(keyWord)
    }
}
