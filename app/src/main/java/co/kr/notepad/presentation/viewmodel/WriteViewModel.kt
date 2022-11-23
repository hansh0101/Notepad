package co.kr.notepad.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.kr.notepad.domain.entity.Memo
import co.kr.notepad.domain.usecase.GetMemoUseCase
import co.kr.notepad.domain.usecase.InsertOrUpdateMemoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class WriteViewModel @Inject constructor(
    private val insertOrUpdateMemoUseCase: InsertOrUpdateMemoUseCase,
    private val getMemoUseCase: GetMemoUseCase
) : ViewModel() {
    private val _memo = MutableLiveData<Memo>()
    val memo: LiveData<Memo> get() = _memo
    private val _isSaved = MutableLiveData<Boolean>()
    val isSaved: LiveData<Boolean> get() = _isSaved

    fun insertOrUpdate(memoId: Long, text: String) {
        viewModelScope.launch {
            val memo = Memo(id = memoId, text = text, date = System.currentTimeMillis())
            insertOrUpdateMemoUseCase(memo)
                .onSuccess { _isSaved.value = true }
                .onFailure { _isSaved.value = false }
        }
    }

    fun getMemo(memoId: Long) {
        Timber.tag("memoId").i(memoId.toString())
        if (memoId == 0L) {
            return
        }
        viewModelScope.launch {
            getMemoUseCase(memoId)
                .onSuccess { _memo.value = it }
                .onFailure { Timber.e(it) }
        }
    }
}