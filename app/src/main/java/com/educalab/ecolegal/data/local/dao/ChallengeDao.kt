package com.educalab.ecolegal.data.local.dao

import androidx.room.*
import com.educalab.ecolegal.data.local.entity.Challenge
import com.educalab.ecolegal.data.local.entity.ChallengeAttempt

@Dao
interface ChallengeDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(challenges: List<Challenge>)

    @Query("SELECT * FROM challenge WHERE scenarioId = :scenarioId ORDER BY challengeOrder ASC")
    suspend fun getForScenario(scenarioId: Long): List<Challenge>

    @Query("SELECT * FROM challenge WHERE id = :id")
    suspend fun getById(id: Long): Challenge?

    @Query("""
        SELECT c.* FROM challenge c
        INNER JOIN environmental_scenario s ON c.scenarioId = s.id
        WHERE s.zoneId = :zoneId
    """)
    suspend fun getForZone(zoneId: Long): List<Challenge>

    @Query("SELECT COUNT(*) FROM challenge")
    suspend fun countAll(): Int

    // ChallengeAttempt
    @Insert
    suspend fun insertAttempt(attempt: ChallengeAttempt): Long

    @Query("SELECT * FROM challenge_attempt WHERE challengeId = :challengeId AND userId = :userId ORDER BY timestamp DESC")
    suspend fun getAttempts(challengeId: Long, userId: Long): List<ChallengeAttempt>

    @Query("SELECT * FROM challenge_attempt WHERE userId = :userId AND success = 0 ORDER BY timestamp DESC LIMIT :limit")
    suspend fun getRecentFailedAttempts(userId: Long, limit: Int): List<ChallengeAttempt>

    @Query("""
        SELECT COUNT(DISTINCT challengeId) FROM challenge_attempt
        WHERE userId = :userId AND success = 1
    """)
    suspend fun countDistinctChallengesCompleted(userId: Long): Int
}
