package com.educalab.ecolegal.data.local.dao

import androidx.room.*
import com.educalab.ecolegal.data.local.entity.Badge
import com.educalab.ecolegal.data.local.entity.UserBadge
import kotlinx.coroutines.flow.Flow

@Dao
interface BadgeDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(badges: List<Badge>)

    @Query("SELECT * FROM badge")
    suspend fun getAll(): List<Badge>

    @Query("SELECT * FROM badge WHERE code = :code LIMIT 1")
    suspend fun getByCode(code: String): Badge?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun awardBadge(userBadge: UserBadge): Long

    @Query("SELECT * FROM user_badge WHERE userId = :userId")
    fun observeUserBadges(userId: Long): Flow<List<UserBadge>>

    @Query("SELECT * FROM user_badge WHERE userId = :userId")
    suspend fun getUserBadges(userId: Long): List<UserBadge>

    @Query("SELECT COUNT(*) FROM user_badge WHERE userId = :userId AND badgeId = :badgeId")
    suspend fun hasBadge(userId: Long, badgeId: Long): Int

    @Query("SELECT COUNT(*) FROM user_badge WHERE userId = :userId")
    suspend fun countUserBadges(userId: Long): Int
}
