package com.example.maisonflowers.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Extensión para crear una instancia de DataStore a nivel de aplicación
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

// Enum para representar los diferentes modos de tema
enum class ThemeMode {
    SYSTEM, // Sigue la configuración del sistema
    LIGHT,  // Siempre modo claro
    DARK    // Siempre modo oscuro
}

class ThemeManager(private val context: Context) {

    // Clave para almacenar la preferencia del tema en DataStore
    private val THEME_KEY = stringPreferencesKey("theme_mode")

    // Flujo para observar el modo de tema actual
    val themeMode: Flow<ThemeMode> = context.dataStore.data
        .map { preferences ->
            // Lee el valor de la preferencia, si no existe, por defecto es SYSTEM
            ThemeMode.valueOf(preferences[THEME_KEY] ?: ThemeMode.SYSTEM.name)
        }

    // Función para guardar el modo de tema seleccionado
    suspend fun saveThemeMode(mode: ThemeMode) {
        context.dataStore.edit { preferences ->
            preferences[THEME_KEY] = mode.name
        }
    }
}
