package com.lomu.quran.activity

import android.os.Bundle
import android.util.Log
import com.lomu.quran.R
import com.lomu.quran.auxiliary_data.TitleSurah
import com.lomu.quran.adapter.AyahSurahRecycler
import com.lomu.quran.manager.Constant
import com.lomu.quran.manager.ManageData
import kotlinx.android.synthetic.main.activity_surah.*


class SurahActivity :BaseActivity(R.layout.activity_surah){
    private var data:TitleSurah?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initVariable()
        executeOperationAyahSurah()
    }

    override fun setup() {
        callback()
        surahWithoutBasmala()
    }

    private fun initVariable() {
       data= intent.getParcelableExtra(Constant.SURAH_DATA_INTENT)
    }

    private fun surahWithoutBasmala() {

        if(data?.number==9){
         basmalaId.text=""
        }
    }

    private fun executeOperationAyahSurah(){
           adapter()
           setData()

   }

    private fun adapter() {
            val nameSurah =
                data?.englishName
            recyclerShowAzkarId.adapter = AyahSurahRecycler(
                ManageData.getAyahSurah(),
                ManageData.getAyahSurahEnglish(),
                baseContext,
                nameSurah!!
            )

    }

    private fun callback() {
        btnBackZekr.setOnClickListener {
           try {
               finish()
           }catch (Ex:Exception){
               Log.d("TAG",Ex.message.toString())
           }
           }
    }

    private fun setData() {
        nameZekrId.text=data?.name
        englishNameTranslateSurahFirstId.text=data?.englishNameTranslation
    }
}