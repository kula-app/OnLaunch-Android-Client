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
    @SerializedName("actionType") val actionType: Action.Type?,
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
    actionType = actionType ?: Action.Type.DISMISS,
)
