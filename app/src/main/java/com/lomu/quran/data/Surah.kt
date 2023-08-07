package com.lomu.quran.data

import com.lomu.quran.auxiliary_data.TitleSurah
import java.util.*

data class Surah(val code:Int,val status:String,val data:LinkedList<TitleSurah>)
