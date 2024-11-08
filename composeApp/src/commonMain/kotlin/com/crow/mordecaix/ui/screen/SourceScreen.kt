package com.crow.mordecaix.ui.screen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Create
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.crow.mordecaix.model.exposed.SourceModel
import com.crow.mordecaix.ui.component.RippleRoundedOutlineBox
import com.crow.mordecaix.ui.theme.MordecaiXTheme
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.haze
import dev.chrisbanes.haze.hazeChild
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.mp.KoinPlatform.getKoin


@OptIn(ExperimentalResourceApi::class, ExperimentalHazeMaterialsApi::class)
@Composable
fun SourceScreen() {
    var hazeRadius by remember { mutableStateOf(0.dp) }
    val sources = getKoin().get<List<SourceModel>>()
    val color = MaterialTheme.colorScheme.primary
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(sources.size) {
            val hazeState = remember { HazeState() }
            RippleRoundedOutlineBox(
                modifierBefore = Modifier.padding(10.dp),
                modifierAfter = Modifier
                    .fillMaxSize()
                    .border(1.dp, Color.LightGray.copy(alpha = 0.5f), RoundedCornerShape(7.5.dp))
                    .shadow(1.dp, RoundedCornerShape(7.5.dp)),
                outlineWidth = 2f,
                onClick = {
                    /*hazeRadius = if (hazeRadius.value < 2) {
                        (hazeRadius.value + 0.2).dp
                    } else {
                        0.dp
                    }*/
                },
            ) {
                Row(modifier = Modifier.haze(state = hazeState)) {
                    Icon(
                        imageVector = Icons.Rounded.Create,
                        contentDescription = null,
                        modifier = Modifier.padding(10.dp)
                    )
                    Text(
                        text = sources[it].mName,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                        modifier = Modifier.padding(10.dp)

                    )
                }
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .hazeChild(state = hazeState) {
                            backgroundColor = color
                            tints = listOf(HazeTint.Color(Color.White.copy(alpha = 0.1f)))
                            blurRadius = hazeRadius
                            noiseFactor = 0f
                        }
                )
            }
        }
    }
}

@Composable
@Preview
fun RippleBoxExample() {
    val items = listOf(1,2,3)
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(items.size) {
            RippleRoundedOutlineBox(
                modifierBefore = Modifier.padding(10.dp),
                modifierAfter = Modifier
                    .fillMaxSize()
                    .border(1.dp, Color.LightGray.copy(alpha = 0.5f), RoundedCornerShape(7.5.dp))
                    .shadow(1.dp, RoundedCornerShape(7.5.dp)),
                outlineWidth = 2f,
                onClick = { },
            ) {
                Row {
                    Icon(
                        imageVector = Icons.Rounded.Create,
                        contentDescription = null,
                        modifier = Modifier.padding(10.dp)
                    )
                    Text(
                        text = items[it].toString(),
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                        modifier = Modifier.padding(10.dp)

                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun SourceScreenPreview() {
    MordecaiXTheme {
        SourceScreen()
    }
}