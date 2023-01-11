package co.kr.notepad.presentation.di

import android.content.Context
import androidx.room.Room
import co.kr.notepad.data.database.MIGRATION_1_2
import co.kr.notepad.data.database.MIGRATION_2_3
import co.kr.notepad.data.database.MemoDao
import co.kr.notepad.data.database.NotepadDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideNotepadDatabase(@ApplicationContext context: Context): NotepadDatabase =
        Room.databaseBuilder(context, NotepadDatabase::class.java, "notepad.db")
            .addMigrations(MIGRATION_1_2)
            .addMigrations(MIGRATION_2_3)
            .build()

    @Provides
    @Singleton
    fun provideMemoDao(database: NotepadDatabase): MemoDao = database.memoDao()
}