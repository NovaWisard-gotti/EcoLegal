package com.educalab.ecolegal.data.local.dao

import androidx.room.*
import com.educalab.ecolegal.data.local.entity.RestorationMission
import com.educalab.ecolegal.data.local.entity.RestorationStep

@Dao
interface RestorationDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMissions(missions: List<RestorationMission>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSteps(steps: List<RestorationStep>)

    @Query("SELECT * FROM restoration_mission WHERE zoneId = :zoneId")
    suspend fun getForZone(zoneId: Long): List<RestorationMission>

    @Query("SELECT * FROM restoration_mission WHERE id = :id")
    suspend fun getById(id: Long): RestorationMission?

    @Query("SELECT * FROM restoration_step WHERE missionId = :missionId ORDER BY stepOrder ASC")
    suspend fun getStepsForMission(missionId: Long): List<RestorationStep>

    @Query("SELECT COUNT(*) FROM restoration_mission")
    suspend fun countAll(): Int
}
