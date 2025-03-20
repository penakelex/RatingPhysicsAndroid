package org.penakelex.ratingphysics.di

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.penakelex.ratingphysics.feature_rating.data.repository.RatingRepositoryImplementation
import org.penakelex.ratingphysics.feature_rating.domain.repository.RatingRepository
import org.penakelex.ratingphysics.feature_rating.domain.use_case.GetRatingDataUseCase
import org.penakelex.ratingphysics.feature_rating.domain.use_case.RatingUseCases
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideRatingRepository(): RatingRepository =
        RatingRepositoryImplementation()

    @Provides
    @Singleton
    fun provideRatingUseCases(repository: RatingRepository): RatingUseCases = RatingUseCases(
        getRatingData = GetRatingDataUseCase(repository),
    )

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context = context
}