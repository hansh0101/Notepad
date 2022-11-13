package co.kr.notepad.domain.repository

import co.kr.notepad.domain.entity.Memo

interface MemoRepository {
    suspend fun insert(memo: Memo)
    suspend fun getAll(): List<Memo>
}