package com.example.dictionary.domain.database.repository

import com.example.dictionary.domain.model.DefinitionModel

/**
 * Created by PraNeeTh on 4/7/20
 */
interface DefinitionDatabaseRepository {

    suspend fun insertDefinitions(definitionsList: List<DefinitionModel>)

    suspend fun getDefinitions(searchWord: String): List<DefinitionModel>

    suspend fun updateAll(definitionsList: List<DefinitionModel>)
}

