package com.educalab.verdelegal.data.local.dao

import androidx.room.*
import com.educalab.verdelegal.data.local.entity.Decision
import com.educalab.verdelegal.data.local.entity.DecisionOutcome
import com.educalab.verdelegal.data.local.entity.Consequence

@Dao
interface DecisionDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDecisions(decisions: List<Decision>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOutcomes(outcomes: List<DecisionOutcome>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertConsequences(consequences: List<Consequence>)

    @Query("SELECT * FROM decision WHERE challengeId = :challengeId ORDER BY decisionOrder ASC")
    suspend fun getForChallenge(challengeId: Long): List<Decision>

    @Query("SELECT * FROM decision_outcome WHERE decisionId = :decisionId LIMIT 1")
    suspend fun getOutcomeForDecision(decisionId: Long): DecisionOutcome?

    @Query("SELECT * FROM consequence WHERE id = :id")
    suspend fun getConsequence(id: Long): Consequence?
}
