package com.example.jetpack_compose.data.data_source

import androidx.room.*
import com.example.jetpack_compose.domain.model.Todo
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao { // Data Access Object

    @Query("SELECT * FROM todo ORDER BY date DESC")
    fun todos(): Flow<List<Todo>> // 비동기적인 데이터 흐름 처리하기 적합

    @Insert(onConflict = OnConflictStrategy.REPLACE) // 동일한 id를 가진 투두 객체를 insert하면 충돌 - 대체
    suspend fun insert(todo: Todo)

    @Update
    suspend fun update(todo: Todo)

    @Delete
    suspend fun delete(todo: Todo)
}