package org.penakelex.ratingphysics.feature_rating.presentation.enter

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.io.IOException
import org.penakelex.ratingphysics.feature_rating.presentation.util.getFileNameByUri
import org.penakelex.ratingphysics.feature_rating.presentation.util.saveFileToCache

class EnterViewModel(
    private val context: Application,
) : ViewModel() {
    private val _password = mutableStateOf(PasswordState())
    val password: State<PasswordState> = _password

    private val _file = mutableStateOf(FileState())
    val file: State<FileState> = _file

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow: SharedFlow<UIEvent> = _eventFlow.asSharedFlow()

    fun onEvent(event: EnterEvent) {
        when (event) {
            is EnterEvent.EnteredPassword -> {
                val enteredPassword = event.value

                val passwordValue =
                    if (enteredPassword.lastOrNull() != '\n') enteredPassword
                    else password.value.value

                _password.value = password.value.copy(
                    value = passwordValue,
                    isCorrect = enteredPassword.length <= 5
                            && enteredPassword.matches(Regex("\\d+"))
                )
            }

            is EnterEvent.FileSelected -> {
                val uri = event.uri ?: return

                val fileName = getFileNameByUri(uri, context)

                _file.value =
                    if (fileName == null) file.value.copy(
                        uri = null,
                        isValid = false,
                        name = null,
                    ) else file.value.copy(
                        uri = uri,
                        isValid = true,
                        name = fileName
                    )
            }

            is EnterEvent.ClearPasswordFocus -> {
                event.focusManager.clearFocus()
            }

            is EnterEvent.ValidateData -> {
                val password = password.value
                val file = file.value

                var isPasswordCorrect = true
                var isFileValid = true

                if (password.value.length !in 4..5
                    || !password.value.matches(Regex("\\d+"))
                ) {
                    _password.value = password.copy(isCorrect = false)
                    isPasswordCorrect = false
                }

                if (file.uri == null || getFileNameByUri(file.uri, context) == null) {
                    _file.value = file.copy(isValid = false)
                    isFileValid = false
                }

                if (!isPasswordCorrect || !isFileValid)
                    return

                val uri = file.uri ?: return
                val fileName = file.name ?: return

                viewModelScope.launch(Dispatchers.IO) {
                    val uiEvent = try {
                        val cachedFileName = saveFileToCache(uri, fileName, context)
                        UIEvent.ValidateData(password.value.toUInt(), cachedFileName)
                    } catch (exception: IOException) {
                        exception.printStackTrace()
                        UIEvent.ShowSnackbar("File $fileName not found in given path...")
                    }

                    _eventFlow.emit(uiEvent)
                }
            }
        }
    }

    sealed class UIEvent {
        data class ValidateData(val password: UInt, val fileName: String) : UIEvent()
        data class ShowSnackbar(val message: String) : UIEvent()
    }
}
