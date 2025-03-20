package org.penakelex.ratingphysics.feature_rating.presentation.rating

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.penakelex.ratingphysics.feature_rating.data.repository.InvalidPasswordException
import org.penakelex.ratingphysics.feature_rating.domain.model.RatingData
import org.penakelex.ratingphysics.feature_rating.domain.use_case.RatingUseCases
import java.io.File
import javax.inject.Inject

@HiltViewModel
class RatingDataViewModel @Inject constructor(
    private val ratingUseCases: RatingUseCases,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _ratingData: MutableState<RatingData?> = mutableStateOf(null)
    val ratingData: State<RatingData?> = _ratingData

    private val _data = mutableStateOf(DataState.LoadingData)
    val data: State<DataState> = _data

    init {
        this.viewModelScope.launch(Dispatchers.IO) {
            Log.d("KEYS", savedStateHandle.keys().joinToString())

            val password = savedStateHandle.get<Int>("password")!!.toUInt()
            val filePath = savedStateHandle.get<String>("filePath")!!

            try {
                _ratingData.value = ratingUseCases.getRatingData(password, File(filePath))
                _data.value = DataState.LoadedData
            } catch (exception: InvalidPasswordException) {
                exception.printStackTrace()
                _data.value = DataState.NoLoadedData
            }
        }
    }
}