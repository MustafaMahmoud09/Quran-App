package com.lomu.quran.manager

import android.media.MediaPlayer
import androidx.fragment.app.Fragment
import com.lomu.quran.auxiliary_data.*
import com.lomu.quran.data.Juz
import com.lomu.quran.data.Prayers
import com.lomu.quran.fragment.JuzFragment
import com.lomu.quran.fragment.MyListFragment
import com.lomu.quran.fragment.SurahsFragment
import io.reactivex.rxjava3.disposables.Disposable
import java.util.*

object ManageData {
    var url=""
    var check=true
    var count=0
    var numAyah=0
    var checkStopSurah=false
    var checkDispose=false
    var disposable: Disposable?=null
    var positionSurahStartSound=-1
    var mediaPlayer= MediaPlayer()
    var mediaPlayerSurahComplete=MediaPlayer()
    val newMyList = LinkedList<TitleSurah>()
    private val listSound=LinkedList<AyahSurah>()
    private val ayahJuz= LinkedList<AyahJuz>()
    private val ayahSurah= LinkedList<AyahSurah>()
    private val prayers= LinkedList<Prayers>()
    private val myList=LinkedList<TitleSurah>()
    private var dateToDay:DateToDay?=null
    private val surahTitle= LinkedList<TitleSurah>()
    private val ayahSurahEnglish=LinkedList<AyahSurahEnglish>()
    private val datePrayer=LinkedList<PrayerDate>()
    private val azkarList=LinkedList<Azkar>()
    private val juz=listOf(Juz(1,"الجزء الأول"),Juz(2,"الجزء الثاني"),Juz(3,"الجزء الثالت")
        ,Juz(4,"الجزء الرابع"),Juz(5,"الجزء الخامس"),Juz(6,"الجزء السادس")
        ,Juz(7,"الجزء السابع"),Juz(8,"الجزء الثامن"),Juz(9,"الجزء التاسع")
        ,Juz(10,"الجزء العاشر"),Juz(11,"الجزء الحادي عشر"),Juz(12,"الجزء الثاني عشر")
        ,Juz(13,"الجزء الثالث عشر"),Juz(14,"الجزء الرابع عشر"),Juz(15,"الجزء الخامس عشر")
        ,Juz(16,"الجزء السادس عشر"),Juz(17,"الجزء السابع عشر"),Juz(18,"الجزء الثامن عشر")
        ,Juz(19,"الجزء التاسع عشر"),Juz(20,"الجزء العشرون"),Juz(21,"الجزء الحادي والعشرون")
        ,Juz(22,"الجزء الثاني والعشرون"),Juz(23,"الجزء الثالث والعشرون"),Juz(24,"الجزء الرابع والعشرون")
        ,Juz(25,"الجزء الخامس والعشرون"),Juz(26,"الجزء السادس والعشرون"),Juz(27,"الجزء السابع والعشرون")
        ,Juz(28,"الجزء الثامن والعشرون"),Juz(29,"الجزء التاسع والعشرون"),Juz(30,"الجزء الثلاثون"))
    val setAyahJuz:(LinkedList<AyahJuz>)->Unit={

        ayahJuz.addAll(it)
    }//end setAyahJuz
    val removeAyahJuz:()->Unit={
        ayahJuz.removeAll(ayahJuz)
    }
    val setAyahSurah:(LinkedList<AyahSurah>)->Unit={
       ayahSurah.addAll(it)
    }//end setAyahSurah

    val setAyahSurahEnglish:(LinkedList<AyahSurahEnglish>)->Unit={
        ayahSurahEnglish.addAll(it)
    }

    val setAzkar:(LinkedList<Azkar>)->Unit={
        azkarList.removeAll(azkarList)
        azkarList.addAll(it)
    }//end setAzkar

    val removeAyahSurah:()->Unit={
        ayahSurah.removeAll(ayahSurah)
    }//end setAyahSurah

    val removeAyahSurahEnglish:()->Unit={
        ayahSurahEnglish.removeAll(ayahSurahEnglish)
    }
   val removeDatePrayer:()->Unit={
       datePrayer.removeAll(datePrayer)
   }

    val setPrayers:(LinkedList<Prayers>)->Unit={
        prayers.addAll(it)
    }//end setPrayersTitle

    val setSurahTitle:(LinkedList<TitleSurah>)->Unit={
        surahTitle.removeAll(surahTitle)
        surahTitle.addAll(it)
    }//end setSurahTitle

    val setMyList:(TitleSurah)->Unit={
        myList.add(it)
    }
    val removeAllMyList:()->Unit={
        myList.removeAll(myList)
    }
    val setAllMyList:(LinkedList<TitleSurah>)->Unit={
        myList.removeAll(myList)
        myList.addAll(it)
    }
    val removeMyListItem:(Int)->Unit={
        myList.removeAt(it)
    }

    val setDateToday:(String,String)->Unit={timeM,timeH->
       dateToDay= DateToDay(timeM,timeH)
    }
    val setDatePrayer:(PrayerDate)->Unit={
        datePrayer.add(it)
    }
    val setAyahSound:(LinkedList<AyahSurah>)->Unit={
        listSound.addAll(it)
    }
    val removeAyahSound:()->Unit={
        listSound.removeAll(listSound)
    }
    fun getAyahSound()= listSound
    fun getDatePrayer()= datePrayer
    fun getDatToDay()= dateToDay

    fun getAyahJuz()= ayahJuz

    fun getAyahSurah()=ayahSurah

    fun getJuzTitle()= juz

    fun getPrayers()=prayers

    fun getSurahTitle()=surahTitle
    fun getMyList()= myList
    fun getAzkar()= azkarList

    fun getAyahSurahEnglish()= ayahSurahEnglish
    fun getFragmentList()=listOf<Fragment>(SurahsFragment(),JuzFragment(),MyListFragment())

}//end ManageData

