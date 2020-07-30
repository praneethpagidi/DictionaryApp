package com.example.dictionary.domain.database

import androidx.room.*
import com.example.dictionary.domain.model.DefinitionModel

/**
 * Created by PraNeeTh on 4/7/20
 */
@Dao
interface DefinitionDao {
    @Query("SELECT * FROM DefinitionModel WHERE DefinitionModel.word LIKE :searchWord")
    fun getResponseValues(searchWord: String): List<DefinitionModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertResponseValues(models: List<DefinitionModel>): List<Long>

    @Update
    fun updateResponseValues(models: List<DefinitionModel>)

    @Delete
    fun deleteResponseValues(models: List<DefinitionModel>)
}