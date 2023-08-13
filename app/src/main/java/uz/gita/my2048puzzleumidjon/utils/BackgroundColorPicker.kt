package uz.gita.my2048puzzleumidjon.utils

import com.example.my2048puzzle.R

class BackgroundColorPicker {
    private val backMap = HashMap<Int, Int>()

    init {
        loadMap()
    }

    private fun loadMap() {
        backMap[0] = R.drawable.bg_item_0
        backMap[2] = R.drawable.bg_item_2
        backMap[4] = R.drawable.bg_item_4
        backMap[8] = R.drawable.bg_item_8
        backMap[16] = R.drawable.bg_item_16
        backMap[32] = R.drawable.bg_item_32
        backMap[64] = R.drawable.bg_item_64
        backMap[128] = R.drawable.bg_item_128
        backMap[256] = R.drawable.bg_item_256
        backMap[512] = R.drawable.bg_item_512
        backMap[1024] = R.drawable.bg_item_1024
        backMap[2048] = R.drawable.bg_item_2048
    }

    fun getColorByValue(value: Int): Int {
        return backMap.getOrDefault(value, R.drawable.bg_item_0)
    }
}