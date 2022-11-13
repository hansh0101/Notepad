package co.kr.notepad.presentation.di

import co.kr.notepad.data.repository.MemoRepositoryImpl
import co.kr.notepad.domain.repository.MemoRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindMemoRepository(memoRepositoryImpl: MemoRepositoryImpl): MemoRepository
}