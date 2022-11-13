package co.kr.notepad.domain.usecase

import co.kr.notepad.domain.entity.Memo
import co.kr.notepad.domain.repository.MemoRepository
import javax.inject.Inject

class InsertMemoUseCase @Inject constructor(
    private val memoRepository: MemoRepository
) {
    suspend operator fun invoke(memo: Memo) {
        memoRepository.insert(memo)
    }
}