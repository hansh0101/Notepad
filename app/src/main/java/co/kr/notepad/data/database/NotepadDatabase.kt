package co.kr.notepad.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import co.kr.notepad.data.model.MemoDto

@Database(entities = [MemoDto::class], version = 1)
abstract class NotepadDatabase : RoomDatabase() {
    abstract fun memoDao(): MemoDao
}