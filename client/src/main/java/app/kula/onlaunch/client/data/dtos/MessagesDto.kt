package app.kula.onlaunch.client.data.dtos

import androidx.annotation.Keep
import app.kula.onlaunch.client.data.models.Action
import app.kula.onlaunch.client.data.models.Message
import com.google.gson.annotations.SerializedName

@Keep
internal data class MessageDto(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("body") val body: String,
    @SerializedName("blocking") val isBlocking: Boolean,
    @SerializedName("actions") val actions: List<ActionDto>,
)

@Keep
internal data class ActionDto(
    @SerializedName("title") val title: String,
    @SerializedName("actionType") val actionType: String?,
)

internal fun List<MessageDto>.toMessages() = map(MessageDto::toMessage)

internal fun MessageDto.toMessage() = Message(
    id = id,
    title = title,
    body = body,
    isBlocking = isBlocking,
    actions = actions.mapNotNull(ActionDto::toAction),
)

internal fun ActionDto.toAction(): Action? {
    return Action(
        title = title,
        actionType = when (actionType) {
            "DISMISS" -> Action.Type.DISMISS
            "OPEN_APP_IN_APP_STORE" -> Action.Type.OPEN_APP_IN_APP_STORE
            else -> return null
        },
    )
}
