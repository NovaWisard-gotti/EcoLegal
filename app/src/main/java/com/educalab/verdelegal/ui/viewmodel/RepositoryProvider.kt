package com.educalab.verdelegal.ui.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.educalab.verdelegal.VerdeLegalApp
import com.educalab.verdelegal.data.repository.VerdeLegalRepository

/** Da acceso al [VerdeLegalRepository] singleton creado en [VerdeLegalApp]. */
@Composable
fun rememberRepository(): VerdeLegalRepository {
    val context = LocalContext.current.applicationContext as VerdeLegalApp
    return context.repository
}

/**
 * Crea (o recupera) un ViewModel de VerdeLegal inyectando el repositorio,
 * sin necesidad de escribir una Factory dedicada por cada pantalla.
 *
 * Uso: val vm = verdeLegalViewModel(key = "zone_$zoneId") { repo -> ZoneViewModel(repo, userId, zoneId) }
 */
@Composable
inline fun <reified VM : ViewModel> verdeLegalViewModel(
    key: String? = null,
    crossinline creator: (VerdeLegalRepository) -> VM
): VM {
    val repository = rememberRepository()
    return viewModel(
        key = key,
        factory = viewModelFactory { initializer { creator(repository) } }
    )
}
