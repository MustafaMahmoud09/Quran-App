package com.lomu.quran.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity(private val idLayout:Int):AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(idLayout)

    }//end onCreate

    override fun onResume() {

        super.onResume()

         setup()

    }//end onResume()

    abstract fun setup()

}//end class BaseActivity()