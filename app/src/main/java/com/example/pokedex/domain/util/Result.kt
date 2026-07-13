package com.example.pokedex.domain.util

// Wrapper que modelo o resultado de uma operação que pode falhar

sealed class Result<out T> {
    /**
     * Operação bem-sucedida. Carrega o valor produzido
     * @param data o valor real
     */
    data class Sucess<T>(val data: T) : Result<T>()

    /**
     * Operação falhou. Carrega a exception que causou a falha
     * @param exception a Throwable original
     */
    data class Error(val exception: Throwable) : Result<Nothing>()
}