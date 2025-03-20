package org.penakelex.ratingphysics.feature_rating.presentation.enter

import android.net.Uri
import androidx.compose.ui.focus.FocusManager

sealed class EnterEvent {
    data class FileSelected(val uri: Uri?) : EnterEvent()
    data class EnteredPassword(val value: String) : EnterEvent()
    data class ClearPasswordFocus(val focusManager: FocusManager): EnterEvent()
    data object ValidateData : EnterEvent()
}