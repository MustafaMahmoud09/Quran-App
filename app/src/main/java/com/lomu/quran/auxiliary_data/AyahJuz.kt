package com.lomu.quran.auxiliary_data


data class AyahJuz(val number:Int,val audio:String,val audioSecondary:List<String>,val text:String,val surah:TitleSurah,val numberInSurah:Int,val juz:Int,val manzil:Int,val page:Int,val ruku:Int,val hizbQuarter:Int,val sajda:Any)