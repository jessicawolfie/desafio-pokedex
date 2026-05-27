package com.example.pokedex.data.remote.dto

import com.google.gson.annotations.SerializedName
data class TypeSlotDto(
    @SerializedName("slot") val slot: Int,
    @SerializedName("type") val type: TypeDto
)

data class TypeDto(
    @SerializedName("name") val name: String
)