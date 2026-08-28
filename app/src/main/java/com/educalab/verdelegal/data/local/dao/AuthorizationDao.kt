package com.educalab.verdelegal.data.local.dao

import androidx.room.*
import com.educalab.verdelegal.data.local.entity.AuthorizationActivity
import com.educalab.verdelegal.data.local.entity.EnvironmentalImpact
import com.educalab.verdelegal.data.local.entity.ProtectionMeasure
import com.educalab.verdelegal.data.local.entity.AuthorizationDecision

@Dao
interface AuthorizationDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertActivities(activities: List<AuthorizationActivity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertImpacts(impacts: List<EnvironmentalImpact>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMeasures(measures: List<ProtectionMeasure>)

    @Insert
    suspend fun insertDecision(decision: AuthorizationDecision): Long

    @Query("SELECT * FROM authorization_activity WHERE zoneId = :zoneId")
    suspend fun getForZone(zoneId: Long): List<AuthorizationActivity>

    @Query("SELECT * FROM authorization_activity WHERE id = :id")
    suspend fun getById(id: Long): AuthorizationActivity?

    @Query("SELECT * FROM environmental_impact WHERE authorizationActivityId = :activityId")
    suspend fun getImpacts(activityId: Long): List<EnvironmentalImpact>

    @Query("SELECT * FROM protection_measure WHERE authorizationActivityId = :activityId")
    suspend fun getMeasures(activityId: Long): List<ProtectionMeasure>

    @Query("SELECT * FROM authorization_decision WHERE userId = :userId")
    suspend fun getDecisionsForUser(userId: Long): List<AuthorizationDecision>

    @Query("SELECT COUNT(*) FROM authorization_activity")
    suspend fun countAll(): Int
}
