package com.educalab.ecolegal.data.local.dao

import androidx.room.*
import com.educalab.ecolegal.data.local.entity.UserProfile
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(profile: UserProfile): Long

    @Update
    suspend fun update(profile: UserProfile)

    @Query("SELECT * FROM user_profile LIMIT 1")
    fun observeCurrentProfile(): Flow<UserProfile?>

    @Query("SELECT * FROM user_profile LIMIT 1")
    suspend fun getCurrentProfile(): UserProfile?

    @Query("SELECT COUNT(*) FROM user_profile")
    suspend fun count(): Int
}
