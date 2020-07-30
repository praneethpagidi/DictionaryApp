package com.example.dictionary.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dictionary.domain.database.repository.DefinitionDatabaseRepository
import com.example.dictionary.domain.model.DefinitionModel
import com.example.dictionary.domain.model.ServiceResponse
import com.example.dictionary.domain.service.ServiceResult
import com.example.dictionary.domain.service.repository.DefinitionServiceRepository
import com.example.dictionary.util.HEADER_HOST
import com.example.dictionary.util.HEADER_KEY
import com.example.dictionary.util.SingleLiveDataEvent
import kotlinx.coroutines.launch

/**
 * Created by PraNeeTh on 4/7/20
 */

class SearchViewModel(
    private val serviceRepository: DefinitionServiceRepository,
    private val dataBaseRepository: DefinitionDatabaseRepository
) : ViewModel() {
    private var mKeyWord: String = ""
    private var mDefinitions = MutableLiveData<List<DefinitionModel>>()
    val definitions: LiveData<List<DefinitionModel>> = mDefinitions

    private var mToastString = MutableLiveData<SingleLiveDataEvent<String>>()
    val toastString: LiveData<SingleLiveDataEvent<String>> = mToastString

    fun searchForWord(searchWord: String) {
        mKeyWord = searchWord
        viewModelScope.launch {
            val responseData = serviceRepository.getSearchResult(
                HEADER_HOST, HEADER_KEY, searchWord
            )

            val definitions = dataBaseRepository.getDefinitions(searchWord)

            when (responseData) {
                is ServiceResult.Success -> {
                    handleSuccessAndOperateDatabase(responseData.data, definitions)
                }

                is ServiceResult.Error -> {
                    handleErrorAndShowToast(definitions)
                }
            }
        }
    }

    suspend fun handleSuccessAndOperateDatabase(responseData: ServiceResponse,
                                                definitions: List<DefinitionModel>) {
        val definitionList = responseData.modelList

        mDefinitions.value = definitionList?.let { list ->
            list.mapNotNull { it }
        }

        if (!definitions.isNullOrEmpty() && !definitionList.isNullOrEmpty()) {
            dataBaseRepository.updateAll(definitionList.mapNotNull { it })
            return
        }

        definitionList?.let { list ->
            list.mapNotNull { it }
        }?.let {
            dataBaseRepository.insertDefinitions(it)
        }
    }

    fun handleErrorAndShowToast(definitions: List<DefinitionModel>) {
        if (definitions.isNullOrEmpty()) {
            mToastString.value = SingleLiveDataEvent("Please search for a valid word or check " +
                    "your Internet connection, Definition not found in Database")
            mDefinitions.value = null
            return
        }
        mDefinitions.value = definitions
        mToastString.value = SingleLiveDataEvent("Please check your Internet connection," +
                    " Definition found from Database")
    }
}