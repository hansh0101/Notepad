package co.kr.notepad.domain.usecase

import co.kr.notepad.domain.entity.Memo
import co.kr.notepad.domain.repository.MemoRepository
import co.kr.notepad.presentation.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DeleteMemoUseCase @Inject constructor(
    private val memoRepository: MemoRepository,
    @IoDispatcher private val coroutineDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(memo: Memo) =
        runCatching {
            withContext(coroutineDispatcher) {
                memoRepository.delete(memo)
            }
        }
}