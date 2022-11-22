package co.kr.notepad.data.database

import androidx.room.*
import co.kr.notepad.data.model.MemoDto

@Dao
interface MemoDao {
    @Insert
    suspend fun insert(memo: MemoDto): Long

    @Query("SELECT * FROM memo")
    suspend fun getAll(): List<MemoDto>

    @Query("SELECT * FROM memo WHERE id = :memoId")
    suspend fun getMemo(memoId: Long): MemoDto

    @Update
    suspend fun update(memo: MemoDto): Int

    @Delete
    suspend fun delete(vararg memo: MemoDto): Int
}