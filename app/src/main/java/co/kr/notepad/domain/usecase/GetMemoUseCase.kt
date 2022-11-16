package co.kr.notepad.domain.usecase

import co.kr.notepad.domain.entity.Memo
import co.kr.notepad.domain.repository.MemoRepository
import co.kr.notepad.presentation.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetMemoUseCase @Inject constructor(
    private val memoRepository: MemoRepository,
    @IoDispatcher private val coroutineDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(memoId: Long): Result<Memo> =
        runCatching {
            withContext(coroutineDispatcher) {
                memoRepository.getMemo(memoId)
            }
        }
}