package com.example.dictionary.domain.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.dictionary.domain.model.DefinitionModel

/**
 * Created by PraNeeTh on 4/7/20
 */
@Database(entities = [DefinitionModel::class], exportSchema = false, version = 1)
abstract class DefinitionDatabase: RoomDatabase() {
    abstract val definitionDao: DefinitionDao
}