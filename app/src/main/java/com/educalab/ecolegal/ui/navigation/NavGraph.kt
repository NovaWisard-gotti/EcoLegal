package com.educalab.ecolegal.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.educalab.ecolegal.ui.screens.*
import com.educalab.ecolegal.ui.theme.ForestMid
import com.educalab.ecolegal.ui.viewmodel.AppStartState
import com.educalab.ecolegal.ui.viewmodel.AppViewModel
import com.educalab.ecolegal.ui.viewmodel.ecoLegalViewModel

@Composable
fun EcoLegalNavGraph() {
    val appViewModel = ecoLegalViewModel(key = "app") { repo -> AppViewModel(repo) }
    val startState by appViewModel.startState.collectAsState()

    when (val state = startState) {
        is AppStartState.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = ForestMid) }
        }
        is AppStartState.NeedsProfile -> {
            ProfileSetupScreen(onCreate = { alias, avatarKey -> appViewModel.createProfile(alias, avatarKey) })
        }
        is AppStartState.Ready -> {
            if (!state.profile.onboardingCompleted) {
                OnboardingScreen(onFinished = { appViewModel.completeOnboarding() })
            } else {
                MainNavHost(userId = state.profile.id)
            }
        }
    }
}

@Composable
private fun MainNavHost(userId: Long) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.MAP,
        enterTransition = { fadeIn(tween(220)) },
        exitTransition = { fadeOut(tween(180)) }
    ) {
        composable(Routes.MAP) {
            MapScreen(
                userId = userId,
                onOpenZone = { zoneId -> navController.navigate(Routes.zone(zoneId)) },
                onOpenBadges = { navController.navigate(Routes.BADGES) }
            )
        }
        composable(
            Routes.ZONE,
            arguments = listOf(navArgument("zoneId") { type = NavType.LongType })
        ) { backStackEntry ->
            val zoneId = backStackEntry.arguments?.getLong("zoneId") ?: return@composable
            ZoneScreen(
                userId = userId,
                zoneId = zoneId,
                onBack = { navController.popBackStack() },
                onOpenChallenge = { challengeId -> navController.navigate(Routes.challenge(challengeId)) },
                onOpenRestoration = { missionId -> navController.navigate(Routes.restoration(missionId)) },
                onOpenAuthorization = { activityId -> navController.navigate(Routes.authorization(activityId)) }
            )
        }
        composable(
            Routes.CHALLENGE,
            arguments = listOf(navArgument("challengeId") { type = NavType.LongType })
        ) { backStackEntry ->
            val challengeId = backStackEntry.arguments?.getLong("challengeId") ?: return@composable
            ChallengeScreen(userId = userId, challengeId = challengeId, onBack = { navController.popBackStack() })
        }
        composable(
            Routes.RESTORATION,
            arguments = listOf(navArgument("missionId") { type = NavType.LongType })
        ) { backStackEntry ->
            val missionId = backStackEntry.arguments?.getLong("missionId") ?: return@composable
            RestorationScreen(userId = userId, missionId = missionId, onBack = { navController.popBackStack() })
        }
        composable(
            Routes.AUTHORIZATION,
            arguments = listOf(navArgument("activityId") { type = NavType.LongType })
        ) { backStackEntry ->
            val activityId = backStackEntry.arguments?.getLong("activityId") ?: return@composable
            AuthorizationScreen(userId = userId, activityId = activityId, onBack = { navController.popBackStack() })
        }
        composable(Routes.BADGES) {
            BadgesScreen(userId = userId, onBack = { navController.popBackStack() })
        }
        composable(Routes.REVIEW) {
            ReviewScreen(
                userId = userId,
                onBack = { navController.popBackStack() },
                onOpenChallenge = { challengeId -> navController.navigate(Routes.challenge(challengeId)) }
            )
        }
    }
}
