package co.kr.notepad.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.kr.notepad.domain.entity.Memo
import co.kr.notepad.domain.usecase.DeleteMemoUseCase
import co.kr.notepad.domain.usecase.GetAllMemoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val getAllMemoUseCase: GetAllMemoUseCase,
    private val deleteMemoUseCase: DeleteMemoUseCase
) : ViewModel() {
    private val selectedList = mutableListOf<Memo>()
    private val _memos = MutableStateFlow<UiState<List<Memo>>>(UiState.Init)
    val memos: StateFlow<UiState<List<Memo>>> get() = _memos
    private val _selectedMemos = MutableStateFlow<UiState<List<Memo>>>(UiState.Init)
    val selectedMemos: StateFlow<UiState<List<Memo>>> get() = _selectedMemos

    fun getAll() {
        viewModelScope.launch {
            _memos.emit(UiState.Loading)
            getAllMemoUseCase()
                .onSuccess {
                    _memos.emit(UiState.Success(it))
                }.onFailure {
                    Timber.e(it)
                    _memos.emit(UiState.Failure(it))
                }
        }
    }

    fun delete() {
        viewModelScope.launch {
            _selectedMemos.emit(UiState.Loading)
            deleteMemoUseCase(selectedList.toList())
                .onSuccess {
                    getAll()
                    selectedList.clear()
                    _selectedMemos.emit(UiState.Success(selectedList))
                }.onFailure {
                    Timber.e(it)
                    _selectedMemos.emit(UiState.Failure(it))
                }
        }
    }

    fun updateSelectedMemos(memo: Memo) {
        _selectedMemos.value = UiState.Loading
        selectedList.run {
            if (this.contains(memo)) {
                this.remove(memo)
            } else {
                this.add(memo)
            }
            _selectedMemos.value = UiState.Success(this)
        }
    }

    fun clearSelectedMemos() {
        _selectedMemos.value = UiState.Loading
        selectedList.run {
            this.clear()
            _selectedMemos.value = UiState.Success(this)
        }
    }
}