package org.penakelex.ratingphysics.feature_rating.presentation.util

sealed class Screen(val route: String) {
    data object EnterScreen : Screen("enter_screen")
    data object RatingDataScreen : Screen("rating_data_screen")
}