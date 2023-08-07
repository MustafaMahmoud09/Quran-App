package com.lomu.quran.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.lomu.quran.R
import com.lomu.quran.fragment.AzkarFragment
import com.lomu.quran.fragment.PrayersFragment
import com.lomu.quran.fragment.QuranFragment
import com.lomu.quran.fragment.SoundFragment
import com.lomu.quran.manager.Constant
import com.lomu.quran.manager.ManageData
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity :BaseActivity(R.layout.activity_main){
    private var fragment:Int?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        codeMain()
    }

    private fun codeMain() {
        if(ManageData.count==0){
               selectFragment(QuranFragment())
               toolMainId.title = getString(R.string.app_name)
               fragment=1
               ManageData.count++
        }
    }

    override fun setup() {
        callback()
        createNavInTool()
        navMainId.setNavigationItemSelectedListener {
            codeSelectMode(it)
            true
        }
    }//end setup()

    private fun codeSelectMode(it: MenuItem) {
     when(it.itemId){
         R.id.app_bar_switch->{
             changeMode()
         }
     }
    }

    private fun changeMode() {
     val sharedMode=getSharedPreferences(Constant.NAME_SHARED_MODE_APP, MODE_PRIVATE)
     val edit=sharedMode.edit()
     val getMode=sharedMode.getBoolean(Constant.NAME_MODE_ITEM_SHARED,false)
     if(getMode){
         edit.putBoolean(Constant.NAME_MODE_ITEM_SHARED,false)
         AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
     }else{
         edit.putBoolean(Constant.NAME_MODE_ITEM_SHARED,true)
         AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
     }
     edit.apply()

    }

    private fun createNavInTool() {
        val showNavInToolbar = ActionBarDrawerToggle(this, drawerMainId, toolMainId, R.string.open, R.string.close)
        drawerMainId.addDrawerListener(showNavInToolbar)
        showNavInToolbar.syncState()
    }
    private fun callback() {

        bottomNavigationSelectCategory.setOnItemSelectedListener {
            codeBottomNavigationSelectCategory(it)
        }

    }

    private fun codeBottomNavigationSelectCategory(it: MenuItem): Boolean {
        when (it.itemId) {
            R.id.mushafId -> {
                executeSelectFragmentFinally(QuranFragment(),getString(R.string.app_name),1)
            }
            R.id.prayersId -> {
                executeSelectFragmentFinally(PrayersFragment(),getString(R.string.prayer_times),2)
            }
            R.id.soundId->{
              executeSelectFragmentFinally(SoundFragment(),getString(R.string.sound),3)
            }
            R.id.azkarId->{
                executeSelectFragmentFinally(AzkarFragment(),getString(R.string.azkar),4)
            }
        }
        return true
    }
    private fun executeSelectFragmentFinally(fragmentObj:Fragment,title:String,fragmentNum:Int){
        selectFragment(fragmentObj)
        toolMainId.title=title
        fragment=fragmentNum
    }

    private fun selectFragment(fragment:Fragment){
            val trans = supportFragmentManager.beginTransaction()
            trans.replace(R.id.parentFragment, fragment)
            trans.commit()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        when (fragment) {
            1 -> {
                outState.putInt(NAME_FRAGMENT_STATE,1)
            }
            2 -> {
                outState.putInt(NAME_FRAGMENT_STATE,2)
            }
            3 -> {
                outState.putInt(NAME_FRAGMENT_STATE,3)
            }
            else->{
                outState.putInt(NAME_FRAGMENT_STATE,4)
            }
        }

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        when (savedInstanceState.getInt(NAME_FRAGMENT_STATE)) {
            1 -> {
                executeSelectFragmentFinally(QuranFragment(),getString(R.string.app_name),1)
            }
            2 -> {
                executeSelectFragmentFinally(PrayersFragment(),getString(R.string.prayer_times),2)
            }
            3 -> {
                executeSelectFragmentFinally(SoundFragment(),getString(R.string.sound),3)
            }
            else->{
                executeSelectFragmentFinally(AzkarFragment(),getString(R.string.azkar),4)
            }
        }
        ManageData.count++
    }

    override fun onBackPressed() {
        if(drawerMainId.isDrawerOpen(GravityCompat.START)){
         drawerMainId.closeDrawer(GravityCompat.START)
        }else{
            super.onBackPressed()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        ManageData.count=0
    }
    companion object{
        const val NAME_FRAGMENT_STATE="simpleName"
    }

}//end class MainActivity