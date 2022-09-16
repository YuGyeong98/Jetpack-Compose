package com.example.jetpack_compose.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.jetpack_compose.domain.model.Todo

@Database(entities = [Todo::class], version = 1) // version - 숫자가 올라가거나 테이블의 구조가 변경됐을 때 업그레이드 처리
abstract class TodoDatabase: RoomDatabase() {
    abstract fun todoDao(): TodoDao
}