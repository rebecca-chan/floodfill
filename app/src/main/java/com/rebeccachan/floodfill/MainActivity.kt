package com.rebeccachan.floodfill

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.rebeccachan.floodfill.ui.theme.FloodFillTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FloodFillTheme {
                PaintApp()
            }
        }
    }
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
fun PaintApp() {
    var selectedColor by remember { mutableStateOf(Color.White) }
    var floodFillEnabled by remember { mutableStateOf(false) }
    val defaultColor = Color.White
    val colorChoices = listOf(Color.Black, Color.Red, Color.Blue, Color.Green, Color.Yellow)
    val cells = remember { mutableStateListOf<Color>() }
    val rows = 10
    val cols = 10

    // Initialize the cells list
    repeat(rows * cols) {
        cells.add(defaultColor)
    }

    // Flood fill algorithm
    fun floodFill(row: Int, col: Int, targetColor: Color, replacementColor: Color) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) return
        val index = row * cols + col
        if (cells[index] != targetColor) return

        cells[index] = replacementColor

        floodFill(row - 1, col, targetColor, replacementColor) // North
        floodFill(row, col - 1, targetColor, replacementColor) // West
        floodFill(row, col + 1, targetColor, replacementColor) // East
        floodFill(row + 1, col, targetColor, replacementColor) // South
    }

    Column {
        Row {
            Text("Selected Color")
            Cell(color = selectedColor, onClick = {})
            TextButton(
                onClick = { floodFillEnabled = !floodFillEnabled },
                colors = ButtonDefaults.textButtonColors(
                    backgroundColor = if (floodFillEnabled) Color.Gray else Color.Blue
                ),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text("Flood Fill", color = if (floodFillEnabled) Color.Black else Color.White)
            }
        }
        Row {
            colorChoices.forEach { color ->
                Button(
                    modifier = Modifier
                        .padding(8.dp),
                    onClick = { selectedColor = color },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = color
                    )
                ) {
                    Text("")
                }
            }
        }

        LazyVerticalGrid(columns = GridCells.Fixed(cols)) {
            itemsIndexed(cells) { index, color ->
                Cell(color = color) {
                    // Get the row and column number of the clicked cell
                    val row = index / cols
                    val col = index % cols

                    // Call the flood fill function with the clicked cell's color
                    if (floodFillEnabled) {
                        floodFill(row, col, cells[index], selectedColor)
                    } else {
                        cells[index] = selectedColor
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PaintApp()
}
