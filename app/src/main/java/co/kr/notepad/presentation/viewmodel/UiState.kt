package co.kr.notepad.presentation.viewmodel

sealed class UiState<out T> {
    object Init : UiState<Nothing>()
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Failure(val exception: Throwable) : UiState<Nothing>()
}
