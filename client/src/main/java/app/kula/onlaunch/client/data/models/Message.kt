package app.kula.onlaunch.client.data.models

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
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
    @Keep
    enum class Type {
        @SerializedName("DISMISS")
        DISMISS,
        @SerializedName("OPEN_APP_IN_APP_STORE")
        OPEN_APP_IN_APP_STORE
    }
}
