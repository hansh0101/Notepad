package co.kr.notepad.presentation.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.kr.notepad.domain.entity.Memo
import co.kr.notepad.domain.usecase.GetMemoUseCase
import co.kr.notepad.domain.usecase.InsertOrUpdateMemoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class WriteViewModel @Inject constructor(
    private val insertOrUpdateMemoUseCase: InsertOrUpdateMemoUseCase,
    private val getMemoUseCase: GetMemoUseCase
) : ViewModel() {
    private val _memo = MutableStateFlow<UiState<Memo>>(UiState.Init)
    val memo: StateFlow<UiState<Memo>> get() = _memo
    private val _isSaved = MutableStateFlow<UiState<Unit>>(UiState.Init)
    val isSaved: StateFlow<UiState<Unit>> get() = _isSaved
    val imageUri = MutableStateFlow<Uri?>(null)

    fun insertOrUpdate(memoId: Long, title: String, text: String) {
        val newMemo =
            Memo(
                id = memoId,
                title = title,
                text = text,
                image = imageUri.value.toString(),
                date = System.currentTimeMillis()
            )
        if (memo.value is UiState.Success) {
            if (title.isNotBlank() && !newMemo.isContentsTheSame((memo.value as UiState.Success).data)) {
                viewModelScope.launch {
                    _isSaved.emit(UiState.Loading)
                    insertOrUpdateMemoUseCase(newMemo)
                        .onSuccess {
                            _isSaved.emit(UiState.Success(Unit))
                        }.onFailure {
                            Timber.e(it)
                            _isSaved.emit(UiState.Failure(it))
                        }
                }
            }
        }
    }

    fun getMemo(memoId: Long) {
        if (memoId == 0L) {
            return
        }
        viewModelScope.launch {
            _memo.emit(UiState.Loading)
            getMemoUseCase(memoId)
                .onSuccess { memoFromDatabase ->
                    _memo.emit(UiState.Success(memoFromDatabase))
                    memoFromDatabase.image?.let { imageUri.emit(Uri.parse(it)) }
                }.onFailure {
                    Timber.e(it)
                    _memo.emit(UiState.Failure(it))
                }
        }
    }
}