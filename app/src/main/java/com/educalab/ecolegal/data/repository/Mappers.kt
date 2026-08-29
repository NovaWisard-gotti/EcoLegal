package com.educalab.ecolegal.data.repository

import com.educalab.ecolegal.data.local.entity.*
import com.educalab.ecolegal.domain.model.*

/** Traduce entidades Room <-> modelos de dominio, para mantener los motores libres de Android/Room. */

fun EnvironmentalZone.toDomain() = ZoneInfo(id, code, displayName, shortDescription, mapOrder, mapX, mapY, unlockRequiredBadges, iconKey)

fun EnvironmentalScenario.toDomain() = ScenarioInfo(id, zoneId, title, lumaIntro, sceneOrder, backgroundKey)

fun EnvironmentalIssue.toDomain() = IssueInfo(id, scenarioId, title, description, iconKey, SeverityLevel.valueOf(severity), positionX, positionY)

fun Challenge.toDomain() = ChallengeInfo(id, scenarioId, ChallengeType.valueOf(type), title, prompt, difficulty, xpReward, challengeOrder)

fun Decision.toDomain() = DecisionOption(id, challengeId, text, isCorrect, isPartial, decisionOrder)

fun Consequence.toDomain() = ConsequenceInfo(id, description, SeverityLevel.valueOf(severity), visualKey, relatedRestorationMissionId)

fun RestorationStep.toDomain() = RestorationStepInfo(id, missionId, description, stepOrder, itemKey, targetSlotKey)

fun RestorationMission.toDomain(steps: List<RestorationStep>) =
    RestorationMissionInfo(id, zoneId, title, description, xpReward, badgeIdOnComplete, steps.map { it.toDomain() })

fun EnvironmentalImpact.toDomain() = ImpactInfo(impactText, SeverityLevel.valueOf(impactLevel))

fun ProtectionMeasure.toDomain() = MeasureInfo(id, measureText, isRecommended)

fun AuthorizationActivity.toDomain(impacts: List<EnvironmentalImpact>, measures: List<ProtectionMeasure>) =
    AuthorizationActivityInfo(
        id, zoneId, activityName, description, iconKey,
        AuthorizationChoice.valueOf(correctChoice),
        impacts.map { it.toDomain() },
        measures.map { it.toDomain() }
    )

fun Badge.toDomain() = BadgeInfo(id, code, name, description, iconKey, criteriaKey)
