package app.kula.onlaunch.client.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

private val KEY_DISMISSED_MESSAGE_IDS = stringSetPreferencesKey("isDataSharingEnabled")

internal class OnLaunchDataStore(context: Context) {

    private val dataStore: DataStore<Preferences> = context.onLaunchDataStore

    suspend fun getDismissedMessageIds(): Set<Int> = dataStore.data.first().let {
        it[KEY_DISMISSED_MESSAGE_IDS]?.map(String::toInt)?.toSet() ?: emptySet()
    }

    suspend fun addDismissedMessageId(messageId: Int) {
        dataStore.edit {
            it[KEY_DISMISSED_MESSAGE_IDS] =
                (it[KEY_DISMISSED_MESSAGE_IDS] ?: emptySet()) + messageId.toString()
        }
    }
}


private val Context.onLaunchDataStore by preferencesDataStore(
    name = "OnLaunch"
)
