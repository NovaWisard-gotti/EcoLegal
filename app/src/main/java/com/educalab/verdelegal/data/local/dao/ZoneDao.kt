package com.educalab.verdelegal.data.local.dao

import androidx.room.*
import com.educalab.verdelegal.data.local.entity.EnvironmentalZone
import kotlinx.coroutines.flow.Flow

@Dao
interface ZoneDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(zones: List<EnvironmentalZone>)

    @Query("SELECT * FROM environmental_zone ORDER BY mapOrder ASC")
    fun observeAll(): Flow<List<EnvironmentalZone>>

    @Query("SELECT * FROM environmental_zone ORDER BY mapOrder ASC")
    suspend fun getAll(): List<EnvironmentalZone>

    @Query("SELECT * FROM environmental_zone WHERE id = :id")
    suspend fun getById(id: Long): EnvironmentalZone?

    @Query("SELECT COUNT(*) FROM environmental_zone")
    suspend fun count(): Int
}
