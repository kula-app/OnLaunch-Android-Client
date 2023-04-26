package app.kula.onlaunch.client.data.dtos

import app.kula.onlaunch.client.data.models.Action
import app.kula.onlaunch.client.data.models.Message

internal data class MessageDto(
    val id: Int,
    val title: String,
    val body: String,
    val isBlocking: Boolean,
    val actions: List<ActionDto>,
)

internal data class ActionDto(
    val title: String,
    val actionType: String,
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
    actionType = Action.Type.valueOf(actionType),
)
