package co.kr.notepad.presentation.viewmodel

import android.net.Uri
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
    private val _isErrorOccurred = MutableLiveData<Boolean>()
    val isErrorOccurred: LiveData<Boolean> get() = _isErrorOccurred
    val imageUri = MutableLiveData<Uri?>(null)

    fun insertOrUpdate(memoId: Long, title: String, text: String) {
        val newMemo =
            Memo(id = memoId, title = title, text = text, date = System.currentTimeMillis())
        if (title.isNotBlank() && !newMemo.isContentsTheSame(memo.value)) {
            viewModelScope.launch {
                insertOrUpdateMemoUseCase(newMemo)
                    .onFailure { _isErrorOccurred.value = true }
            }
        }
    }

    fun getMemo(memoId: Long) {
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