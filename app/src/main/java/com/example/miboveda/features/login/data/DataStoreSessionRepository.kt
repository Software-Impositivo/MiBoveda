package com.example.miboveda.features.login.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.miboveda.features.login.domain.model.AuthUser
import com.example.miboveda.features.login.domain.repository.SessionRepository
import java.io.IOException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

// Una sola instancia por archivo: se declara a nivel de archivo, nunca dentro de una clase.
private val Context.sessionDataStore by preferencesDataStore(name = "session")

private val EMAIL = stringPreferencesKey("email")
private val DISPLAY_NAME = stringPreferencesKey("display_name")
private val PHOTO_URL = stringPreferencesKey("photo_url")

class DataStoreSessionRepository(context: Context) : SessionRepository {

    private val dataStore = context.applicationContext.sessionDataStore

    override val session: Flow<AuthUser?> = dataStore.data
        .catch { error ->
            if (error is IOException) emit(emptyPreferences()) else throw error
        }
        .map { prefs ->
            val email = prefs[EMAIL] ?: return@map null
            AuthUser(
                email = email,
                displayName = prefs[DISPLAY_NAME],
                photoUrl = prefs[PHOTO_URL],
            )
        }

    override suspend fun save(user: AuthUser) {
        dataStore.edit { prefs ->
            prefs[EMAIL] = user.email
            if (user.displayName != null) prefs[DISPLAY_NAME] = user.displayName else prefs.remove(DISPLAY_NAME)
            if (user.photoUrl != null) prefs[PHOTO_URL] = user.photoUrl else prefs.remove(PHOTO_URL)
        }
    }

    override suspend fun clear() {
        dataStore.edit { it.clear() }
    }
}