package app.kula.onlaunch.client.data.dtos

import app.kula.onlaunch.client.data.models.Action
import app.kula.onlaunch.client.data.models.Message
import com.google.gson.annotations.SerializedName

internal data class MessageDto(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("body") val body: String,
    @SerializedName("blocking") val isBlocking: Boolean,
    @SerializedName("actions") val actions: List<ActionDto>,
)

internal data class ActionDto(
    @SerializedName("title") val title: String,
    @SerializedName("actionType") val actionType: String,
)

internal fun List<MessageDto>.toMessages() = map(MessageDto::toMessage)

internal fun MessageDto.toMessage() = Message(
    id = id,
    title = title,
    body = body,
    isBlocking = isBlocking,
    actions = actions.map(ActionDto::toAction)
)

internal fun ActionDto.toAction() = Action(
    title = title,
    actionType = when (actionType) {
        "DISMISS" -> Action.Type.DISMISS
        else -> Action.Type.DISMISS
    },
)
