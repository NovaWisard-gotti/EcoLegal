package com.educalab.verdelegal.data.local.dao

import androidx.room.*
import com.educalab.verdelegal.data.local.entity.EnvironmentalScenario
import kotlinx.coroutines.flow.Flow

@Dao
interface ScenarioDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(scenarios: List<EnvironmentalScenario>)

    @Query("SELECT * FROM environmental_scenario WHERE zoneId = :zoneId ORDER BY sceneOrder ASC")
    fun observeForZone(zoneId: Long): Flow<List<EnvironmentalScenario>>

    @Query("SELECT * FROM environmental_scenario WHERE zoneId = :zoneId ORDER BY sceneOrder ASC")
    suspend fun getForZone(zoneId: Long): List<EnvironmentalScenario>

    @Query("SELECT * FROM environmental_scenario WHERE id = :id")
    suspend fun getById(id: Long): EnvironmentalScenario?
}
