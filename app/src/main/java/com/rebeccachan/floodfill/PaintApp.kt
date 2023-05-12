package com.rebeccachan.floodfill

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun PaintApp(viewModel: PaintAppViewModel = viewModel()) {
    val state by viewModel.state.collectAsState()

    Column {
        Row {
            ColorPreview(state.selectedColor)
            Spacer(modifier = Modifier.weight(1.0f))
            FloodFillStatus(state.floodFillEnabled, viewModel::toggleFloodFill)
        }
        ColorChoices(selectColor = viewModel::selectColor)
        ColorGrid(cells = state.cellColors, viewModel::colorCell)
    }
}

@Composable
fun ColorPreview(color: Color) {
    Text("Selected Color")
    Cell(color = color, onClick = {})
}

@Composable
fun Cell(color: Color, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(40.dp, 40.dp)
            .background(color)
            .border(width = 1.dp, color = Color.Black)
            .clickable(onClick = onClick)
    )
}

@Composable
fun FloodFillStatus(floodFillEnabled: Boolean, toggleFloodFill: () -> Unit) {
    TextButton(
        onClick = toggleFloodFill,
        colors = ButtonDefaults.textButtonColors(
            backgroundColor = if (floodFillEnabled) Color.Gray else Color.Blue
        ),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(4.dp)
    ) {
        Text("Flood Fill", color = if (floodFillEnabled) Color.Black else Color.White)
    }
}


@Composable
fun ColorChoices(selectColor: (Color) -> Unit) {
    val colorChoices =
        listOf(
            Color.Black,
            Color.Red,
            Color.Blue,
            Color.Green,
            Color.Yellow,
            Color.Cyan,
            Color.Magenta,
            Color.LightGray,
            Color.White
        )

    Row {
        colorChoices.forEach { color ->
            Button(
                modifier = Modifier
                    .padding(8.dp)
                    .width(width = 20.dp),
                onClick = { selectColor(color) },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = color
                )
            ) {
                Text("")
            }
        }
    }
}

@Composable
fun ColorGrid(cells: List<Color>, colorCell: (Int) -> Unit) {
    LazyVerticalGrid(columns = GridCells.Fixed(10)) {
        itemsIndexed(cells) { index, color ->
            Cell(color = color) {
                colorCell(index)
            }
        }
    }
}