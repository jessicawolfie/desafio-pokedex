package com.example.pokedex.domain.repository

import android.R
import com.example.pokedex.domain.model.Pokemon
import com.example.pokedex.domain.util.Result

// Contrato de repositório de Pokémons

interface PokemonRepository {
    /**
     * Busca uma página da lista de Pokémons
     * @param limit quantos itens
     * @param offset a partir de qual índice
     * @return Success com a lista de pokémon ou Error com a exception que causou a falha
     */
    suspend fun getPokemonList(limit: Int, offset: Int): Result<List<Pokemon>>

    // Busca o detalhe de um pokemon por id
    suspend fun getPokemonById(id: Int): Result<Pokemon>

    // Busca o detalhe de um pokemon por nome
    suspend fun getPokemonByName(name: String): Result<Pokemon>
}