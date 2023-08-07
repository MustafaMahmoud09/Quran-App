package com.lomu.quran.activity


import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import com.google.gson.Gson
import com.lomu.quran.R
import com.lomu.quran.data.AllAzkar
import com.lomu.quran.manager.Constant
import com.lomu.quran.manager.DMLSqlite
import com.lomu.quran.manager.ManageData
import com.lomu.quran.manager.ServerRequest
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.android.synthetic.main.activity_start.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.concurrent.TimeUnit


class StartActivity :BaseActivity(R.layout.activity_start){
    private val compositeDisposable=CompositeDisposable()

    private val requestPermission=registerForActivityResult(ActivityResultContracts.RequestPermission(),::checkPerm)

    private fun checkPerm(check:Boolean){
        if(check){
            //define location here
            Toast.makeText(this,"yes",Toast.LENGTH_SHORT).show()

        }else{
           //here default location here
            Toast.makeText(this,"no",Toast.LENGTH_SHORT).show()

        }
    }//end checkPerm

    override fun onCreate(savedInstanceState: Bundle?) {

        modeApp()
        super.onCreate(savedInstanceState)
        startRequestPerm()
        getDataAzkar()
        selectMyListFromLocalDatabase()

    }//end onCreate

    private fun startRequestPerm() {

        requestPermission.launch(android.Manifest.permission.ACCESS_COARSE_LOCATION)

    }//end startRequestPerm

    override fun setup() {
        callback()

    }//end setup()

    private fun getDataAzkar() {

        val data=getDataAzkarFromJsonFile()
        val gson=Gson()
        val dataFormat=gson.fromJson(data,AllAzkar::class.java)
        ManageData.setAzkar(dataFormat.data)

    }

    private fun getDataAzkarFromJsonFile(): String {

       val dataAsset=assets.open("adhkar.json")
       val isr=InputStreamReader(dataAsset)
       val buffer=BufferedReader(isr)

       return buffer.readText()
    }


    private fun modeApp() {

        val shared=getSharedPreferences(Constant.NAME_SHARED_MODE_APP, MODE_PRIVATE)
        val valueMode=shared.getBoolean(Constant.NAME_MODE_ITEM_SHARED,false)

        if(valueMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        }//end modeApp

    private fun selectMyListFromLocalDatabase() {

        DMLSqlite.selectMyListFromLocalDatabase(baseContext)

    }//end selectMyListFromLocalDatabase

    private fun collectDataFromServer() {
        if(ServerRequest.checkConnection(baseContext)) {

            ServerRequest.requestTitleSurah(this)
            ServerRequest.requestTimePrayers()

        }else{

            Toast.makeText(baseContext,getString(R.string.not_connected),Toast.LENGTH_SHORT).show()

        }
    }//end collectDataFromServer

    private fun callback() {

        getStartBtnId.setOnClickListener {

                collectDataFromServer()
                intentToMainActivity()

        }

    }//end callback

   private fun intentToMainActivity(){

       val observable=Observable.interval(100,TimeUnit.MILLISECONDS)
       observable.subscribe(::onNext,::onError).add(compositeDisposable)

   }//end intentToMainActivity

   private fun onNext(num:Long){
       if(ManageData.getSurahTitle().size>0&&ManageData.getDatePrayer().size>0) {
               if(COUNT==0) {
                   val intent = Intent(this, MainActivity::class.java)
                   startActivity(intent)
                   finish()
                   compositeDisposable.dispose()
                   COUNT++
               }
       }
       }//end onNext

    private fun onError(throwable: Throwable){

    }//end onError

    private fun Disposable.add(compositeDisposable: CompositeDisposable) {

      compositeDisposable.add(this)

    }//end add


    override fun onDestroy() {

        super.onDestroy()
        COUNT=0

    }//end onDestroy


    companion object{

        private var COUNT=0

    }//end companion object
}//end class StartActivity


