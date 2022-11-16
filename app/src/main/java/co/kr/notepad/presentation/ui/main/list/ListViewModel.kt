package co.kr.notepad.presentation.ui.main.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.kr.notepad.domain.entity.Memo
import co.kr.notepad.domain.usecase.GetAllMemoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val getAllMemoUseCase: GetAllMemoUseCase
) : ViewModel() {
    private var _memos = MutableLiveData<List<Memo>>()
    val memos: LiveData<List<Memo>> get() = _memos

    fun getAll() {
        viewModelScope.launch {
            getAllMemoUseCase()
                .onSuccess { _memos.value = it }
                .onFailure { Timber.e(it) }
        }
    }
}