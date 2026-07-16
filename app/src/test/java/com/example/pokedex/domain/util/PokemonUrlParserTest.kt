package com.example.pokedex.domain.util

import com.google.common.truth.Truth.assertThat
import org.junit.Test

// Testes do PokemonUrlParser - função pura de parsing de URL
class PokemonUrlParserTest {

    @Test
    fun `extractIdFromUrl quando url e valida entao retorna o id`() {
        val url = "https://pokeapi.co/api/v2/pokemon/25/"

        val id = PokemonUrlParser.extractIdFromUrl(url)

        assertThat(id).isEqualTo(25)
    }

    @Test
    fun `extracIdFromUrl quando url tem id de tres digitos entao retorna o id`() {
        val id = PokemonUrlParser.extractIdFromUrl("https://pokeapi.co/api/v2/pokemon/150/")

        assertThat(id).isEqualTo(150)
    }

    @Test
    fun `extractIdFromUrl quando url nao termina em barra entao ainda extrai o id`() {
        val id = PokemonUrlParser.extractIdFromUrl("https://pokeapi.co/api/v2/pokemon/25")

        assertThat(id).isEqualTo(25)
    }

    @Test
    fun `extractIdFromUrl quando url e vazia entao retorna null`() {
        val id = PokemonUrlParser.extractIdFromUrl("")

        assertThat(id).isNull()
    }

    @Test
    fun `extractIdFromUrl quando url nao contem numero entao retorna null`() {
        val id = PokemonUrlParser.extractIdFromUrl("https://pokeapi.co/api/v2/pokemon/abc/")

        assertThat(id).isNull()
    }
}