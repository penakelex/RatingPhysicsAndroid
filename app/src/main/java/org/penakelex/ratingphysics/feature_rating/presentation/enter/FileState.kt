package org.penakelex.ratingphysics.feature_rating.presentation.enter

import android.net.Uri

data class FileState(
    val uri: Uri? = null,
    val isValid: Boolean = true,
    val name: String? = null,
)