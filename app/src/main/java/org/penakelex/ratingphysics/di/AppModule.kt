package org.penakelex.ratingphysics.di

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.penakelex.ratingphysics.feature_rating.data.repository.RatingRepositoryImplementation
import org.penakelex.ratingphysics.feature_rating.domain.repository.RatingRepository
import org.penakelex.ratingphysics.feature_rating.domain.use_case.GetRatingDataUseCase
import org.penakelex.ratingphysics.feature_rating.domain.use_case.RatingUseCases
import org.penakelex.ratingphysics.feature_rating.presentation.enter.EnterViewModel
import org.penakelex.ratingphysics.feature_rating.presentation.rating.RatingDataViewModel

val appModule = module {
    singleOf(::RatingRepositoryImplementation) { bind<RatingRepository>() }
    single {
        val ratingRepository = get<RatingRepository>()
        RatingUseCases(
            getRatingData = GetRatingDataUseCase(ratingRepository)
        )
    }
    viewModelOf(::EnterViewModel)
    viewModelOf(::RatingDataViewModel)
}
