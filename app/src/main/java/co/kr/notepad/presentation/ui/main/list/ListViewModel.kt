package co.kr.notepad.presentation.ui.main.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.kr.notepad.domain.entity.Memo
import co.kr.notepad.domain.usecase.DeleteMemoUseCase
import co.kr.notepad.domain.usecase.GetAllMemoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val getAllMemoUseCase: GetAllMemoUseCase,
    private val deleteMemoUseCase: DeleteMemoUseCase
) : ViewModel() {
    private val _memos = MutableLiveData<List<Memo>>()
    val memos: LiveData<List<Memo>> get() = _memos
    private val selectedList = mutableListOf<Memo>()
    private val _selectedMemos = MutableLiveData<List<Memo>>(selectedList)
    val selectedMemos: LiveData<List<Memo>> get() = _selectedMemos

    fun getAll() {
        viewModelScope.launch {
            getAllMemoUseCase()
                .onSuccess { _memos.value = it }
                .onFailure { Timber.e(it) }
        }
    }

    fun delete() {
        viewModelScope.launch {
            deleteMemoUseCase(selectedList.toList())
                .onSuccess {
                    getAll()
                    selectedList.clear()
                    _selectedMemos.value = selectedList
                }.onFailure { Timber.e(it) }
        }
    }

    fun updateSelectedMemos(memo: Memo) {
        if (selectedList.contains(memo)) {
            selectedList.remove(memo)
        } else {
            selectedList.add(memo)
        }
        _selectedMemos.value = selectedList
    }
}