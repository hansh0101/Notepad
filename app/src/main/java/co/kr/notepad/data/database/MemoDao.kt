package co.kr.notepad.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import co.kr.notepad.data.model.MemoDto

@Dao
interface MemoDao {
    @Insert
    suspend fun insert(memo: MemoDto): Long

    @Query("SELECT * FROM memo")
    suspend fun getAll(): List<MemoDto>
}