package org.penakelex.ratingphysics.feature_rating.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.append
import io.ktor.serialization.kotlinx.json.json
import org.penakelex.ratingphysics.feature_rating.domain.model.RatingData
import org.penakelex.ratingphysics.feature_rating.domain.repository.RatingRepository
import java.io.File
import kotlin.jvm.Throws

class RatingRepositoryImplementation : RatingRepository {
    private val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json()
        }
    }

    @Throws(InvalidPasswordException::class)
    override suspend fun getRatingDataByPassword(password: UInt, file: File): RatingData {
        val response = client
            .get("http://127.0.0.1:8000/rating_physics/decipher") {
                formData {
                    append(
                        key = "file",
                        value = file.readBytes(),
                        headers = Headers.build {
                            append(HttpHeaders.ContentType, ContentType.Application.OctetStream)
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
            }

        if (response.status.value != 200) {
            throw InvalidPasswordException("Student with password $password not found in file")
        }

        return response.body<RatingData>()
    }
}