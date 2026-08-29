# EcoLegal — Guardianes del Valle Verde

Aplicación educativa Android (nativa, Kotlin + Jetpack Compose) para niños de
8 a 12 años sobre educación ambiental y comprensión básica de normas y
responsabilidades, ambientada en un valle ficticio llamado **Valle Verde**.

> **Estado de compilación: NO VERIFICADA en el entorno de desarrollo.**
> Este proyecto se escribió en un contenedor sandbox sin acceso a
> `dl.google.com` / `maven.google.com` (repositorio Maven de Google) ni a
> `services.gradle.org`, por lo que `./gradlew` no pudo ejecutarse aquí.
> El repositorio incluye un workflow de GitHub Actions
> (`.github/workflows/build.yml`) que compila, testea y linta el proyecto
> automáticamente al hacer `git push`, en un entorno con acceso completo a
> internet. Ver `docs/BUILD_REPORT.md` para el detalle exacto de qué se pudo
> y no se pudo verificar localmente.

## Resumen

- **Paquete:** `com.educalab.ecolegal`
- **Versión:** 1.0.0
- **Idioma:** Español
- **Plataforma:** Android nativo, `minSdk 24`, `compileSdk/targetSdk 34`, JDK 17
- **Stack:** Kotlin, Jetpack Compose, Material 3, Navigation Compose, MVVM,
  Repository Pattern, Room, Coroutines/Flow
- **100% offline:** sin Firebase, sin backend, sin login, sin anuncios, sin
  analítica, sin permiso `INTERNET`.

## Concepto

El niño explora el Valle Verde a través de un **mapa ilustrado** (no un
dashboard ni una lista de botones) y visita 5 zonas: **El Bosque**, **El Río
Claro**, **La Comunidad**, **Refugio Animal** y **Campos Verdes**. Guiado por
**Luma**, una exploradora ambiental, el niño:

```
EXPLORA → OBSERVA → COMPRENDE → DECIDE → OBSERVA CONSECUENCIAS → REPARA → APRENDE
```

Cada zona combina 4 mecánicas educativas distintas (no solo opción múltiple):

| Mecánica | Qué hace el niño |
|---|---|
| **Decisión responsable** | Elige la mejor decisión ante una situación (con niveles correcto/parcial/incorrecto) |
| **Semáforo ambiental** | Clasifica acciones como verde / amarillo / rojo |
| **Detective Verde** | Toca y descubre elementos problemáticos en una escena ilustrada |
| **Ordenar pasos** | Ordena los pasos correctos de una acción responsable |
| **Reparar el entorno** | Elige y coloca elementos para restaurar visualmente una zona |
| **Ruta de la autorización** | Revisa impactos y medidas de cuidado, y decide autorizar / pedir cambios / no autorizar una actividad ficticia |

El progreso, las 12 insignias coleccionables y las mejoras visuales del valle
se derivan siempre de acciones reales del niño, persistidas en una base de
datos Room local.

## Estructura del repositorio

```
app/                    Código fuente Android (Kotlin + Compose)
database/                schema.sql y sample_data.sql (referencia, en SQL puro)
docs/                     Documentación (memoria, manuales, BD, build report) + PDFs
deliverables/             Artefactos finales (APK, ZIP fuente, PDFs) — se llenan tras compilar
.github/workflows/        CI que compila, testea y linta el proyecto en cada push
gradle/, gradlew*          Gradle Wrapper (ver nota sobre el .jar en BUILD_REPORT.md)
build.gradle.kts, settings.gradle.kts, gradle.properties
```

## Cómo compilar

### Opción recomendada: GitHub Actions
1. Sube este repositorio a GitHub.
2. El workflow `.github/workflows/build.yml` se ejecuta automáticamente y
   publica el APK, los resultados de tests y el reporte de lint como
   artefactos descargables de la Action.

### Localmente (Android Studio / línea de comandos)
Requiere JDK 17 y Android SDK (compileSdk 34) instalados, con acceso normal
a internet (Maven de Google/Maven Central).

```bash
./gradlew clean
./gradlew testDebugUnitTest
./gradlew lintDebug
./gradlew assembleDebug
```

Si `gradle/wrapper/gradle-wrapper.jar` no existe (no pudo descargarse en el
entorno de desarrollo original), genéralo una vez con un Gradle instalado
localmente:

```bash
gradle wrapper --gradle-version 8.7
```

## Privacidad

EcoLegal no solicita nombre real, correo, teléfono, dirección, ubicación
ni contactos. El perfil usa solo un alias y un avatar ilustrado local (8
opciones). Todos los datos se guardan únicamente en el dispositivo.

## Documentación

- `docs/MEMORIA_DESCRIPTIVA.md` — propósito, enfoque pedagógico y alcance
- `docs/MANUAL_USUARIO.md` — guía de uso pensada para niños/familias
- `docs/MANUAL_TECNICO.md` — arquitectura, motores, navegación, simplificaciones
- `docs/BASE_DE_DATOS.md` — modelo Room, entidades, relaciones, DER
- `docs/BUILD_REPORT.md` — estado real y honesto de compilación/tests
