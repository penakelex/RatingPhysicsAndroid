package org.penakelex.ratingphysics.feature_rating.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.append
import io.ktor.http.contentType
import kotlinx.serialization.json.Json
import org.penakelex.ratingphysics.feature_rating.domain.model.RatingData
import org.penakelex.ratingphysics.feature_rating.domain.repository.RatingRepository
import java.io.File
import java.net.ConnectException

class RatingRepositoryImplementation : RatingRepository {
    companion object {
        private const val DEFAULT_URL = "http://45.90.46.187:8000/rating_physics"
    }

    private val client = HttpClient(CIO)

    @Throws(InvalidPasswordException::class, CanNotAccessServerException::class)
    override suspend fun getRatingDataByPassword(password: UInt, file: File): RatingData {
        val response = try {
            client.post("$DEFAULT_URL/decipher") {
                setBody(
                    MultiPartFormDataContent(
                        parts = formData {
                            append(
                                key = "file",
                                value = file.readBytes(),
                                headers = Headers.build {
                                    append(
                                        HttpHeaders.ContentType,
                                        ContentType.Application.OctetStream
                                    )
                                }
                            )
                            append(
                                key = "password",
                                value = password.toString(),
                                headers = Headers.build {
                                    append(HttpHeaders.ContentType, ContentType.Application.Json)
                                }
                            )
                        }
                    )
                )

                contentType(ContentType.MultiPart.Mixed)
            }
        } catch (exception: ConnectException) {
            exception.printStackTrace()
            throw CanNotAccessServerException("Can't access server")
        }

        if (response.status.value != 200) {
            throw InvalidPasswordException("Student with password $password not found in file")
        }

        return Json.decodeFromString(response.bodyAsText())
    }
}