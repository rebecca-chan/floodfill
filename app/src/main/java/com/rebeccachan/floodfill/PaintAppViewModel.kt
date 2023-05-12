package com.rebeccachan.floodfill

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*

class PaintAppViewModel : ViewModel() {

    val rows = 10
    val cols = 10

    private val _state = MutableStateFlow(PaintAppState())
    val state: StateFlow<PaintAppState> = _state
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            PaintAppState()
        )


    data class PaintAppState(
        val selectedColor: Color = Color.Black,
        val cellColors: List<Color> = List(100) { Color.White },
        val floodFillEnabled: Boolean = false
        )

    fun selectColor(color: Color) {
        _state.update {
            it.copy(selectedColor = color)
        }
    }

     fun colorCell(index: Int) {
        val row = index / cols
        val col = index % cols
        val currentColor = state.value.cellColors[index]
        if(state.value.floodFillEnabled) {
                floodFill(row = row, col = col, currentColor = currentColor)
        } else {
            _state.update {
                it.copy(cellColors = it.cellColors.mapIndexed { i, color ->
                    if (i == index) it.selectedColor else color
                })
            }
        }
    }

    fun toggleFloodFill() {
        _state.update {
            it.copy(floodFillEnabled = !it.floodFillEnabled)
        }
    }

    // Flood fill algorithm
    private fun floodFill(row: Int, col: Int, currentColor: Color) {

        if (row < 0 || row >= rows || col < 0 || col >= cols) return
        val index = row * cols + col
        if (state.value.cellColors[index] != currentColor) return

        _state.update {
            it.copy(cellColors = it.cellColors.mapIndexed {
               i, color -> if (i == index) it.selectedColor else color
            })
        }

        floodFill(row - 1, col, currentColor) // North
        floodFill(row, col - 1, currentColor) // West
        floodFill(row, col + 1, currentColor) // East
        floodFill(row + 1, col, currentColor) // South
    }


}