package co.kr.notepad.presentation.di

import co.kr.notepad.data.datasource.MemoDataSource
import co.kr.notepad.data.datasource.MemoDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {
    @Binds
    abstract fun bindMemoDataSource(memoDataSourceImpl: MemoDataSourceImpl): MemoDataSource
}