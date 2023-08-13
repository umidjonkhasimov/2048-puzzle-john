package uz.gita.my2048puzzleumidjon.repository

import android.util.Log
import uz.gita.my2048puzzleumidjon.utils.MySharedPref
import kotlin.random.Random

class AppRepository private constructor() {
    private val NEW_ELEMENT = 2
    private val mySharedPref = MySharedPref.getInstance()
    var isGameOverBool = false

    companion object {
        private lateinit var instance: AppRepository

        fun getInstance(): AppRepository {
            if (!(Companion::instance.isInitialized))
                instance = AppRepository()
            return instance
        }
    }

    private var gridMatrix = arrayOf(
        arrayOf(0, 0, 0, 0),
        arrayOf(0, 0, 0, 0),
        arrayOf(0, 0, 0, 0),
        arrayOf(0, 0, 0, 0)
    )
    private lateinit var prevGridMatrix: Array<Array<Int>>
    var score = 0
    var prevScore = 0
    var highestScore = 0

    init {
        restoreInstanceState()
    }

    private fun restoreInstanceState() {
        if (mySharedPref.restoreGameState() != null) {
            score = mySharedPref.restoreCurrentScore()
            highestScore = mySharedPref.restoreHighestScore()

            val matrix = mySharedPref.restoreGameState()!!.split("#") as ArrayList
            Log.d("GGG", matrix.toString())
            matrix.removeLast()
            for (i in matrix.indices) {
                gridMatrix[i / 4][i % 4] = matrix[i].toInt()
            }
        } else {
            newGame()
        }

        prevGridMatrix = gridMatrix.map {
            it.clone()
        }.toTypedArray()
    }

    private fun addNewElement() {
        val emptyList = ArrayList<Pair<Int, Int>>()
        for (i in gridMatrix.indices) {
            for (j in gridMatrix[i].indices) {
                if (gridMatrix[i][j] == 0)
                    emptyList.add(Pair(i, j))
            }
        }
        val randomPosition = Random.nextInt(0, emptyList.size)
        gridMatrix[emptyList[randomPosition].first][emptyList[randomPosition].second] = NEW_ELEMENT
    }

    fun moveLeft() {
        prevGridMatrix = gridMatrix.map {
            it.clone()
        }.toTypedArray()
        var isMatrixChanged = false
        var amountsToBeAdded = 0

        for (i in gridMatrix.indices) {
            val currRow = gridMatrix[i].clone()
            val amounts = ArrayList<Int>(4)
            var bool = true
            for (j in gridMatrix[i].indices) {
                if (gridMatrix[i][j] == 0) continue
                if (amounts.isEmpty()) {
                    amounts.add(gridMatrix[i][j])
                } else {
                    if (amounts.last() == gridMatrix[i][j] && bool) {
                        amounts[amounts.size - 1] = amounts.last() * 2
                        amountsToBeAdded += amounts.last()
                        bool = false
                    } else {
                        amounts.add(gridMatrix[i][j])
                        bool = true
                    }
                }
                gridMatrix[i][j] = 0
            }
            for (k in amounts.indices) {
                if (currRow[k] != amounts[k])
                    isMatrixChanged = true
                gridMatrix[i][k] = amounts[k]
            }
        }
        setGameScore(amountsToBeAdded)
        if (isMatrixChanged)
            if (!isGameOver())
                addNewElement()
    }

    fun moveRight() {
        prevGridMatrix = gridMatrix.map {
            it.clone()
        }.toTypedArray()
        var isMatrixChanged = false
        var amountToBeAdded = 0
        for (i in gridMatrix.indices) {
            val currRow = gridMatrix[i].clone()
            val amounts = ArrayList<Int>(4)
            var bool = true
            for (j in gridMatrix[i].size - 1 downTo 0) {
                if (gridMatrix[i][j] == 0) continue
                if (amounts.isEmpty()) {
                    amounts.add(gridMatrix[i][j])
                } else {
                    if (amounts.last() == gridMatrix[i][j] && bool) {
                        amounts[amounts.size - 1] = amounts.last() * 2
                        amountToBeAdded += amounts.last()
                        bool = false
                    } else {
                        amounts.add(gridMatrix[i][j])
                        bool = true
                    }
                }
                gridMatrix[i][j] = 0
            }
            for (k in amounts.indices) {
                if (amounts[k] != currRow[currRow.size - k - 1])
                    isMatrixChanged = true
                gridMatrix[i][gridMatrix[i].size - k - 1] = amounts[k]
            }
        }
        setGameScore(amountToBeAdded)
        if (isMatrixChanged)
            if (!isGameOver())
                addNewElement()
    }

    fun moveUp() {
        prevGridMatrix = gridMatrix.map {
            it.clone()
        }.toTypedArray()
        var isMatrixChanged = false
        var amountToBeAdded = 0
        for (i in 0 until gridMatrix[0].size) {
            val currCol = ArrayList<Int>()
            val amounts = ArrayList<Int>(4)
            var bool = true
            for (j in gridMatrix.indices) {
                currCol.add(gridMatrix[j][i])
                if (gridMatrix[j][i] == 0) continue
                if (amounts.isEmpty()) {
                    amounts.add(gridMatrix[j][i])
                } else {
                    if (amounts.last() == gridMatrix[j][i] && bool) {
                        amounts[amounts.size - 1] = amounts.last() * 2
                        amountToBeAdded += amounts.last()
                        bool = false
                    } else {
                        amounts.add(gridMatrix[j][i])
                        bool = true
                    }
                }
                gridMatrix[j][i] = 0
            }
            for (k in amounts.indices) {
                if (currCol[k] != amounts[k])
                    isMatrixChanged = true
                gridMatrix[k][i] = amounts[k]
                Log.d("GGG", "${currCol[k]} ${amounts[k]}")
            }
        }
        setGameScore(amountToBeAdded)
        if (isMatrixChanged)
            if (!isGameOver())
                addNewElement()
    }

    fun moveDown() {
        prevGridMatrix = gridMatrix.map {
            it.clone()
        }.toTypedArray()
        var isMatrixChanged = false
        var amountToBeAdded = 0
        for (i in 0 until gridMatrix[0].size) {
            val currCol = ArrayList<Int>()
            val amounts = ArrayList<Int>(4)
            var bool = true
            for (j in gridMatrix.size - 1 downTo 0) {
                currCol.add(gridMatrix[j][i])
                if (gridMatrix[j][i] == 0) continue
                if (amounts.isEmpty()) {
                    amounts.add(gridMatrix[j][i])
                } else {
                    if (amounts.last() == gridMatrix[j][i] && bool) {
                        amounts[amounts.size - 1] = amounts.last() * 2
                        amountToBeAdded += amounts.last()
                        bool = false
                    } else {
                        amounts.add(gridMatrix[j][i])
                        bool = true
                    }
                }
                gridMatrix[j][i] = 0
            }
            for (k in amounts.indices) {
                if (currCol[k] != amounts[k])
                    isMatrixChanged = true
                gridMatrix[gridMatrix.size - k - 1][i] = amounts[k]
            }
        }
        setGameScore(amountToBeAdded)
        if (isMatrixChanged)
            if (!isGameOver())
                addNewElement()
    }

    private fun setGameScore(last: Int) {
        prevScore = score
        score += last
        setHighestScore()
    }

    private fun setHighestScore() {
        if (score > highestScore)
            highestScore = score
    }

    fun isGameOver(): Boolean {
        for (i in gridMatrix.indices) {
            for (j in gridMatrix[i].indices) {
                val currElem = gridMatrix[i][j]
                if (currElem == 0)
                    return false
                if (j == gridMatrix[i].size - 1) {
                    if (i != gridMatrix.size - 1)
                        if (currElem == gridMatrix[i + 1][j])
                            return false
                } else if (i == gridMatrix.size - 1) {
                    if (currElem == gridMatrix[i][j + 1]) {
                        return false
                    }
                } else {
                    if (currElem == gridMatrix[i][j + 1] || currElem == gridMatrix[i + 1][j])
                        return false
                }
            }
        }
        return true
    }

    fun newGame() {
        for (i in gridMatrix.indices) {
            for (j in gridMatrix[i].indices) {
                gridMatrix[i][j] = 0
            }
        }
        score = 0
        mySharedPref.nullify()
        addNewElement()
        addNewElement()
    }

    fun saveInstanceState() {
        val instanceState = java.lang.StringBuilder()
        for (i in gridMatrix.indices) {
            for (j in gridMatrix[i].indices) {
                instanceState.append(gridMatrix[i][j].toString()).append("#")
            }
        }
        mySharedPref.saveCurrentScore(score)
        mySharedPref.saveHighestScore(highestScore)
        mySharedPref.saveGameState(instanceState)
    }

    fun getGridMatrix(): Array<Array<Int>> {
        return gridMatrix.clone()
    }

    fun getPrevGridMatrix(): Array<Array<Int>> {
        return prevGridMatrix
    }

    fun setPrevState() {
        gridMatrix = prevGridMatrix.map {
            it.clone()
        }.toTypedArray()
        score = prevScore
    }
}