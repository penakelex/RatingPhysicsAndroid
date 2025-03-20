package org.penakelex.ratingphysics.feature_rating.domain.repository

import org.penakelex.ratingphysics.feature_rating.domain.model.RatingData
import java.io.File

interface RatingRepository {
    suspend fun getRatingDataByPassword(password: UInt, file: File): RatingData
}