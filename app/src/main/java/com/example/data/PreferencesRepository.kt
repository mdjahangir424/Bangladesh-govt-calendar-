package com.example.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "bd_calendar_preferences")

class PreferencesRepository(private val context: Context) {

    companion object {
        val KEY_LANGUAGE = stringPreferencesKey("app_language") // "bn" or "en"
        val KEY_THEME = stringPreferencesKey("theme_mode") // "system", "light", "dark"
        val KEY_HIJRI_OFFSET = intPreferencesKey("hijri_day_offset") // -2 to +2
        val KEY_NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")
        val KEY_NOTIFY_ON_DAY = booleanPreferencesKey("notify_on_day")
        val KEY_NOTIFY_DAY_BEFORE = booleanPreferencesKey("notify_day_before")
    }

    val languageFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[KEY_LANGUAGE] ?: "bn"
    }

    val themeFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[KEY_THEME] ?: "system"
    }

    val hijriOffsetFlow: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[KEY_HIJRI_OFFSET] ?: 0
    }

    val notificationsEnabledFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[KEY_NOTIFICATIONS_ENABLED] ?: true
    }

    val notifyOnDayFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[KEY_NOTIFY_ON_DAY] ?: true
    }

    val notifyDayBeforeFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[KEY_NOTIFY_DAY_BEFORE] ?: true
    }

    suspend fun setLanguage(language: String) {
        context.dataStore.edit { preferences ->
            preferences[KEY_LANGUAGE] = language
        }
    }

    suspend fun setTheme(theme: String) {
        context.dataStore.edit { preferences ->
            preferences[KEY_THEME] = theme
        }
    }

    suspend fun setHijriOffset(offset: Int) {
        val clamped = offset.coerceIn(-2, 2)
        context.dataStore.edit { preferences ->
            preferences[KEY_HIJRI_OFFSET] = clamped
        }
    }

    suspend fun setNotificationsEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[KEY_NOTIFICATIONS_ENABLED] = enabled
        }
    }

    suspend fun setNotifyOnDay(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[KEY_NOTIFY_ON_DAY] = enabled
        }
    }

    suspend fun setNotifyDayBefore(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[KEY_NOTIFY_DAY_BEFORE] = enabled
        }
    }

    suspend fun resetPreferences() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
