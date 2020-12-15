package com.dopedevx.congratsshower

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var congratsButton: Button // field for congrats button as seen on the screen
    private var paperHeight // height of the paper piece
            = 0f
    private var paperWidth // width of the paper piece
            = 0f

    //    the field "shouldResume" is a flag that indicates whether or not the runnable should run on onResume
    private var shouldResume = false
    private lateinit var dialog: View  // reference to the constraintLayout that contains the layout to be shown when congrats button is pressed
    var performShowerHandler = Handler()

    // Runnable that is responsible for animating the paper piece
    var performShowerRunnable: Runnable = object : Runnable {
        override fun run() {
            performShower()
            performShowerHandler.postDelayed(this, 50)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        congratsButton = findViewById(R.id.congrats)
        dialog = findViewById(R.id.dialog)
        congratsButton.setOnClickListener { v: View? ->
            performShowerHandler.post(performShowerRunnable)
            congratsButton.visibility = View.INVISIBLE
            dialog.visibility = View.VISIBLE
        }
        paperHeight = convertDpToPixel(24f, this)
        paperWidth = convertDpToPixel(18f, this)
        // assigning the flag to false so that the animation does not start right away because the activity is newly created
        shouldResume = false
    }

    override fun onPause() {
        super.onPause()
        // stopping the animation to preserve the resources
        performShowerHandler.removeCallbacks(performShowerRunnable)
    }

    override fun onResume() {
        super.onResume()
        if (shouldResume) {
            // restarting the animation when the activity resumes
            performShowerHandler.post(performShowerRunnable)
        }
    }

    private fun performShower() {
        // changing the flag to true so that animation resumes when the activity is resumed
        shouldResume = true
        val parent = congratsButton.parent as ViewGroup
        val screenWidth = parent.width.toFloat()
        val screenHeight = parent.height.toFloat()
        val paperPiece: ImageView = AppCompatImageView(this)
        paperPiece.setImageResource(R.drawable.paper_piece)
        paperPiece.elevation = 50F// elevating the paper pieces so that they appear over the dialog.
        val colorArray = arrayOf("#ffb49b", "#ffa990", "#ffc5ab", "#ffa38a")
        paperPiece.setColorFilter(Color.parseColor(colorArray[(Math.random() * (3 + 1) + 0).toInt()])) // setting random color to the paper piece
        val random = Random()
        // object animator for moving the paper from up to down
        val upToDown = ObjectAnimator.ofFloat(paperPiece, View.TRANSLATION_Y,
                -paperHeight - 40, screenHeight + paperHeight)
        // object animator for moving the paper piece from left to right
        val leftToRight = ObjectAnimator.ofFloat(paperPiece, View.TRANSLATION_X,
                Math.random().toFloat() * (screenWidth + 300 - (-screenWidth + 300) + 1) + (-screenWidth + 300),
                random.nextInt((screenWidth - paperWidth).toInt()).toFloat())
        // object animator for rotating the paper about the x-axis
        val rotatorX = ObjectAnimator.ofFloat(paperPiece, View.ROTATION_X, 0f, random.nextFloat() * 1500)
        // object animator for rotating the paper piece about the y-axis
        val rotatorY = ObjectAnimator.ofFloat(paperPiece, View.ROTATION_Y, 0f, random.nextFloat() * 1500)
        // object animator for rotating the paper about the z-axis
        val rotator = ObjectAnimator.ofFloat(paperPiece, View.ROTATION, 0f, random.nextFloat() * 360)
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