package com.example.pokedex.data.mapper

import com.example.pokedex.data.remote.dto.OfficialArtworkDto
import com.example.pokedex.data.remote.dto.OtherSpritesDto
import com.example.pokedex.data.remote.dto.PokemonDetailDto
import com.example.pokedex.data.remote.dto.SpritesDto
import com.example.pokedex.data.remote.dto.TypeDto
import com.example.pokedex.data.remote.dto.TypeSlotDto
import com.google.common.truth.Truth.assertThat
import org.junit.Test

// Testes do Mapper DTO -> Domain.
class PokemonMapperTest {

    /**
     * Object Mother: cria um DTO "típico" (Bulbasaur) com valores default.
     * Qualquer parâmetro pode ser sobrescrito por nome no chamador.
     */
    private fun fakeDto(
        id: Int = 1,
        name: String = "bulbasaur",
        height: Int = 7,                                       // -> 0.7m
        weight: Int = 69,                                      // -> 6.9kg
        typeNames: List<String> = listOf("grass", "poison"),
        artworkUrl: String? = "https://img/bulbasaur.png"
    ) = PokemonDetailDto(
        id = id,
        name = name,
        height = height,
        weight = weight,
        types = typeNames.mapIndexed { index, typeName ->
            TypeSlotDto(slot = index + 1, type = TypeDto(typeName))
        },
        sprites = SpritesDto(
            other = OtherSpritesDto(
                officialArtwork = OfficialArtworkDto(frontDefault = artworkUrl)
            )
        )
    )

    // --- Caso feliz (happy path) ---

    @Test
    fun `toDomain mapeia todos os campos corretamente no caso feliz`() {
        // Arrange
        val dto = fakeDto()

        // Act
        val model = dto.toDomain()

        // Assert — verifico CAMPO POR CAMPO. Não junto tudo em um isEqualTo(esperado)
        // porque, quando algo falhar, quero saber EXATAMENTE qual campo deu errado.
        assertThat(model.id).isEqualTo(1)
        assertThat(model.name).isEqualTo("Bulbasaur")           // capitalizado
        assertThat(model.formattedId).isEqualTo("#001")         // formatado
        assertThat(model.heightInMeters).isWithin(0.7)         // convertido
        assertThat(model.weightInKg).isWithin(6.9)             // convertido
        assertThat(model.types).containsExactly("grass", "poison").inOrder()  // achatado + ordem
        assertThat(model.imageUrl).isEqualTo("https://img/bulbasaur.png")
    }

    // --- Casos de borda ---

    @Test
    fun `toDomain quando artwork e null entao usa imagem de fallback`() {
        val dto = fakeDto(artworkUrl = null)

        val model = dto.toDomain()

        // A URL de fallback é interna ao mapper, mas o teste verifica que
        // o resultado NÃO É null e tem a cara de URL de placeholder.
        // Assert pela ESTRUTURA, não pela URL exata, se a URL mudar amanhã,
        // o teste não quebra
        assertThat(model.imageUrl).isNotEmpty()
        assertThat(model.imageUrl).contains("PokeAPI")
    }

    @Test
    fun `toDomain quando other sprite e null entao usa imagem de fallback`() {
        val dto = fakeDto().copy(
            sprites = SpritesDto(other = null)
        )

        val model = dto.toDomain()

        assertThat(model.imageUrl).contains("PokeAPI")
    }

    @Test
    fun `toDomain quando lista de tipos vazia entao retorna lista vazia`() {
        val dto = fakeDto(typeNames = emptyList())

        val model = dto.toDomain()

        assertThat(model.types).isEmpty()
    }

    @Test
    fun `toDomain quando pokemon tem um unico tipo entao retorna lista com um elemento`() {
        val dto = fakeDto(typeNames = listOf("bug"))

        val model = dto.toDomain()

        assertThat(model.types).containsExactly("bug")
    }

    @Test
    fun `toDomain preserva a ordem dos tipos`() {
        val dto = fakeDto(typeNames = listOf("dragon", "flying"))

        val model = dto.toDomain()

        assertThat(model.types).containsExactly("dragon", "flying").inOrder()
    }

    @Test
    fun `toDomain capitaliza nome mesmo quando ja vem capitalizado da API`() {
        val dto = fakeDto(name = "Mewtwo")

        val model = dto.toDomain()

        assertThat(model.name).isEqualTo("Mewtwo")
    }

    @Test
    fun `toDomain quando id tem tres digitos entao formattedId nao tem zeros extras`() {
        val dto = fakeDto(id = 150)

        val model = dto.toDomain()

        assertThat(model.formattedId).isEqualTo("#150")
    }
}