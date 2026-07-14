package com.example.pokedex.di

import com.example.pokedex.data.repository.PokemonRepositoryImpl
import com.example.pokedex.domain.repository.PokemonRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// Módulo Hilt: amarra a interface PokemonRepository à implementação PokemonRepositoryImpl
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindPokeminRepository(
        impl: PokemonRepositoryImpl
    ): PokemonRepository
}