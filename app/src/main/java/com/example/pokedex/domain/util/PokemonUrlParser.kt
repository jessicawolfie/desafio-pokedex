package com.example.pokedex.domain.util

// EExtrai o id numérico da url de um pokémon retornada pela lista de PokéAPI
object PokemonUrlParser {
    /**
     * @param url url da PokéAPI no formato "https://pokeapi.co/api/v2/pokemon/{id}/"
     * @retun o id extraído, ou null se a url não estiver no formato esperado
     */
    fun extractIdFromUrl(url: String): Int? {
        return url.split("/").filter { it.isNotEmpty() }.lastOrNull()?.toIntOrNull()
    }
}