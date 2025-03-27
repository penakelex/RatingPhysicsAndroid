package org.penakelex.ratingphysics.feature_rating.presentation.rating

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.penakelex.ratingphysics.feature_rating.data.repository.CanNotAccessServerException
import org.penakelex.ratingphysics.feature_rating.data.repository.InvalidPasswordException
import org.penakelex.ratingphysics.feature_rating.domain.model.RatingData
import org.penakelex.ratingphysics.feature_rating.domain.use_case.RatingUseCases
import java.io.File

class RatingDataViewModel(
    private val ratingUseCases: RatingUseCases,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _ratingData: MutableState<RatingData?> = mutableStateOf(null)
    val ratingData: State<RatingData?> = _ratingData

    private val _data = mutableStateOf(DataState.LoadingData)
    val data: State<DataState> = _data

    init {
        val password = savedStateHandle.get<Int>("password")?.toUInt()
        val filePath = savedStateHandle.get<String>("filePath")

        if (password == null || filePath == null) {
            _data.value = DataState.NoLoadedData
        } else {
            this.viewModelScope.launch(Dispatchers.IO) {
                try {
                    _ratingData.value = ratingUseCases.getRatingData(password, File(filePath))
                    _data.value = DataState.LoadedData
                } catch (exception: InvalidPasswordException) {
                    exception.printStackTrace()
                    _data.value = DataState.NoLoadedData
                } catch (exception: CanNotAccessServerException) {
                    exception.printStackTrace()
                    _data.value = DataState.CantAccessServer
                }
            }
        }
    }
}