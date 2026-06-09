package com.example.pokedex.domain.util

object PokemonFormatter {
    fun formatId(id: Int): String = "#%03d".format(id)

    fun heightToMeters(heightInDecimeters: Int): Double = heightInDecimeters / 10.0

    fun weightToKg(weightInHectograms: Int): Double = weightInHectograms / 10.0

    fun capitalizeName(name: String): String =
        name.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
}