package com.lomu.quran.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

abstract class BaseFragment(private val idLayout:Int) :Fragment() {//end class BaseFragment

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return layoutInflater.inflate(idLayout,container,false)

    }//end onCreateView()

    override fun onStart() {
        super.onStart()
        setup()
    }//end onResume()

    abstract fun setup()

}