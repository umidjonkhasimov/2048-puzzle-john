package uz.gita.my2048puzzleumidjon

import android.annotation.SuppressLint
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.LinearLayoutCompat
import com.example.my2048puzzle.R
import com.example.my2048puzzle.databinding.ActivityGameBinding
import uz.gita.my2048puzzleumidjon.model.SideEnum
import uz.gita.my2048puzzleumidjon.repository.AppRepository
import uz.gita.my2048puzzleumidjon.utils.BackgroundColorPicker
import uz.gita.my2048puzzleumidjon.utils.MyTouchListener

class GameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameBinding
    private var items: MutableList<TextView> = ArrayList(16)
    private lateinit var prevItems: Array<Array<Int>>
    private val repository = AppRepository.getInstance()
    private val colorPicker = BackgroundColorPicker()
    private lateinit var customDialog: Dialog
    private lateinit var myTouchListener: MyTouchListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadViews()
        drawMatrixToView()
        setListeners()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setListeners() {
        myTouchListener = MyTouchListener(this)
        myTouchListener.setMySideMovementListener {
            when (it) {
                SideEnum.UP -> {
                    repository.moveUp()
                    drawMatrixToView()
                }

                SideEnum.DOWN -> {
                    repository.moveDown()
                    drawMatrixToView()
                }

                SideEnum.LEFT -> {
                    repository.moveLeft()
                    drawMatrixToView()
                }

                SideEnum.RIGHT -> {
                    repository.moveRight()
                    drawMatrixToView()
                }
            }
            binding.tvScore.text = repository.score.toString()
        }
        binding.mainView.setOnTouchListener(myTouchListener)
        binding.btnUndo.setOnClickListener {
            previousState()
        }
        binding.buttonReload.setOnClickListener {
            showRestartDialog()
        }
    }

    private fun showRestartDialog() {
        customDialog = Dialog(this)
        customDialog.apply {
            setContentView(R.layout.dialog_restart)
            window?.setBackgroundDrawable(
                AppCompatResources.getDrawable(
                    this@GameActivity, R.drawable.custom_dialog_background
                )
            )
            window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            setCancelable(false)
            findViewById<AppCompatButton>(R.id.btn_restart).setOnClickListener {
                repository.newGame()
                drawMatrixToView()
                dismiss()
            }
            findViewById<AppCompatButton>(R.id.btn_cancel).setOnClickListener {
                this.dismiss()
            }
        }.show()
    }

    private fun showGameOverDialog() {
        customDialog = Dialog(this)
        customDialog.apply {
            setContentView(R.layout.dialog_game_over)
            window?.setBackgroundDrawable(
                AppCompatResources.getDrawable(
                    this@GameActivity, R.drawable.custom_dialog_background
                )
            )
            window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            setCancelable(false)
            findViewById<AppCompatButton>(R.id.btn_restart_over).setOnClickListener {
                dismiss()
                repository.newGame()
                drawMatrixToView()
            }
            findViewById<AppCompatButton>(R.id.btn_main_over).setOnClickListener {
                finish()
            }
        }.show()
    }

    private fun drawMatrixToView() {
        if (repository.isGameOver()) {
            showGameOverDialog()
        }
        binding.tvScore.text = repository.score.toString()
        binding.tvHighScore.text = repository.highestScore.toString()
        val matrix = repository.getGridMatrix()
        for (i in matrix.indices) {
            for (j in matrix[i].indices) {
                items[i * 4 + j].apply {
                    text = if (matrix[i][j] == 0) ""
                    else matrix[i][j].toString()

                    setBackgroundResource(colorPicker.getColorByValue(matrix[i][j]))
                }
            }
        }
    }

    private fun previousState() {
        prevItems = repository.getPrevGridMatrix()
        binding.tvScore.text = repository.prevScore.toString()
        for (i in prevItems.indices) {
            for (j in prevItems[i].indices) {
                items[i * 4 + j].apply {
                    text = if (prevItems[i][j] == 0) ""
                    else prevItems[i][j].toString()

                    setBackgroundResource(colorPicker.getColorByValue(prevItems[i][j]))
                }
            }
        }
        repository.setPrevState()
    }

    private fun loadViews() {
        for (i in 0 until binding.mainView.childCount) {
            val linear = binding.mainView.getChildAt(i) as LinearLayoutCompat
            for (j in 0 until linear.childCount) {
                items.add(linear.getChildAt(j) as TextView)
            }
        }
    }

    override fun onStop() {
        repository.saveInstanceState()
        super.onStop()
    }
}