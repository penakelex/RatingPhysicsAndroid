package org.penakelex.ratingphysics.feature_rating.domain.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Serializable
@Immutable
data class PracticalLesson(
    val notAttend: Boolean,
    val tasks: UByte?,
)