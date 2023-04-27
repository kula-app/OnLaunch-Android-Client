package app.kula.onlaunch.client.data.api

import app.kula.onlaunch.client.data.dtos.MessageDto
import retrofit2.http.GET
import retrofit2.http.Header

internal interface OnLaunchApi {
    @GET("v0.1/messages")
    suspend fun getMessages(
        @Header("x-api-key") publicKey: String,
    ): List<MessageDto>
}
