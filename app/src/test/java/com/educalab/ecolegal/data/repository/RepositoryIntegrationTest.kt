package com.educalab.ecolegal.data.repository

import androidx.test.core.app.ApplicationProvider
import com.educalab.ecolegal.data.local.AppDatabase
import com.educalab.ecolegal.data.local.entity.AuthorizationChoice
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Pruebas de integración sobre una base de datos Room real en memoria
 * (sin red, sin mocks de las tablas) para validar que el progreso, las
 * insignias y los desbloqueos se derivan correctamente de acciones reales.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class RepositoryIntegrationTest {

    private lateinit var db: AppDatabase
    private lateinit var repository: EcoLegalRepository

    @Before
    fun setUp() = runTest {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        db = AppDatabase.inMemory(context)
        repository = EcoLegalRepository(db)
        repository.ensureSeeded()
    }

    @Test
    fun `seeding twice does not duplicate zones`() = runTest {
        repository.ensureSeeded()
        repository.ensureSeeded()
        assertEquals(5, db.zoneDao().count())
    }

    @Test
    fun `creating a profile initializes progress rows for every zone`() = runTest {
        val profile = repository.createProfile("Guardián Uno", "avatar_zorro")
        val progress = db.progressDao().getForUser(profile.id)
        assertEquals(5, progress.size)
    }

    @Test
    fun `only the first zone is unlocked for a brand new profile`() = runTest {
        val profile = repository.createProfile("Guardián Dos", "avatar_nutria")
        val unlocked = db.unlockedZoneDao().getForUser(profile.id)
        assertEquals(1, unlocked.size)
    }

    @Test
    fun `completing a decision challenge correctly increases xp and progress`() = runTest {
        val profile = repository.createProfile("Guardián Tres", "avatar_buho")
        val challenge = repository.getChallenge(301L)!!
        val options = repository.getDecisionsForChallenge(301L)
        val correct = options.first { it.isCorrect }

        repository.submitDecision(profile.id, challenge, correct)

        val zoneId = db.scenarioDao().getById(challenge.scenarioId)!!.zoneId
        val progress = db.progressDao().getForUserAndZone(profile.id, zoneId)
        assertNotNull(progress)
        assertEquals(1, progress!!.challengesCompleted)
        assertTrue(progress.xp > 0)
    }

    @Test
    fun `completing the first challenge awards the Primer Guardian badge`() = runTest {
        val profile = repository.createProfile("Guardián Cuatro", "avatar_tortuga")
        val challenge = repository.getChallenge(301L)!!
        val options = repository.getDecisionsForChallenge(301L)
        val correct = options.first { it.isCorrect }

        repository.submitDecision(profile.id, challenge, correct)

        val badges = repository.getBadgesOverview(profile.id)
        assertTrue(badges.any { it.badge.code == "PRIMER_GUARDIAN" && it.earned })
    }

    @Test
    fun `an incorrect decision does not increase completed challenge count`() = runTest {
        val profile = repository.createProfile("Guardián Cinco", "avatar_colibri")
        val challenge = repository.getChallenge(301L)!!
        val options = repository.getDecisionsForChallenge(301L)
        val incorrect = options.first { !it.isCorrect && !it.isPartial }

        repository.submitDecision(profile.id, challenge, incorrect)

        val zoneId = db.scenarioDao().getById(challenge.scenarioId)!!.zoneId
        val progress = db.progressDao().getForUserAndZone(profile.id, zoneId)
        assertEquals(0, progress!!.challengesCompleted)
    }

    @Test
    fun `completing a restoration mission increases restoration progress`() = runTest {
        val profile = repository.createProfile("Guardián Seis", "avatar_conejo")
        val mission = repository.getRestorationMission(701L)!!

        repository.completeRestorationMission(profile.id, mission)

        val progress = db.progressDao().getForUserAndZone(profile.id, mission.zoneId)
        assertTrue(progress!!.restorationCompleted >= 1)
    }

    @Test
    fun `an authorization decision is persisted with its correctness`() = runTest {
        val profile = repository.createProfile("Guardián Siete", "avatar_mapache")
        val activity = repository.getAuthorizationActivity(902L)!! // impactos solo LOW -> recomendación AUTORIZAR

        repository.submitAuthorizationDecision(profile.id, activity, AuthorizationChoice.AUTORIZAR, emptyList())

        val decisions = db.authorizationDao().getDecisionsForUser(profile.id)
        assertEquals(1, decisions.size)
        assertTrue(decisions.first().isCorrect)
    }

    @Test
    fun `progress and badges persist across repository instances backed by the same database`() = runTest {
        val profile = repository.createProfile("Guardián Ocho", "avatar_ardilla")
        val challenge = repository.getChallenge(301L)!!
        val correct = repository.getDecisionsForChallenge(301L).first { it.isCorrect }
        repository.submitDecision(profile.id, challenge, correct)

        // Simula "reabrir la app": un nuevo repositorio sobre la MISMA base de datos.
        val reopenedRepository = EcoLegalRepository(db)
        val badges = reopenedRepository.getBadgesOverview(profile.id)
        assertTrue(badges.any { it.earned })
    }
}
