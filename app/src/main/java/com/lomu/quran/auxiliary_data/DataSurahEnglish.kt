package com.lomu.quran.auxiliary_data

import java.util.*

data class DataSurahEnglish(val number:Int,val name :String,val englishName:String,val englishNameTranslation:String,val revelationType:String,val numberOfAyahs:Int,val ayahs:LinkedList<AyahSurahEnglish>)