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
    /**
     *  Height and Width of the drawable which will be used for positioning the paper piece
     *  at the start and end of the animation.
     */
    var mPaperHeight = 0f
    var mPaperWidth = 0f

    /**
     * Drawable used as a paper piece. This drawable can be replace upon developers choice.
     */
    var mDrawable = ContextCompat.getDrawable(context, R.drawable.paper_piece)!!

    init {
        mPaperHeight = convertDpToPixel(mDrawable.intrinsicHeight.toFloat(), context)
        mPaperWidth = convertDpToPixel(mDrawable.intrinsicWidth.toFloat(), context)
    }

    /**
     * Handler variable used for executing the runnables that will stop or start the shower.
     */
    var mShowerTaskHandler = Handler(Looper.getMainLooper())

    /**
     * Runnable variable used for starting the shower.
     */
    var mStartShowerRunnable = object : Runnable {
        override fun run() {
            performShower()
            // the following line of code will execute this runnable every 50ms recursively
            // this is the backbone of this library.
            // a paper piece is added to the screen and then animated very randomly so that it reaches
            // the bottom when this process is repeated every 50ms a shower is achieved.
            mShowerTaskHandler.postDelayed(this, 50)
        }
    }

    /**
     * Runnable for stopping the shower.
     */
    var mStopShowerRunnable: Runnable = Runnable { stopShower() }

    /**
     * Elevation of the paper pieces.
     */
    var mElevation = 30F

    /**
     * This function is responsible for animating the paper pieces.
     */
    private fun performShower() {
        // checking screen width and height for calculation required for creating animation
        val screenWidth = parent.width.toFloat()
        val screenHeight = parent.height.toFloat()

        // this is the imageView that will be dynamically added to the screen
        val paperPiece: ImageView = AppCompatImageView(context)
        paperPiece.setImageDrawable(mDrawable)
        paperPiece.elevation =
            mElevation// elevating the paper pieces so that they appear over the dialog.

        // array of colors in the form of strings. These colors will be applied to the
        // paper pieces randomly. Different color for each paper piece is not really significant
        // but it gives a really better look.
        val colorArray = arrayOf("#ffb49b", "#ffa990", "#ffc5ab", "#ffa38a")

        // setting random color to the paper piece only if the default drawable is used
        if (mDrawable == ContextCompat.getDrawable(context, R.drawable.paper_piece)) {
//          a random number from 0 to 3 is generated and the element at that index in the colorArray
//          is used as a color for the incoming paper piece.
            paperPiece.setColorFilter(Color.parseColor(colorArray[(Math.random() * (3 + 1) + 0).toInt()]))
        }
        // An instance of the Random class used for generating pseudo-random numbers for random animation
        val random = Random()
        // object animator for moving the paper from up to down
        val upToDown = ObjectAnimator.ofFloat(
            paperPiece, View.TRANSLATION_Y,
            -mPaperHeight - 40, screenHeight + mPaperHeight
        )
        // object animator for moving the paper piece from left to right
        val leftToRight = ObjectAnimator.ofFloat(
            paperPiece, View.TRANSLATION_X,
            Math.random()
                .toFloat() * (screenWidth + 300 - (-screenWidth + 300) + 1) + (-screenWidth + 300),
            random.nextInt((screenWidth - mPaperWidth).toInt()).toFloat()
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
        // playing the rotation animation and translation motion altogether at a time
        setOfShowerAnimation.playTogether(upToDown, rotator, rotatorX, rotatorY, leftToRight)

//      the animation time is also randomly assigned so that paper pieces fall at different speed and
//      different time

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

    /**
     * This function starts the shower. Don't forget to call stopShower() when the activity pauses
     * @onPause
     */
    fun startShower() {
        mShowerTaskHandler.post(mStartShowerRunnable)
    }

    /**
     * This function starts the shower and runs it for given time duration.
     * @param runningTime is the time duration in milliseconds for which the animation will run.
     */
    fun startShower(runningTime: Long) {
        mShowerTaskHandler.post(mStartShowerRunnable)
        mShowerTaskHandler.postDelayed(mStopShowerRunnable, runningTime)
    }

    /**
     * This function will stop the shower when called.
     */
    fun stopShower() {
        mShowerTaskHandler.removeCallbacks(mStartShowerRunnable)
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