/*
package com.crow.mordecaix

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.MutatorMutex
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeProgress
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlin.math.abs
import kotlin.math.min
//常规状态
const val NORMAL = 0

//下拉刷新状态
const val REFRESHING = 1

//上拉加载状态
const val LOADING_MORE = 2

@Stable
class MySwipeRefreshState(
    loadState: Int,
) {
    private val _indicatorOffset = Animatable(0f)
    private val mutatorMutex = MutatorMutex()

    var loadState: Int by mutableIntStateOf(loadState)

    */
/**
     * Whether a swipe/drag is currently in progress.
     *//*

    var isSwipeInProgress: Boolean by mutableStateOf(false)
        internal set

    */
/**
     * 下拉到新页面
     *//*

    var isSkip: Boolean by mutableStateOf(false)
        internal set

    //上下拉的偏移量等等
    @OptIn(ExperimentalMaterialApi::class)
    var progress: SwipeProgress by mutableStateOf(SwipeProgress())
        internal set

    */
/**
     * The current offset for the indicator, in pixels.
     *//*

    internal val indicatorOffset: Float get() = _indicatorOffset.value

    internal suspend fun animateOffsetTo(
        offset: Float,
    ) {
        mutatorMutex.mutate {
            _indicatorOffset.animateTo(offset)
        }
    }

    */
/**
     * Dispatch scroll delta in pixels from touch events.
     *//*

    internal suspend fun dispatchScrollDelta(
        delta: Float,
        location: Int,
        maxOffsetY: Float,
    ) {
        mutatorMutex.mutate(MutatePriority.UserInput) {
            _indicatorOffset.snapTo(_indicatorOffset.value + delta)
            updateProgress(
                location = location,
                maxOffsetY = maxOffsetY,
            )
        }
    }

    */
/**
     * 更新progress
     * @param maxOffsetY  下拉或者上拉indicator最大高度
     *//*

    private fun updateProgress(
        offsetY: Float = abs(indicatorOffset),
        location: Int,
        maxOffsetY: Float,
    ) {
        val offsetPercent = min(1f, offsetY / maxOffsetY)

        val offset = min(maxOffsetY, offsetY)
        progress = SwipeProgress(location, offset, offsetPercent)
    }

}

*/
