package co.kr.notepad.data.repository

import co.kr.notepad.data.datasource.MemoDataSource
import co.kr.notepad.domain.entity.Memo
import co.kr.notepad.domain.repository.MemoRepository
import javax.inject.Inject

class MemoRepositoryImpl @Inject constructor(
    private val memoDataSource: MemoDataSource
) : MemoRepository {
    override suspend fun insertOrUpdate(memo: Memo) {
        if (memo.id == 0L) {
            memoDataSource.insert(memo)
        } else if (memo.id > 0L) {
            memoDataSource.update(memo)
        } else {
            throw IllegalArgumentException("Invalid memoId")
        }
    }

    override suspend fun getAll(): List<Memo> {
        return memoDataSource.getAll()
    }

    override suspend fun getMemo(memoId: Long): Memo {
        return memoDataSource.getMemo(memoId)
    }

    override suspend fun delete(memos: List<Memo>) {
        memoDataSource.delete(memos)
    }
}