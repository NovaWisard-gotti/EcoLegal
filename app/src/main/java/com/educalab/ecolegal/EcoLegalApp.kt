package com.educalab.ecolegal

import android.app.Application
import com.educalab.ecolegal.data.local.AppDatabase
import com.educalab.ecolegal.data.repository.EcoLegalRepository

/** Application: crea la base de datos Room y el repositorio como singletons simples. */
class EcoLegalApp : Application() {
    lateinit var repository: EcoLegalRepository
        private set

    override fun onCreate() {
        super.onCreate()
        val db = AppDatabase.getInstance(this)
        repository = EcoLegalRepository(db)
    }
}
