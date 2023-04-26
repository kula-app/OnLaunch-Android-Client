package app.kula.onlaunch.client.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
internal data class Message(
    val id: Int,
    val title: String,
    val body: String,
    val isBlocking: Boolean,
    val actions: List<Action>,
) : Parcelable

@Parcelize
internal data class Action(
    val title: String,
    val actionType: Type,
) : Parcelable {
    enum class Type {
        DISMISS
    }
}
