package com.lomu.quran.local_database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.lomu.quran.manager.Constant

class MyListSqLiteHelper(context: Context):SQLiteOpenHelper(context,Constant.DATA_BASE_NAME,null,version){
    override fun onCreate(database: SQLiteDatabase?) {
        val databaseCreated="CREATE TABLE ${Constant.TABLE_SURAH_NAME}(${Constant.COLUMN_NUMBER_SURAH} INTEGER PRIMARY KEY," +
                "${Constant.COLUMN_NAME_SURAH} VARCHAR(80),${Constant.COLUMN_ENGLISH_NAME_SURAH} VARCHAR(80)," +
                "${Constant.COLUMN_ENGLISH_NAME_TRANSLATE_SURAH} VARCHAR(80),${Constant.COLUMN_NUMBER_OF_AYAHS_SURAH} INTEGER," +
                "${Constant.COLUMN_REVELATION_TYPE_SURAH} VARCHAR(80))"

                database?.execSQL(databaseCreated)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

    companion object{
        var version:Int=1
    }
}