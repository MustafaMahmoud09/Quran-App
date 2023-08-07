package com.lomu.quran.manager

import android.content.Context
import android.net.ConnectivityManager
import com.google.gson.Gson
import com.lomu.quran.auxiliary_data.PrayerDate
import com.lomu.quran.data.*
import okhttp3.*
import java.io.IOException

object ServerRequest {

    private val client= OkHttpClient()
    private var request:Request?=null
    private val gson= Gson()

    fun checkConnection(context:Context):Boolean{
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val nInfo = cm.activeNetworkInfo
        return nInfo != null && nInfo.isAvailable && nInfo.isConnected
    }

    fun requestAyah(position: Int,pathLanguage:String,obj:String,context: Context,typeList:Boolean) {
        try { request= Request.Builder().url("https://api.alquran.cloud/v1/surah/${position}/$pathLanguage").build()}catch (ex:Exception){}
        client.newCall(request!!).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {

            }

            override fun onResponse(call: Call, response: Response) {
                val res=response.body!!.string()
                if(obj=="SurahEnglish"){
                    val data = gson.fromJson(res, SurahEnglish::class.java)
                    ManageData.setAyahSurahEnglish(data.data.ayahs)
                }else{
                    val data=gson.fromJson(res, CompleteSurah::class.java)
                    if(typeList) {
                        ManageData.setAyahSurah(data.data.ayahs)
                    }else{
                        ManageData.setAyahSound(data.data.ayahs)
                    }
               }
            }

        })
    }

    fun requestJuz(numJuz:Int,context: Context){

        try { request=Request.Builder().url("https://api.alquran.cloud/v1/juz/$numJuz/ar.alafasy").build()}catch (ex:Exception){}

        client.newCall(request!!).enqueue(object :Callback{

            override fun onFailure(call: Call, e: IOException) {

            }

            override fun onResponse(call: Call, response: Response) {

                val data=response.body!!.string()
                val dataFormat= gson.fromJson(data,DataJuz::class.java)
                ManageData.setAyahJuz(dataFormat.data.ayahs)

            }

        })

    }

     fun requestTitleSurah(context: Context){
         try { request=Request.Builder().url("https://api.alquran.cloud/v1/surah").build() }catch (ex:Exception){ }
            client.newCall(request!!).enqueue(object : Callback {

                override fun onFailure(call: Call, e: IOException) {

                }

                override fun onResponse(call: Call, response: Response) {


                        val data = response.body?.string()
                        val dataFormat = gson.fromJson(data, Surah::class.java)
                        ManageData.setSurahTitle(dataFormat.data)

                }
            })


    }//end requestTitleSurah()

    fun requestTimePrayers(){

        try { request = Request.Builder().url("https://api.aladhan.com/v1/timingsByAddress?address=Cairo").build() }catch (ex:Exception){ }
        client.newCall(request!!).enqueue(object :Callback{

            override fun onFailure(call: Call, e: IOException) {


            }


            override fun onResponse(call: Call, response: Response) {
              val data=response.body!!.string()
              val dataFormat= gson.fromJson(data,Prayers::class.java)
              parsingDataPrayer(dataFormat)

            }

        })
 }

    private fun parsingDataPrayer(dataFormat: Prayers) {
        ManageData.setDateToday(dataFormat.data.date.readable,dataFormat.data.date.hijri.date)
        ManageData.removeDatePrayer()
        ManageData.setDatePrayer(PrayerDate("الفجر",dataFormat.data.timings.Fajr,"Fajr"))
        ManageData.setDatePrayer(PrayerDate("الضهر",dataFormat.data.timings.Dhuhr,"Dhuhr"))
        ManageData.setDatePrayer(PrayerDate("العصر",dataFormat.data.timings.Asr,"Asr"))
        ManageData.setDatePrayer(PrayerDate("المغرب",dataFormat.data.timings.Maghrib,"Maghrib"))
        ManageData.setDatePrayer(PrayerDate("العشاء",dataFormat.data.timings.Isha,"Isha"))
    }

}