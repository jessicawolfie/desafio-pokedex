package com.example.pokedex.data.remote.api

import com.example.pokedex.data.remote.dto.PokemonDetailDto
import com.example.pokedex.data.remote.dto.PokemonListResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// Interface Retrofit para os endpoints da PokéAPI

interface PokemonApi {
    /**
     * GET / pokemon?limit=&offset= — lista paginada
     * @param limit quantos itens retornar
     * @param offset a partir de qual índice começar
     *
     * Retorna a resposta com metadados de paginação + lista resumida
     * O detalhe de cada item vem em outra chamada
     */
    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("LIMIT") limit: Int,
        @Query("offset") offset: Int
    ): PokemonListResponseDto


    /**
     * GET /pokemon/{id} - detalhe por id numérico
     * @param id o id da Pokédex Nacional
     * @Path susbtitui {id} na URL: com id=25, vira /pokemon/25
     */
    @GET("pokemon/{id}")
    suspend fun getPokemonById(@Path("id") id: Int): PokemonDetailDto

    /**
     * GET /pokemon/{name} - detalhe/busca por nome
     * @param name o nome em minúsculo
     */
    @GET("pokemon/{name}")
    suspend fun getPokemonByName(@Path("name") name: String): PokemonDetailDto

}