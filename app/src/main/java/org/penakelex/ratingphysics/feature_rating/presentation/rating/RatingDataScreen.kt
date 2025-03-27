package org.penakelex.ratingphysics.feature_rating.presentation.rating

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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel
import org.penakelex.ratingphysics.R
import org.penakelex.ratingphysics.feature_rating.presentation.rating.components.DataCategory

@Composable
fun RatingDataScreen(
    navController: NavController,
    viewModel: RatingDataViewModel = koinViewModel(),
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
                    text = stringResource(R.string.back_button_text),
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
                        stringResource(R.string.full_name_title) to ratingData!!.fullName,
                        stringResource(R.string.group_title) to ratingData.group,
                        stringResource(R.string.summary_title) to ratingData.summary,
                        stringResource(R.string.rating_group_title) to ratingData.ratingGroup,
                        stringResource(R.string.rating_flow_title) to ratingData.ratingFlow,
                        stringResource(R.string.colloquium_title) to ratingData.colloquium,
                        stringResource(R.string.cgt_cw_title) to ratingData.cgtCw,
                        stringResource(R.string.lw_title) to ratingData.lw,
                        stringResource(R.string.it_title) to ratingData.it,
                        stringResource(R.string.essay_title) to ratingData.essay,
                        stringResource(R.string.nirs_title) to ratingData.nirs,
                        stringResource(R.string.sum_practice_title) to ratingData.sumPractice,
                        stringResource(R.string.omissions_title) to ratingData.omissions,
                    ).map { (naming, data) ->
                        val dataString = data?.let {
                            when (data) {
                                is Float -> "%.2f".format(data)
                                else -> data.toString()
                            }
                        } ?: stringResource(R.string.no_data_label)

                        naming to dataString
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
                            val values = ratingData.practicalLessons.map { (notAttend, tasks) ->
                                buildString {
                                    append(tasks ?: stringResource(R.string.no_data_label))
                                    if (notAttend) {
                                        append(' ')
                                        append(stringResource(R.string.was_not_in_class_label))
                                    }
                                }
                            }

                            DataCategory(
                                modifier = Modifier
                                    .padding(top = 6.dp),
                                isOpened = isPracticalLessonsCategoryOpened,
                                name = stringResource(R.string.practical_lessons_title),
                                categoryValues = values,
                            )
                        }

                        item {
                            DataCategory(
                                modifier = Modifier
                                    .padding(top = 6.dp),
                                isOpened = isCgtsCategoryOpened,
                                name = stringResource(R.string.cgt_title),
                                categoryValues = ratingData.cgts.map {
                                    it?.toString() ?: stringResource(R.string.no_data_label)
                                },
                            )
                        }
                    }
                }

                DataState.NoLoadedData -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = stringResource(R.string.student_not_found_label))
                    }
                }

                DataState.CantAccessServer -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = stringResource(R.string.cant_access_server_label))
                    }
                }
            }
        }
    }
}