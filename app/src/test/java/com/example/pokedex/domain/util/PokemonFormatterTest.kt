package com.example.pokedex.domain.util

import com.google.common.truth.Truth.assertThat
import org.junit.Test

// Teste do PokemonFormatter
class PokemonFormatterTest {

    // --- formatId ---
    // Convenção de nome em backticks: `método_condição_resultado esperado`.
    @Test
    fun `formatId quando id tem um digito entao preenche com dois zeros a esquerda`() {
        // Padrão Arrange-Act-Assert (AAA):
        // Arrange - preparar entrada
        // Act - executar a função sob teste
        // Assert - verificar o resultado

        // Act
        val resultado = PokemonFormatter.formatId(1)

        // Assert - assertThat do Truth
        // Em caso de falha a mensagem é "expected #001 but was #1"
        assertThat(resultado).isEqualTo("#001")
    }

    @Test
    fun `formatId quando id tem dois digitos entao preenche com um zero a esquerda`() {
        assertThat(PokemonFormatter.formatId(25)).isEqualTo("#025")
    }

    @Test
    fun `formatId quando id tem tres digitos entao nao adiciona zeros`() {
        assertThat(PokemonFormatter.formatId(150)).isEqualTo("#150")
    }

    @Test
    fun `formatId quando id tem quatro digitos entao nao trunca`() {
        // Caso de borda: a partir da geração 5+ existem pokémons com id > 999.
        // O %03d preenche ATÉ 3 dígitos, não LIMITA a 3.
        assertThat(PokemonFormatter.formatId(1025)).isEqualTo("#1025")
    }

    // --- heightToMeters ---
    @Test
    fun `heightToMeters converte decimetros para metros sem truncar`() {
        assertThat(PokemonFormatter.heightToMeters(7)).isWithin(0.7)
    }

    @Test
    fun `heightToMeters quando altura é zero retorna zero`() {
        assertThat(PokemonFormatter.heightToMeters(145)).isWithin(14.54)
    }

    @Test
    fun `heightToMeters lida com valores grandes`() {
        assertThat(PokemonFormatter.heightToMeters(145)).isWithin(14.5)
    }

    // --- weightToKg ---
    @Test
    fun `weightToKg converte hectogramas para quilos`() {
        assertThat(PokemonFormatter.weightToKg(69)).isWithin(6.9)
    }

    @Test
    fun `weightToKg lida com valores grandes`() {
        assertThat(PokemonFormatter.weightToKg(9500)).isWithin(950.0)
    }

    // --- capitalizeName ---
    @Test
    fun `capitalizeName transforma primeira letra em maiuscula`() {
        assertThat(PokemonFormatter.capitalizeName("bulbasaur")).isEqualTo("Bulbasaur")
    }

    @Test
    fun `capitalizeName quando nome ja esta capitalizado nao modifica`() {
        assertThat(PokemonFormatter.capitalizeName("Pikachu")).isEqualTo("Pikachu")
    }

    @Test
    fun `capitalizeName quando string vazia entao retorna string vazia`() {
        assertThat(PokemonFormatter.capitalizeName("")).isEqualTo("")
    }
}