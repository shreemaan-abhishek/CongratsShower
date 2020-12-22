package com.dopedevx.congratsshower

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.dopedevx.congratsshowerlibrary.CongratsShower

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val congratsButton = findViewById<Button>(R.id.congrats)
        val dialog = findViewById<View>(R.id.dialog)
        congratsButton.setOnClickListener {
            val shower = CongratsShower(findViewById(R.id.root), this)

//            You can also set drawables on your own. Setting drawables here will replace the paper_piece.xml
//            drawable with the drawable that you set.
//
//            shower.mDrawable = ContextCompat.getDrawable(this,R.drawable.ic_launcher_background)!!

            shower.startShower(10000)
            congratsButton.visibility = View.INVISIBLE
            dialog.visibility = View.VISIBLE
        }
    }
}