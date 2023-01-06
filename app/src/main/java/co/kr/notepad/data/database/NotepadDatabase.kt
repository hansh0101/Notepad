package co.kr.notepad.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import co.kr.notepad.data.model.MemoDto

@Database(entities = [MemoDto::class], version = 2)
abstract class NotepadDatabase : RoomDatabase() {
    abstract fun memoDao(): MemoDao
}

val MIGRATION_1_2 = object: Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE memo ADD COLUMN title TEXT NOT NULL DEFAULT ''")
    }
}