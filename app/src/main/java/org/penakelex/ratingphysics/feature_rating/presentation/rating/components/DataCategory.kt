package org.penakelex.ratingphysics.feature_rating.presentation.rating.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DataCategory(
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