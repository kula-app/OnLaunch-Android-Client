package app.kula.onlaunch.client.data.api

import app.kula.onlaunch.client.data.dtos.MessageDto
import retrofit2.http.GET
import retrofit2.http.Header

internal interface OnLaunchApi {
    @GET("v0.2/messages")
    suspend fun getMessages(
        @Header("x-api-key") publicKey: String,
        @Header("X-ONLAUNCH-LOCALE") locale: String,
        @Header("X-ONLAUNCH-LOCALE-LANGUAGE-CODE") localeLanguageCode: String,
        @Header("X-ONLAUNCH-LOCALE-REGION-CODE") localeRegionCode: String,
        @Header("X-ONLAUNCH-VERSION-CODE") versionCode: String,
        @Header("X-ONLAUNCH-VERSION-NAME") versionName: String,
        @Header("X-ONLAUNCH-PACKAGE-NAME") packageName: String,
        @Header("X-ONLAUNCH-PLATFORM-NAME") platformName: String,
        @Header("X-ONLAUNCH-PLATFORM-VERSION") platformVersion: String,
        @Header("X-ONLAUNCH-UPDATE-AVAILABLE") updateAvailable: Boolean?,
    ): List<MessageDto>
}
