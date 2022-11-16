package co.kr.notepad.domain.repository

import co.kr.notepad.domain.entity.Memo

interface MemoRepository {
    suspend fun insertOrUpdate(memo: Memo)
    suspend fun getAll(): List<Memo>
    suspend fun getMemo(memoId: Long): Memo
}