package com.lomu.quran.activity


import android.os.Bundle
import com.lomu.quran.R
import com.lomu.quran.adapter.AyahJuzRecycler
import com.lomu.quran.manager.ManageData
import kotlinx.android.synthetic.main.activity_juz.*

class JuzActivity :BaseActivity(R.layout.activity_juz) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapterInJuz()
    }

    override fun setup() {

               callBack()

    }

    private fun adapterInJuz() {
            recyclerAyahJuzId.adapter = AyahJuzRecycler(ManageData.getAyahJuz(), this)
        }

    private fun callBack() {
        btnJuzBack.setOnClickListener {
            finish()
        }
    }
}