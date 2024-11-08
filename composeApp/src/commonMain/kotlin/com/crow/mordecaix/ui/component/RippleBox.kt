package com.crow.mordecaix.ui.component

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp

/**
* RippleBox提供点击波纹动画
*
* @author:crowforkotlin
* @time:2024-10-12 17:27:40 下午 星期六
*/

@Composable
private fun RippleBox(
    selected: Boolean = false,
    isSelectEnable: Boolean = false,
    modifierBefore: Modifier = Modifier,
    modifierAfter: Modifier = Modifier,
    outlineAnimationSpec: AnimationSpec<Float> = tween(durationMillis = 200, easing = LinearEasing),
    innerAnimationSpec: AnimationSpec<Float> = tween(
        durationMillis = 200,
        easing = LinearOutSlowInEasing
    ),
    onDrawOutline: DrawScope.(outlineValue: Float, clickValue: Float) -> Unit = { outlineValue, clickValue ->
        drawRect(
            color = Color.Black.copy(1f * outlineValue),
            style = Stroke(width = 2f)
        )
    },
    onDrawInner: DrawScope.(outlineValue: Float, clickValue: Float) -> Unit = { outlineValue, clickValue ->
        drawRect(
            color = Color.LightGray.copy(0.6f * clickValue),
        )
    },
    onEnter: () -> Unit = {},
    onClick: () -> Unit = {},
    content: @Composable BoxScope.() -> Unit
) {
    var isPress by remember { mutableStateOf(false) }
    var isEnter by remember { mutableStateOf(false) }
    val animationEnter by animateFloatAsState(
        targetValue = if (isEnter || (isSelectEnable && selected)) 1f else 0f,
        animationSpec = outlineAnimationSpec
    )
    val animationPress by animateFloatAsState(
        targetValue = if (isPress) 1f else 0f,
        animationSpec = innerAnimationSpec
    )
    Box(
        content = content,
        modifier = modifierBefore
            .drawWithContent {
                // (abs(clickOffset.x) - abs(halfWidth)) * animationPress.value
                onDrawOutline(this, animationEnter, animationPress)
                drawContent()
                onDrawInner(this, animationEnter, animationPress)
            }
            .pointerInput(Unit) {
                awaitEachGesture {
                    awaitFirstDown().also { it.consume() }
                    isPress = true
                    val up = waitForUpOrCancellation()
                    if (up != null) {
                        up.consume()
                        isPress = false
                        onClick()
                    } else {
                        isPress = false
                    }
                }
            }
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        when (event.type) {
                            PointerEventType.Enter -> {
                                isEnter = true
                                onEnter()
                            }

                            PointerEventType.Exit -> {
                                isEnter = false
                            }

                            PointerEventType.Release -> {
                                isEnter = false
                            }
                        }
                    }
                }
            }
            .then(modifierAfter)
    )
}

@Composable
fun RippleRoundedOutlineBox(
    modifierBefore: Modifier = Modifier,
    modifierAfter: Modifier = Modifier,
    selected: Boolean = false,
    isSelectEnable: Boolean = false,
    innerColor: Color = Color.LightGray.copy(alpha = 0.6f),
    outlineColor: Color = Color.Black,
    outlineWidth: Float = 4f,
    cornerRadius: CornerRadius = CornerRadius(7.5.dp.value),
    outlineAnimationSpec: AnimationSpec<Float> = tween(durationMillis = 200, easing = LinearEasing),
    innerAnimationSpec: AnimationSpec<Float> = tween(
        durationMillis = 200,
        easing = LinearOutSlowInEasing
    ),
    onEnter: () -> Unit = {},
    onClick: () -> Unit = {},
    content: @Composable BoxScope.() -> Unit
) {
    RippleBox(
        selected = selected,
        isSelectEnable = isSelectEnable,
        modifierBefore = modifierBefore,
        modifierAfter = modifierAfter,
        outlineAnimationSpec = outlineAnimationSpec,
        innerAnimationSpec = innerAnimationSpec,
        onEnter = onEnter,
        onClick = onClick,
        content = content,
        onDrawOutline = { outlineValue, clickValue ->
            drawRoundRect(
                color = outlineColor.copy(outlineColor.alpha * outlineValue),
                style = Stroke(outlineWidth, cap = StrokeCap.Round, join = StrokeJoin.Round),
                cornerRadius = cornerRadius
            )
        },
        onDrawInner = { outlineValue, clickValue ->
            drawRoundRect(
                color = innerColor.copy(innerColor.alpha * clickValue),
                cornerRadius = cornerRadius
            )
        }
    )
}

@Composable
fun RippleOutlineBox(
    modifierBefore: Modifier = Modifier,
    modifierAfter: Modifier = Modifier,
    innerColor: Color = Color.LightGray.copy(alpha = 0.6f),
    outlineColor: Color = Color.Black,
    outlineWidth: Float = 4f,
    outlineAnimationSpec: AnimationSpec<Float> = tween(durationMillis = 200, easing = LinearEasing),
    innerAnimationSpec: AnimationSpec<Float> = tween(
        durationMillis = 200,
        easing = LinearOutSlowInEasing
    ),
    onEnter: () -> Unit = {},
    onClick: () -> Unit = {},
    content: @Composable BoxScope.() -> Unit
) {
    RippleBox(
        modifierBefore = modifierBefore,
        modifierAfter = modifierAfter,
        outlineAnimationSpec = outlineAnimationSpec,
        innerAnimationSpec = innerAnimationSpec,
        onEnter = onEnter,
        onClick = onClick,
        content = content,
        onDrawOutline = { outlineValue, clickValue ->
            drawRect(
                color = outlineColor.copy(outlineColor.alpha * outlineValue),
                style = Stroke(outlineWidth, cap = StrokeCap.Round, join = StrokeJoin.Round),
            )
        },
        onDrawInner = { outlineValue, clickValue ->
            drawRect(
                color = innerColor.copy(innerColor.alpha * clickValue),
            )
        }
    )
}

@Composable
fun RippleRoundedFillBox(
    selected: Boolean = false,
    isSelectEnable: Boolean = false,
    modifierBefore: Modifier = Modifier,
    modifierAfter: Modifier = Modifier,
    innerColor: Color = Color.LightGray.copy(alpha = 0.6f),
    outlineColor: Color = Color.LightGray.copy(alpha = 0.3f),
    cornerRadius: CornerRadius = CornerRadius(7.5.dp.value),
    outlineAnimationSpec: AnimationSpec<Float> = tween(durationMillis = 200, easing = LinearEasing),
    innerAnimationSpec: AnimationSpec<Float> = tween(
        durationMillis = 200,
        easing = LinearOutSlowInEasing
    ),
    onEnter: () -> Unit = {},
    onClick: () -> Unit = {},
    content: @Composable BoxScope.() -> Unit
) {

    RippleBox(
        selected = selected,
        isSelectEnable = isSelectEnable,
        modifierBefore = modifierBefore,
        modifierAfter = modifierAfter,
        outlineAnimationSpec = outlineAnimationSpec,
        innerAnimationSpec = innerAnimationSpec,
        onEnter = onEnter,
        onClick = onClick,
        content = content,
        onDrawOutline = { outlineValue, clickValue ->
            drawRoundRect(
                color = outlineColor.copy(outlineColor.alpha * outlineValue),
                cornerRadius = cornerRadius
            )
        },
        onDrawInner = { outlineValue, clickValue ->
            drawRoundRect(
                color = innerColor.copy(innerColor.alpha * clickValue),
                cornerRadius = cornerRadius
            )
        }
    )
}

@Composable
fun RippleFilledBox(
    modifierBefore: Modifier = Modifier,
    modifierAfter: Modifier = Modifier,
    innerColor: Color = Color.LightGray.copy(alpha = 0.6f),
    outlineColor: Color = Color.LightGray.copy(alpha = 0.3f),
    outlineAnimationSpec: AnimationSpec<Float> = tween(durationMillis = 200, easing = LinearEasing),
    innerAnimationSpec: AnimationSpec<Float> = tween(
        durationMillis = 200,
        easing = LinearOutSlowInEasing
    ),
    modifier: Modifier = Modifier,
    onEnter: () -> Unit = {},
    onClick: () -> Unit = {},
    content: @Composable BoxScope.() -> Unit
) {
    RippleBox(
        modifierBefore = modifierBefore,
        modifierAfter = modifierAfter,
        outlineAnimationSpec = outlineAnimationSpec,
        innerAnimationSpec = innerAnimationSpec,
        onEnter = onEnter,
        onClick = onClick,
        content = content,
        onDrawOutline = { outlineValue, clickValue ->
            drawRoundRect(
                color = outlineColor.copy(outlineColor.alpha * outlineValue),
            )
        },
        onDrawInner = { outlineValue, clickValue ->
            drawRoundRect(
                color = innerColor.copy(innerColor.alpha * clickValue),
            )
        }
    )
}