package org.penakelex.ratingphysics.feature_rating.presentation.enter

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.flow.collectLatest
import org.penakelex.ratingphysics.R
import org.penakelex.ratingphysics.feature_rating.presentation.util.Screen

@Composable
fun EnterScreen(
    navController: NavController,
    viewModel: EnterViewModel = hiltViewModel()
) {
    val focusManager = LocalFocusManager.current

    val password = viewModel.password.value
    val file = viewModel.file.value

    val fileSelectLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri -> viewModel.onEvent(EnterEvent.FileSelected(uri)) }
    )

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is EnterViewModel.UIEvent.ValidateData -> {
                    navController.navigate(
                        "%s?password={%d}&filePath={%s}"
                            .format(Screen.RatingDataScreen.route, event.password, event.fileName)
                    )
                }
                is EnterViewModel.UIEvent.ShowSnackbar -> {
                    TODO()
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        viewModel.onEvent(EnterEvent.ClearPasswordFocus(focusManager))
                    },
                )
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "Пароль:",
                fontSize = 28.sp,
            )

            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = password.value,
                onValueChange = {
                    viewModel.onEvent(EnterEvent.EnteredPassword(it))
                },
                placeholder = {
                    Text("Номер в группе + Номер группы (например, 1401)")
                },
                textStyle = TextStyle(
                    fontSize = 24.sp,
                ),
                keyboardOptions = KeyboardOptions(
                    autoCorrectEnabled = false,
                    keyboardType = KeyboardType.Number,
                ),
                isError = !password.isCorrect,
                maxLines = 1,
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Column(modifier = Modifier) {
            Text(
                text = "Файл:",
                fontSize = 28.sp,
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(
                    modifier = Modifier
                        .size(48.dp),
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    ),
                    onClick = {
                        fileSelectLauncher.launch(arrayOf("application/octet-stream"))
                    }
                ) {
                    Icon(
                        modifier = Modifier
                            .padding(3.dp)
                            .fillMaxSize(),
                        painter = painterResource(R.drawable.file_icon),
                        contentDescription = "File",
                    )
                }

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = file.name ?: "(Не выбран)",
                    fontSize = 24.sp,
                    color = if (file.isValid) Color.Unspecified else Color.Red
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .imePadding(),
            onClick = { viewModel.onEvent(EnterEvent.ValidateData) }
        ) {
            Text(
                text = "Далее",
                fontSize = 24.sp,
            )
        }
    }
}