package com.example.dictionary.domain.model

import com.google.gson.annotations.SerializedName

/**
 * Created by PraNeeTh on 4/7/20
 */
data class ServiceResponse (
    @SerializedName("list")
    val modelList: List<DefinitionModel?>?
)
