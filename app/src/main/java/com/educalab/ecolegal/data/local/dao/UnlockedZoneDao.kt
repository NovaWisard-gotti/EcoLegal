package com.educalab.ecolegal.data.local.dao

import androidx.room.*
import com.educalab.ecolegal.data.local.entity.UnlockedZone
import kotlinx.coroutines.flow.Flow

@Dao
interface UnlockedZoneDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun unlock(entry: UnlockedZone): Long

    @Query("SELECT * FROM unlocked_zone WHERE userId = :userId")
    fun observeForUser(userId: Long): Flow<List<UnlockedZone>>

    @Query("SELECT * FROM unlocked_zone WHERE userId = :userId")
    suspend fun getForUser(userId: Long): List<UnlockedZone>

    @Query("SELECT COUNT(*) FROM unlocked_zone WHERE userId = :userId AND zoneId = :zoneId")
    suspend fun isUnlocked(userId: Long, zoneId: Long): Int
}
