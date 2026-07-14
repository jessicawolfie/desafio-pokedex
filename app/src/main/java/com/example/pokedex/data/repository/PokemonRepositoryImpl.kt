package com.example.pokedex.data.repository

import com.example.pokedex.data.mapper.toDomain
import com.example.pokedex.data.remote.api.PokemonApi
import com.example.pokedex.domain.model.Pokemon
import com.example.pokedex.domain.repository.PokemonRepository
import com.example.pokedex.domain.util.PokemonUrlParser
import com.example.pokedex.domain.util.Result
import javax.inject.Inject

// Implementação completa do PokemonRepository

class PokemonRepositoryImpl @Inject constructor(
    private val api: PokemonApi
) : PokemonRepository {

    /**
     * Lista paginada.
     *
     * Fluxo:
     *   1. Chama a Api pra buscar a lista resumida (só name + url).
     *   2. Pra cada item da lista, extrai o id da url e busca o detalhe completo.
     *   3. Mapeia cada DTO detalhado pro Model do domain.
     *   4. Embrulha o resultado num Success.
     */
    override suspend fun getPokemonList(limit: Int, offset: Int): Result<List<Pokemon>> {
        return try {
            // 1. Busca a lista resumida
            val listResponse = api.getPokemonList(limit, offset)

            // 2. Pra cada item, extrai id e busca detalhe
            val pokemons = listResponse.results.mapNotNull { itemDto ->
                 // Extrai id da url; se url malformada, pula esse item
                val id = PokemonUrlParser.extractIdFromUrl(itemDto.url) ?: return@mapNotNull null
                // Busca o detalhe e converte pra Model
                api.getPokemonById(id).toDomain()
            }

            // 3. Sucesso
            Result.Success(pokemons)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    // Detalhe por id
    override suspend fun getPokemonById(id: Int): Result<Pokemon> {
        return try {
            val pokemon = api.getPokemonById(id).toDomain()
            Result.Success(pokemon)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    // Detalhe por nome
    override suspend fun getPokemonByName(name: String): Result<Pokemon> {
        return try {
            val pokemon = api.getPokemonByName(name).toDomain()
            Result.Success(pokemon)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}