package co.kr.notepad.data.datasource

import co.kr.notepad.data.database.MemoDao
import co.kr.notepad.data.model.MemoDto
import co.kr.notepad.domain.entity.Memo
import javax.inject.Inject

class MemoDataSourceImpl @Inject constructor(
    private val memoDao: MemoDao
) : MemoDataSource {
    override suspend fun insert(memo: Memo) {
        memoDao.insert(
            MemoDto(
                memo.id,
                memo.text,
                memo.date
            )
        )
    }

    override suspend fun getAll(): List<Memo> {
        return memoDao.getAll().map { it.toMemo() }
    }
}