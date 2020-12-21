package com.dopedevx.congratsshowerlibrary

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import java.util.*

class CongratsShower(var parent: ViewGroup, var context: Context) {
    var paperHeight = 0f
    var paperWidth = 0f
    var drawable = ContextCompat.getDrawable(context, R.drawable.paper_piece)!!

    init {
        paperHeight = convertDpToPixel(drawable.intrinsicHeight.toFloat(), context)
        paperWidth = convertDpToPixel(drawable.intrinsicWidth.toFloat(), context)
    }

    var showerTaskHandler = Handler(Looper.getMainLooper())
    var startShowerRunnable = object : Runnable {
        override fun run() {
            performShower()
            showerTaskHandler.postDelayed(this, 50)
        }
    }
    var stopShowerRunnable: Runnable = Runnable { stopShower() }


    private fun performShower() {
        // changing the flag to true so that animation resumes when the activity is resumed
//        shouldResume = true
        val screenWidth = parent.width.toFloat()
        val screenHeight = parent.height.toFloat()

        val paperPiece: ImageView = AppCompatImageView(context)
        paperPiece.setImageDrawable(drawable)
        paperPiece.elevation = 50F// elevating the paper pieces so that they appear over the dialog.

        // array of colors that the paper pieces can have
        val colorArray = arrayOf("#ffb49b", "#ffa990", "#ffc5ab", "#ffa38a")
        // setting random color to the paper piece
        if (drawable == ContextCompat.getDrawable(context, R.drawable.paper_piece)) {
            paperPiece.setColorFilter(Color.parseColor(colorArray[(Math.random() * (3 + 1) + 0).toInt()]))
        }
        val random = Random()
        // object animator for moving the paper from up to down
        val upToDown = ObjectAnimator.ofFloat(
            paperPiece, View.TRANSLATION_Y,
            -paperHeight - 40, screenHeight + paperHeight
        )
        // object animator for moving the paper piece from left to right
        val leftToRight = ObjectAnimator.ofFloat(
            paperPiece, View.TRANSLATION_X,
            Math.random()
                .toFloat() * (screenWidth + 300 - (-screenWidth + 300) + 1) + (-screenWidth + 300),
            random.nextInt((screenWidth - paperWidth).toInt()).toFloat()
        )
        // object animator for rotating the paper about the x-axis
        val rotatorX =
            ObjectAnimator.ofFloat(paperPiece, View.ROTATION_X, 0f, random.nextFloat() * 1500)
        // object animator for rotating the paper piece about the y-axis
        val rotatorY =
            ObjectAnimator.ofFloat(paperPiece, View.ROTATION_Y, 0f, random.nextFloat() * 1500)
        // object animator for rotating the paper about the z-axis
        val rotator =
            ObjectAnimator.ofFloat(paperPiece, View.ROTATION, 0f, random.nextFloat() * 360)
        parent.addView(paperPiece)
        val setOfShowerAnimation = AnimatorSet()
        setOfShowerAnimation.interpolator = AccelerateInterpolator(1.5f)
        setOfShowerAnimation.playTogether(upToDown, rotator, rotatorX, rotatorY, leftToRight)
        val duration = (Math.random() * (6 - 4 + 1) + 4).toInt()
        setOfShowerAnimation.duration = 1000 * duration.toLong()
        setOfShowerAnimation.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                // removing the paperPiece when the animation ends to preserve resources
                parent.removeView(paperPiece)
            }
        })
        setOfShowerAnimation.start()
    }

    fun startShower() {
        showerTaskHandler.post(startShowerRunnable)
    }

    fun startShower(runningTime: Long) {
        showerTaskHandler.post(startShowerRunnable)
        showerTaskHandler.postDelayed(stopShowerRunnable, runningTime)
    }

    fun stopShower() {
        showerTaskHandler.removeCallbacks(startShowerRunnable)
    }

    companion object {
        /**
         * This method converts dp to pixels according to the device's display density.
         *
         * @param dp      A value in dp which we need to convert into pixels
         * @param context Context to get resources and device specific display metrics
         * @return A float value to represent px equivalent to dp depending on device density
         */
        fun convertDpToPixel(dp: Float, context: Context): Float {
            return dp * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
        }
    }
}