package com.educalab.ecolegal.data.local.seed

/** 8 avatares locales ilustrados. Ningún avatar usa fotos reales ni datos personales. */
object AvatarSeed {
    data class AvatarOption(val key: String, val label: String)

    val avatars = listOf(
        AvatarOption("avatar_zorro", "Zorro explorador"),
        AvatarOption("avatar_nutria", "Nutria del río"),
        AvatarOption("avatar_buho", "Búho observador"),
        AvatarOption("avatar_tortuga", "Tortuga guardiana"),
        AvatarOption("avatar_colibri", "Colibrí veloz"),
        AvatarOption("avatar_conejo", "Conejo curioso"),
        AvatarOption("avatar_mapache", "Mapache detective"),
        AvatarOption("avatar_ardilla", "Ardilla aventurera")
    )
}
