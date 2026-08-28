package com.educalab.verdelegal.data.local.dao

import androidx.room.*
import com.educalab.verdelegal.data.local.entity.Progress
import kotlinx.coroutines.flow.Flow

@Dao
interface ProgressDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(progress: Progress): Long

    @Update
    suspend fun update(progress: Progress)

    @Query("SELECT * FROM progress WHERE userId = :userId AND zoneId = :zoneId LIMIT 1")
    suspend fun getForUserAndZone(userId: Long, zoneId: Long): Progress?

    @Query("SELECT * FROM progress WHERE userId = :userId")
    fun observeForUser(userId: Long): Flow<List<Progress>>

    @Query("SELECT * FROM progress WHERE userId = :userId")
    suspend fun getForUser(userId: Long): List<Progress>

    @Query("SELECT COALESCE(SUM(xp), 0) FROM progress WHERE userId = :userId")
    suspend fun getTotalXp(userId: Long): Int
}
