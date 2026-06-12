package com.example.pokedex.data.mapper

import com.example.pokedex.data.remote.dto.PokemonDetailDto
import com.example.pokedex.domain.model.Pokemon
import com.example.pokedex.domain.util.PokemonFormatter

private const val FALLBACK_IMAGE_URL =
    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/0.png"

fun PokemonDetailDto.toDomain(): Pokemon = Pokemon(
    id = id,

    name = PokemonFormatter.capitalizeName(name),

    formattedId = PokemonFormatter.formatId(id),

    heightInMeters = PokemonFormatter.heightToMeters(height),

    weightInKg = PokemonFormatter.weightToKg(weight),

    types = types.map { it.type.name },

    imageUrl = sprites.other?.officialArtwork?.frontDefault ?: FALLBACK_IMAGE_URL
)