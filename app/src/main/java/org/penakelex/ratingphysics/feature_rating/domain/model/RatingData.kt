package org.penakelex.ratingphysics.feature_rating.domain.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Immutable
data class RatingData(
    @SerialName("full_name") val fullName: String,
    val group: String,
    val summary: Float,
    @SerialName("rating_group") val ratingGroup: UByte,
    @SerialName("rating_flow") val ratingFlow: UShort,
    val colloquium: Float?,
    @SerialName("cgt_cw") val cgtCw: Float,
    val lw: Float?,
    val it: Float?,
    val essay: Float?,
    val nirs: Float?,
    @SerialName("sum_practice") val sumPractice: Float,
    val omissions: UByte,
    @SerialName("practical_lessons") val practicalLessons: List<PracticalLesson>,
    val cgts: List<UByte?>,
)