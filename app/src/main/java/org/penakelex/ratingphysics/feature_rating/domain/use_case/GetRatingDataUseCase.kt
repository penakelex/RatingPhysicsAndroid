package org.penakelex.ratingphysics.feature_rating.domain.use_case

import org.penakelex.ratingphysics.feature_rating.data.repository.InvalidPasswordException
import org.penakelex.ratingphysics.feature_rating.domain.model.RatingData
import org.penakelex.ratingphysics.feature_rating.domain.repository.RatingRepository
import java.io.File
import kotlin.jvm.Throws

class GetRatingDataUseCase(private val repository: RatingRepository) {
    @Throws(InvalidPasswordException::class)
    suspend operator fun invoke(password: UInt, file: File): RatingData =
        repository.getRatingDataByPassword(password, file)
}