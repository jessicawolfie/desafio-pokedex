package com.example.pokedex.data.repository

import com.example.pokedex.data.remote.api.PokemonApi
import com.example.pokedex.data.remote.dto.OfficialArtworkDto
import com.example.pokedex.data.remote.dto.OtherSpritesDto
import com.example.pokedex.data.remote.dto.PokemonDetailDto
import com.example.pokedex.data.remote.dto.PokemonListItemDto
import com.example.pokedex.data.remote.dto.PokemonListResponseDto
import com.example.pokedex.data.remote.dto.SpritesDto
import com.example.pokedex.data.remote.dto.TypeDto
import com.example.pokedex.data.remote.dto.TypeSlotDto
import com.example.pokedex.domain.util.Result
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.io.IOException

// Testes do PokemonRepositoryImpl
class PokemonRepositoryImplTest {

    // Mock da Api: substituído a cada teste no @Before
    private lateinit var api: PokemonApi
    // Instâncoa real do Repopsitory sob teste, recebendo o mock por construtor
    private lateinit var repository: PokemonRepositoryImpl

    /**
     * @Before roda antes de cada @Test
     * Cria mock e repository "zerados"- cada teste começa em estado limpo, sem interferência do teste anterior
     */
    @Before
    fun setUp() {
        api = mock()
        repository = PokemonRepositoryImpl(api)
    }

    // Helpers (Object Mother) - construção de DTOs de teste
    private fun fakeDetailDto(id: Int = 1, name: String = "bulbassaur") = PokemonDetailDto(
        id = id,
        name = name,
        height = 7,
        weight = 69,
        types = listOf(TypeSlotDto(slot = 1, type = TypeDto("grass"))),
        sprites = SpritesDto(
            other = OtherSpritesDto(
                officialArtwork = OfficialArtworkDto(frontDefault = "https://img/$name.png")
            )
        )
    )

    private fun fakeListItem(name: String, id: Int) = PokemonListItemDto(
        name = name,
        url = "https://pokeapi.co/api/v2/pokemon/$id/"
    )

    // getPokemonById
    @Test
    fun `getPokemonById quando api responde com sucesso entao retorna Result Success com pokemon mapeado`() = runTest {
        // Arrange: programa o mock pra responder um DTO específico quando chamado
        whenever(api.getPokemonById(1)).thenReturn(fakeDetailDto(id = 1, name = "bulbassaur"))

        // Act
        val result = repository.getPokemonById(1)

        // Assert: verifica que é Success e que o pokemon foi mapeado corretamente
        assertThat(result).isInstanceOf(Result.Success::class.java)
        // Cast seguro: sabemos que é Success por causa do assert acima
        val pokemon = (result as Result.Success).data
        assertThat(pokemon.id).isEqualTo(1)
        assertThat(pokemon.name).isEqualTo("Bulbassaur")
        assertThat(pokemon.formattedId).isEqualTo("#001")
    }

    @Test
    fun `get pokemonById quando api lanca exception entao retorna Result Error prservando a exception`() = runTest {
        // Arrange: programa o mock pra lançar uma exception (simulando falha de rede)
        val networkFailure = IOException("network unreachable")
        org.mockito.kotlin.doAnswer { throw networkFailure }
            .whenever(api).getPokemonById(1)

        // Act
        val result = repository.getPokemonById(1)

        // Assert: virou Result.Error e a exception original está preservada
        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).exception).isEqualTo(networkFailure)
    }

    @Test
    fun `getPokemonById quando chamado entao invoca api com id correto`() = runTest {
        // Esse teste foca em comportamento(verify), não em retorno
        whenever(api.getPokemonById(25)).thenReturn(fakeDetailDto(id = 25, name = "pikachu"))

        repository.getPokemonById(25)

        // verify: confirma que api.getPokemonById(25) foi chamada exatamente uma vez
        verify(api).getPokemonById(25)
    }

    // getPokemonByName
    @Test
    fun `getPokemonByName quando api responde com sucesso entao retorna Result Success`()  = runTest {
        whenever(api.getPokemonByName("pikachu")).thenReturn(fakeDetailDto(id = 25, name = "pikachu"))

        val result = repository.getPokemonByName("pikachu")

        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data.name).isEqualTo("Pikachu")
    }

    @Test
    fun `getPokemonByName quando api lanca exception entao retorna Result Error`() = runTest {
        org.mockito.kotlin.doAnswer { throw IOException() }
            .whenever(api).getPokemonByName("naoexiste")

        val result = repository.getPokemonByName("naoexiste")

        assertThat(result).isInstanceOf(Result.Error::class.java)
    }

    // GetPokemonList
    @Test
    fun `getPokemonList quando api responde com sucesso entao retorna lista mapeada`() = runTest {
        // Arrange: mocka a lista resumida usando doAnswer
        val listResponse = PokemonListResponseDto(
            count = 2,
            next = null,
            previous = null,
            results = listOf(
                fakeListItem(name = "bulbasaur", id = 1),
                fakeListItem(name = "ivysaur", id = 2)
            )
        )
        org.mockito.kotlin.doAnswer { listResponse }
            .whenever(api).getPokemonList(20, 0)

        // E mocka o detalhe de cada item, também via doAnswer.
        val bulbaDetail = fakeDetailDto(id = 1, name = "bulbasaur")
        val ivyDetail = fakeDetailDto(id = 2, name = "ivysaur")
        org.mockito.kotlin.doAnswer { bulbaDetail }
            .whenever(api).getPokemonById(1)
        org.mockito.kotlin.doAnswer { ivyDetail }
            .whenever(api).getPokemonById(2)

        // Act
        val result = repository.getPokemonList(limit = 20, offset = 0)

        // Assert
        assertThat(result).isInstanceOf(Result.Success::class.java)
        val pokemons = (result as Result.Success).data
        assertThat(pokemons).hasSize(2)
        assertThat(pokemons[0].name).isEqualTo("Bulbasaur")
        assertThat(pokemons[1].name).isEqualTo("Ivysaur")
    }

    @Test
    fun `getPokemonList quando lista da api vem vazia entao retorna Success com lista vazia`() = runTest {
        val emptyResponse = PokemonListResponseDto(count = 0, next = null, previous = null, results = emptyList())
        whenever(api.getPokemonList(20, 0)).thenReturn(emptyResponse)

        val result = repository.getPokemonList(limit = 20, offset = 0)

        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data).isEmpty()
    }

    @Test
    fun `getPokemonList quando api lanca exception na lista entao retorna Result Error`() = runTest {
        org.mockito.kotlin.doAnswer { throw IOException("no connection") }
            .whenever(api).getPokemonList(20, 0)

        val result = repository.getPokemonList(limit = 20, offset = 0)

        assertThat(result).isInstanceOf(Result.Error::class.java)
    }

    @Test
    fun `getPokemonList quando api lanca exception no detalhe entao retorna Result Error`() = runTest {
        val listResponse = PokemonListResponseDto(
            count = 1,
            next = null,
            previous = null,
            results = listOf(fakeListItem(name = "bulbassaur", id = 1))
        )
        whenever(api.getPokemonList(20, 0)).thenReturn(listResponse)
        org.mockito.kotlin.doAnswer { throw IOException() }
            .whenever(api).getPokemonById(1)

        val result = repository.getPokemonList(limit = 20, offset = 0)

        assertThat(result).isInstanceOf(Result.Error::class.java)
    }

}