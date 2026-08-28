package com.educalab.verdelegal.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.educalab.verdelegal.data.local.dao.*
import com.educalab.verdelegal.data.local.entity.*

@Database(
    entities = [
        UserProfile::class,
        EnvironmentalZone::class,
        EnvironmentalScenario::class,
        EnvironmentalIssue::class,
        Decision::class,
        DecisionOutcome::class,
        Consequence::class,
        RestorationMission::class,
        RestorationStep::class,
        AuthorizationActivity::class,
        EnvironmentalImpact::class,
        ProtectionMeasure::class,
        AuthorizationDecision::class,
        Challenge::class,
        ChallengeAttempt::class,
        Progress::class,
        Badge::class,
        UserBadge::class,
        UnlockedZone::class
    ],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userProfileDao(): UserProfileDao
    abstract fun zoneDao(): ZoneDao
    abstract fun scenarioDao(): ScenarioDao
    abstract fun issueDao(): IssueDao
    abstract fun challengeDao(): ChallengeDao
    abstract fun decisionDao(): DecisionDao
    abstract fun restorationDao(): RestorationDao
    abstract fun authorizationDao(): AuthorizationDao
    abstract fun progressDao(): ProgressDao
    abstract fun badgeDao(): BadgeDao
    abstract fun unlockedZoneDao(): UnlockedZoneDao

    companion object {
        const val DB_NAME = "verdelegal.db"

        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DB_NAME
                ).build().also { INSTANCE = it }
            }

        /** Variante en memoria, usada por tests. */
        fun inMemory(context: Context): AppDatabase =
            Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
                .allowMainThreadQueries()
                .build()
    }
}
