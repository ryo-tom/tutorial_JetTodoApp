package com.example.jettodoapp

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao // DAOであることをRoomに伝える
interface TaskDao {
    @Insert // 引数で受け取ったデータエンティティをDBに保存するためのメソッドであることをRoomに伝える
    suspend fun insertTask(task: Task)

    @Query("SELECT * FROM Task")
    fun loadAllTasks(): Flow<List<Task>>

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(Task: Task)
}