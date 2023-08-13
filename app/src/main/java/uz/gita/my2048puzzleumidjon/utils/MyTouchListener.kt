package uz.gita.my2048puzzleumidjon.utils

import android.annotation.SuppressLint
import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import uz.gita.my2048puzzleumidjon.model.SideEnum

class MyTouchListener(private val context: Context) : View.OnTouchListener {
    private val myGestureDetector = GestureDetector(context, MyGestureListener())
    private var mySideMovementListener: ((SideEnum) -> Unit)? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        myGestureDetector.onTouchEvent(event)
        return true
    }

    inner class MyGestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            val deltaX = kotlin.math.abs(e2.x - e1.x)
            val deltaY = kotlin.math.abs(e1.y - e2.y)

            if (deltaX > 100 || deltaY > 100) {
                if (deltaX < deltaY) { // vertical
                    if (e2.y > e1.y) { // down
                        mySideMovementListener?.invoke(SideEnum.DOWN)
                    } else { // up
                        mySideMovementListener?.invoke(SideEnum.UP)
                    }
                } else { // horizontal
                    if (e2.x > e1.x) { // right
                        mySideMovementListener?.invoke(SideEnum.RIGHT)
                    } else { // left
                        mySideMovementListener?.invoke(SideEnum.LEFT)
                    }
                }
            }
            return super.onFling(e1, e2, velocityX, velocityY)

        }
    }

    fun setMySideMovementListener(action: (SideEnum) -> Unit) {
        mySideMovementListener = action
    }

}

