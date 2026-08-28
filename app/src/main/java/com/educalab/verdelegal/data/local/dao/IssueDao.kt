package com.educalab.verdelegal.data.local.dao

import androidx.room.*
import com.educalab.verdelegal.data.local.entity.EnvironmentalIssue

@Dao
interface IssueDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(issues: List<EnvironmentalIssue>)

    @Query("SELECT * FROM environmental_issue WHERE scenarioId = :scenarioId")
    suspend fun getForScenario(scenarioId: Long): List<EnvironmentalIssue>
}
