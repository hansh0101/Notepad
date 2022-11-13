package co.kr.notepad.presentation.di

import android.content.Context
import androidx.room.Room
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
            .build()

    @Provides
    @Singleton
    fun provideMemoDao(database: NotepadDatabase): MemoDao = database.memoDao()
}