package com.example.dictionary.domain.database.repository

import com.example.dictionary.domain.database.DefinitionDao
import com.example.dictionary.domain.model.DefinitionModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Created by PraNeeTh on 4/7/20
 */
class DefinitionDatabaseRepositoryImpl(
    private val definitionDao: DefinitionDao,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO

) : DefinitionDatabaseRepository {

    override suspend fun insertDefinitions(definitionsList: List<DefinitionModel>) {
        withContext(coroutineDispatcher) {
            definitionDao.insertResponseValues(definitionsList)
        }
    }

    override suspend fun getDefinitions(searchWord: String): List<DefinitionModel> {
        return withContext(coroutineDispatcher) {
            definitionDao.getResponseValues(searchWord)
        }
    }

    override suspend fun updateAll(definitionsList: List<DefinitionModel>) {
        withContext(coroutineDispatcher) {
            definitionDao.updateResponseValues(definitionsList)
        }
    }
}