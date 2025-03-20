package org.penakelex.ratingphysics.feature_rating.presentation.enter

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.penakelex.ratingphysics.feature_rating.data.repository.InvalidPasswordException
import org.penakelex.ratingphysics.feature_rating.presentation.util.getFileNameByUri
import org.penakelex.ratingphysics.feature_rating.presentation.util.saveFileToCache
import javax.inject.Inject

@HiltViewModel
class EnterViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
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
                    isCorrect = enteredPassword.length < 5 && enteredPassword.matches(Regex("\\d+"))
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

                if (password.value.length != 5 || password.value.matches(Regex("\\d+")))
                    _password.value = password.copy(isCorrect = false)

                if (file.uri == null || getFileNameByUri(file.uri, context) == null)
                    _file.value = file.copy(isValid = false)

                if (!password.isCorrect || !file.isValid)
                    return

                val uri = file.uri ?: return
                val fileName = file.name ?: return

                viewModelScope.launch(Dispatchers.IO) {
                    try {
                        val cachedFileName = saveFileToCache(uri, fileName, context)
                        _eventFlow.emit(
                            UIEvent.ValidateData(password.value.toUInt(), cachedFileName)
                        )
                    } catch (exception: InvalidPasswordException) {
                        _eventFlow.emit(
                            UIEvent.ShowSnackbar(
                                exception.message
                                    ?: "Can't find rating data with given password"
                            )
                        )
                    }
                }
            }
        }
    }

    sealed class UIEvent {
        data class ValidateData(val password: UInt, val fileName: String) : UIEvent()
        data class ShowSnackbar(val message: String) : UIEvent()
    }
}
