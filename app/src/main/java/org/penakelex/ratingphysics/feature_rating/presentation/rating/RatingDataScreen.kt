package org.penakelex.ratingphysics.feature_rating.presentation.rating

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun RatingDataScreen(
    navController: NavController,
    viewModel: RatingDataViewModel = hiltViewModel()
) {
    val ratingData = viewModel.ratingData.value
    val dataState = viewModel.data.value


    val isPracticalLessonsCategoryOpened = remember {
        mutableStateOf(false)
    }

    val isCgtsCategoryOpened = remember {
        mutableStateOf(false)
    }

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .clickable {
                        navController.navigateUp()
                    },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    modifier = Modifier
                        .size(36.dp),
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Arrow back icon",
                )

                Spacer(modifier = Modifier.width(2.dp))

                Text(
                    text = "Назад",
                    fontSize = 18.sp,
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            when (dataState) {
                DataState.LoadingData -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.width(64.dp),
                            color = MaterialTheme.colorScheme.secondary,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant,
                        )
                    }
                }

                DataState.LoadedData -> {
                    val dataList = listOf(
                        "ФИО" to ratingData!!.fullName,
                        "Группа" to ratingData.group,
                        "Сумма" to ratingData.summary,
                        "Рейтинг в группе" to ratingData.ratingGroup,
                        "Рейтинг в потоке" to ratingData.ratingFlow,
                        "Коллоквиум" to ratingData.colloquium,
                        "РГЗ/Контрольные работы" to ratingData.cgtCw,
                        "Лабораторные работы" to ratingData.lw,
                        "Индивидуальные задания" to ratingData.it,
                        "Реферат" to ratingData.essay,
                        "НИРС" to ratingData.nirs,
                        "Сумма по практикам" to ratingData.sumPractice,
                        "Пропуски" to ratingData.omissions,
                    ).map { (naming, data) ->
                        naming to (data?.toString() ?: "Нет данных")
                    }

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        itemsIndexed(
                            items = dataList,
                            key = { _, it -> it.first },
                        ) { index, (naming, value) ->
                            Text(
                                text = buildAnnotatedString {
                                    append(
                                        AnnotatedString(
                                            "$naming: ",
                                            SpanStyle(fontWeight = FontWeight.SemiBold),
                                        )
                                    )
                                    append(value)
                                },
                                fontSize = 28.sp
                            )

                            if (index != dataList.lastIndex)
                                Spacer(modifier = Modifier.height(6.dp))
                        }

                        item {
                            DataCategory(
                                modifier = Modifier
                                    .padding(top = 6.dp),
                                isOpened = isPracticalLessonsCategoryOpened,
                                name = "Практические занятия",
                                categoryValues = ratingData.practicalLessons.map { (notAttend, tasks) ->
                                    buildString {
                                        append(tasks ?: "Нет данных")
                                        if (notAttend)
                                            append(" (Не был(-а))")
                                    }
                                },
                            )
                        }

                        item {
                            DataCategory(
                                modifier = Modifier
                                    .padding(top = 6.dp),
                                isOpened = isCgtsCategoryOpened,
                                name = "РГЗ",
                                categoryValues = ratingData.cgts.map {
                                    it?.toString() ?: "Нет данных"
                                },
                            )
                        }
                    }
                }

                DataState.NoLoadedData -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Не найден студент с данным паролем")
                    }
                }
            }
        }
    }
}

@Composable
private fun DataCategory(
    isOpened: MutableState<Boolean>,
    name: String,
    categoryValues: List<String>,
    modifier: Modifier = Modifier,
) {
    val rotationAngle = animateFloatAsState(
        targetValue = if (isOpened.value) 180f else 0f,
        animationSpec = tween(durationMillis = 300),
    )

    Row(
        modifier = modifier
            .clickable {
                isOpened.value = !isOpened.value
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = name,
            fontSize = 28.sp,
            fontWeight = FontWeight.SemiBold,
        )

        Spacer(modifier = Modifier.width(4.dp))

        Icon(
            imageVector = Icons.Filled.ArrowDropDown,
            contentDescription = "Is data category opened icon",
            modifier = Modifier
                .size(24.dp)
                .graphicsLayer(rotationZ = rotationAngle.value)
        )
    }

    AnimatedVisibility(
        visible = isOpened.value,
        enter = expandVertically(),
        exit = shrinkVertically(),
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
        ) {
            categoryValues.forEachIndexed { index, value ->
                Text(
                    text = "${index.inc()}. $value",
                    fontSize = 24.sp,
                )

                if (index != categoryValues.lastIndex)
                    Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}