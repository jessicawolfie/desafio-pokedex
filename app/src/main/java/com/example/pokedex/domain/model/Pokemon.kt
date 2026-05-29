package com.example.pokedex.domain.model

// Representação do Pokémon no domínio da aplicação
data class Pokemon(
    val id: Int,
    val name: String,
    val formattedId: String,
    val heightInMeters: Double,
    val weightInKg: Double,
    val types: List<String>,
    val imageUrl: String
)