package app.kula.onlaunch.client.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

private val KEY_DISMISSED_MESSAGE_IDS = stringSetPreferencesKey("isDataSharingEnabled")

internal class OnLaunchDataStore(context: Context) {

    private val dataStore: DataStore<Preferences> = context.onLaunchDataStore

    suspend fun getDismissedMessageIds(): Set<Int> = withContext(Dispatchers.IO) {
        dataStore.data.first().let {
            it[KEY_DISMISSED_MESSAGE_IDS]?.map(String::toInt)?.toSet() ?: emptySet()
        }
    }

    suspend fun addDismissedMessageId(messageId: Int) = withContext(Dispatchers.IO) {
        dataStore.edit {
            it[KEY_DISMISSED_MESSAGE_IDS] =
                (it[KEY_DISMISSED_MESSAGE_IDS] ?: emptySet()) + messageId.toString()
        }
    }
}


private val Context.onLaunchDataStore by preferencesDataStore(
    name = "OnLaunch"
)
