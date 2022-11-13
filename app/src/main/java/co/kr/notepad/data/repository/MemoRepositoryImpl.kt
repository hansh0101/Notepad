package co.kr.notepad.data.repository

import co.kr.notepad.data.datasource.MemoDataSource
import co.kr.notepad.domain.entity.Memo
import co.kr.notepad.domain.repository.MemoRepository
import javax.inject.Inject

class MemoRepositoryImpl @Inject constructor(
    private val memoDataSource: MemoDataSource
) : MemoRepository {
    override suspend fun insert(memo: Memo) {
        memoDataSource.insert(memo)
    }

    override suspend fun getAll(): List<Memo> {
        return memoDataSource.getAll()
    }
}