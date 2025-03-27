package org.penakelex.ratingphysics.feature_rating.domain.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Immutable
data class PracticalLesson(
    @SerialName("not_attend") val notAttend: Boolean,
    val tasks: UByte?,
)