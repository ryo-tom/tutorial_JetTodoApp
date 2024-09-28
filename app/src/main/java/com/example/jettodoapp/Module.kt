package com.example.jettodoapp

import android.content.Context
import androidx.room.Room
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@dagger.Module
@InstallIn(SingletonComponent::class) // TaskDaoはアプリを通して一つのインスタンスで大丈夫
object Module {
    @Provides // インスタンスの生成方法をHiltに提示する
    fun provideDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, AppDataBase::class.java, name = "task_database").build()

    // TaskDaoのインスタンスを作る方法を伝える
    @Provides
    fun provideDao(db: AppDataBase) = db.taskDao()
}