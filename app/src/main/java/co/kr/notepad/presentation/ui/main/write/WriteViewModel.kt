package co.kr.notepad.presentation.ui.main.write

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.kr.notepad.domain.entity.Memo
import co.kr.notepad.domain.usecase.InsertMemoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WriteViewModel @Inject constructor(
    private val insertMemoUseCase: InsertMemoUseCase
) : ViewModel() {
    private var _isSaved = MutableLiveData<Boolean>()
    val isSaved: LiveData<Boolean> get() = _isSaved

    fun insert(text: String) {
        viewModelScope.launch {
            val memo = Memo(text = text, date = System.currentTimeMillis())
            insertMemoUseCase(memo)
                .onSuccess { _isSaved.value = true }
                .onFailure { _isSaved.value = false }
        }
    }
}