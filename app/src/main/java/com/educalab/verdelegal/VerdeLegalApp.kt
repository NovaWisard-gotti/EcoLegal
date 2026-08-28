package com.educalab.verdelegal

import android.app.Application
import com.educalab.verdelegal.data.local.AppDatabase
import com.educalab.verdelegal.data.repository.VerdeLegalRepository

/** Application: crea la base de datos Room y el repositorio como singletons simples. */
class VerdeLegalApp : Application() {
    lateinit var repository: VerdeLegalRepository
        private set

    override fun onCreate() {
        super.onCreate()
        val db = AppDatabase.getInstance(this)
        repository = VerdeLegalRepository(db)
    }
}
