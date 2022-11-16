package co.kr.notepad.data.datasource

import co.kr.notepad.domain.entity.Memo

interface MemoDataSource {
    suspend fun insert(memo: Memo)
    suspend fun getAll(): List<Memo>
    suspend fun getMemo(memoId: Long): Memo
    suspend fun update(memo: Memo): Int
}