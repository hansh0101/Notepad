package co.kr.notepad.presentation.ui.main.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.kr.notepad.domain.entity.Memo
import co.kr.notepad.domain.usecase.GetAllMemoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val getAllMemoUseCase: GetAllMemoUseCase
) : ViewModel() {
    init {
        getAll()
    }

    private var _memos = MutableLiveData<List<Memo>>()
    val memos: LiveData<List<Memo>> get() = _memos

    private fun getAll() {
        viewModelScope.launch {
            runCatching {
                withContext(Dispatchers.IO) {
                    getAllMemoUseCase()
                }
            }.onSuccess {
                _memos.value = it
            }.onFailure {
                Timber.e(it)
            }
        }
    }
}