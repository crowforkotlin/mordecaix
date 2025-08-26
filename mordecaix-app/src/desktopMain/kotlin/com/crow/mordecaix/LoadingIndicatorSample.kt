/*
package com.crow.mordecaix

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max

@Composable
fun BoxScope.LoadingIndicatorSample(
    modifier: Modifier,
    state: MySwipeRefreshState,
    indicatorHeight: Dp
) {
    val height = max(30.dp, with(LocalDensity.current) {
        state.progress.offset.toDp()
    })
    Box(
        modifier
            .fillMaxWidth()
            .height(height), contentAlignment = Alignment.Center
    ) {
        if (state.isSwipeInProgress) {
            if (state.progress.offset <= with(LocalDensity.current) { indicatorHeight.toPx() }) {
                Text(text = if (state.progress.location == TOP) "下拉刷新" else "上拉加载更多")
            } else {
                Text(text = if (state.progress.location == TOP) "松开刷新" else "松开加载更多")
            }
        } else {
            AnimatedVisibility(state.loadState == REFRESHING || state.loadState == LOADING_MORE) {
                //加载中
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun BoxScope.LoadingIndicator(
    modifier: Modifier,
    state: MySwipeRefreshState,
    indicatorHeight: Dp
) {
    val density = LocalDensity.current
    //N个矩形条的宽度
    val iconWidth = 35.dp
    //canvas高度
    val canvasHeight = max(30.dp, with(density) {
        state.progress.offset.toDp()
    })
    //矩形条的颜色
    val contentColor = MaterialTheme.colorScheme.surface

    //每个矩形的高度
    val rectHeightArray = remember {
        arrayOf(12.dp, 16.dp, 20.dp, 16.dp, 12.dp).map { with(density) { it.toPx() } }
            .toFloatArray()
    }
    //每个item的宽度
    val itemWidth = with(density) { (iconWidth / rectHeightArray.size).toPx() }
    //矩形条的宽度
    val rectWidth = itemWidth / 2

    if (state.isSwipeInProgress) {
        Canvas(
            modifier = modifier
                .fillMaxWidth()
                .height(canvasHeight)
        ) {
            //让N个圆角矩形横向居中
            val paddingStart = (size.width - iconWidth.toPx()) / 2
            //开始动画的偏移距离
            val startAnimOffset = 20.dp.toPx()
            //@FloatRange(from = 0.0, to = 1.0) 矩形高度的percent百分比
            val realFraction =
                ((state.progress.offset - startAnimOffset).coerceAtLeast(0f) / (indicatorHeight.toPx() - startAnimOffset)).coerceAtMost(
                    1f
                )
            //N个矩形条拼接到一起的高度，并且跟随realFraction变化
            val visibleRectHeight = rectHeightArray.sum() * realFraction
            rectHeightArray.forEachIndexed { index, maxLineHeight ->
                val start = paddingStart + itemWidth * index + (itemWidth - rectWidth)
                val bgTop = (size.height - rectHeightArray[index]) / 2
                //背景
                drawRoundRect(
                    color = contentColor.copy(alpha = 0.25f),
                    topLeft = Offset(x = start, y = bgTop),
                    size = Size(width = rectWidth, height = rectHeightArray[index]),
                    cornerRadius = CornerRadius(rectWidth / 2)
                )
                //单个矩形条的高度，跟随realFraction来变化
                val height =
                    (visibleRectHeight - (rectHeightArray.filterIndexed { i, _ -> i < index }
                        .sum())).coerceAtLeast(0f).coerceAtMost(maxLineHeight)
                val top = (size.height - height) / 2
                drawRoundRect(
                    color = contentColor,
                    topLeft = Offset(x = start, y = top),
                    size = Size(width = rectWidth, height = height),
                    cornerRadius = CornerRadius(rectWidth / 2)
                )
            }
        }
    } else {
        AnimatedVisibility(
            state.loadState != NORMAL,
            modifier = modifier
                .fillMaxWidth()
                .height(canvasHeight),
            enter = fadeIn() + expandVertically(),
            exit = shrinkVertically() + fadeOut(),
        ) {
            val target = 200f
            //无线循环的动画
            val infiniteTransition = rememberInfiniteTransition(label = "")
            val value by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = target,
                animationSpec = infiniteRepeatable(
                    animation = tween(500, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                ), label = ""
            )

            //根据无线循环的动画得到每个矩形高度的percent 百分比
            val percents = arrayOf(
                getOffsetPercent(value, 0f, target),
                getOffsetPercent(value, 30f, target),
                getOffsetPercent(value, 50f, target),
                getOffsetPercent(value, 70f, target),
                getOffsetPercent(value, 100f, target),
            )
            Canvas(
                modifier = modifier
                    .fillMaxWidth()
                    .height(canvasHeight)
            ) {
                val maxHeight = 20.dp.toPx()
                val paddingStart = (size.width - iconWidth.toPx()) / 2
                percents.forEachIndexed { index, percent ->
                    val start = paddingStart + itemWidth * index + (itemWidth - rectWidth)
                    val top = (size.height - maxHeight * percent.coerceAtLeast(0.25f)) / 2
                    drawRoundRect(
                        color = contentColor,
                        topLeft = Offset(x = start, y = top),
                        size = Size(width = rectWidth, height = maxHeight * percent),
                        cornerRadius = CornerRadius(rectWidth / 2)
                    )
                }
            }
        }
    }
}

fun getOffsetPercent(value: Float, offset: Float, spacing: Float, minValue: Float = 0f): Float {
    val toValue = value + offset
    val resValue =
        if (toValue < spacing / 2) {
            toValue
        } else if (toValue < spacing) {
            spacing - toValue
        } else {
            toValue - spacing
        }
    return (resValue / 100f).coerceAtLeast(minValue)
}

*/
