package com.example.pokedex.data.remote.dto

import com.google.gson.annotations.SerializedName

data class PokemonDetailDto (
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("height") val height: Int,
    @SerializedName("weight") val weight: Int,
    @SerializedName("types") val types: List<TypeSlotDto>,
    @SerializedName("sprites") val sprites: SpritesDto
)