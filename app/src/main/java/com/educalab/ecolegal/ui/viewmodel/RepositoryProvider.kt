package com.educalab.ecolegal.ui.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.educalab.ecolegal.EcoLegalApp
import com.educalab.ecolegal.data.repository.EcoLegalRepository

/** Da acceso al [EcoLegalRepository] singleton creado en [EcoLegalApp]. */
@Composable
fun rememberRepository(): EcoLegalRepository {
    val context = LocalContext.current.applicationContext as EcoLegalApp
    return context.repository
}

/**
 * Crea (o recupera) un ViewModel de EcoLegal inyectando el repositorio,
 * sin necesidad de escribir una Factory dedicada por cada pantalla.
 *
 * Uso: val vm = ecoLegalViewModel(key = "zone_$zoneId") { repo -> ZoneViewModel(repo, userId, zoneId) }
 */
@Composable
inline fun <reified VM : ViewModel> ecoLegalViewModel(
    key: String? = null,
    crossinline creator: (EcoLegalRepository) -> VM
): VM {
    val repository = rememberRepository()
    return viewModel(
        key = key,
        factory = viewModelFactory { initializer { creator(repository) } }
    )
}
