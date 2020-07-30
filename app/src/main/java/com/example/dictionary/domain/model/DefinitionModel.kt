package com.example.dictionary.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * Created by PraNeeTh on 4/7/20
 */
@Entity
data class DefinitionModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @SerializedName("defid")
    @ColumnInfo(index = true)
    val definitionId: Int = 0,

    @ColumnInfo
    val definition: String? = null,

    @ColumnInfo
    val word: String? = null,

    @SerializedName("thumbs_up")
    @ColumnInfo
    val thumbsUp: Int = 0,

    @SerializedName("thumbs_down")
    @ColumnInfo
    val thumbsDown: Int = 0
)