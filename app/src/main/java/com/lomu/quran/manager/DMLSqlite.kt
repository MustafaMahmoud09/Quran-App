package com.lomu.quran.manager

import android.content.ContentValues
import android.content.Context
import com.lomu.quran.auxiliary_data.TitleSurah
import com.lomu.quran.local_database.MyListSqLiteHelper
import java.util.*

object DMLSqlite {

    private var myListSqLiteHelperObj:MyListSqLiteHelper?= null

    private val contentValue = ContentValues()

    fun selectMyListFromLocalDatabase(context:Context) {
       ManageData.removeAllMyList()

        myListSqLiteHelperObj= MyListSqLiteHelper(context)

        val resultSet= myListSqLiteHelperObj!!.readableDatabase.rawQuery("SELECT * FROM ${Constant.TABLE_SURAH_NAME} ORDER BY ${Constant.COLUMN_NUMBER_SURAH}",arrayOf())
        while (resultSet.moveToNext()){
            val titleSurah= TitleSurah(resultSet.getInt(0),resultSet.getString(1)
                ,resultSet.getString(2),resultSet.getString(3)
                ,resultSet.getInt(4),resultSet.getString(5))

            ManageData.setMyList(titleSurah)

        }

    }

    fun insetDataSurahInSqLiteTable(position:Int,context: Context){

        myListSqLiteHelperObj= MyListSqLiteHelper(context)

        val item=ManageData.getSurahTitle()[position]

        contentValue.apply {
            put(Constant.COLUMN_NUMBER_SURAH, item.number)
            put(Constant.COLUMN_NAME_SURAH,item.name)
            put(Constant.COLUMN_ENGLISH_NAME_SURAH,item.englishName)
            put(Constant.COLUMN_ENGLISH_NAME_TRANSLATE_SURAH,item.englishNameTranslation)
            put(Constant.COLUMN_NUMBER_OF_AYAHS_SURAH,item.numberOfAyahs)
            put(Constant.COLUMN_REVELATION_TYPE_SURAH,item.revelationType)
        }

        myListSqLiteHelperObj!!.writableDatabase?.insert(
            Constant.TABLE_SURAH_NAME,
            null,
            contentValue
        )
    }

    fun executeDeleteItem(position: Int,context: Context) {

        myListSqLiteHelperObj= MyListSqLiteHelper(context)


        val numberSurah=ManageData.getMyList()[position].number

        myListSqLiteHelperObj!!.writableDatabase.delete(Constant.TABLE_SURAH_NAME,"${Constant.COLUMN_NUMBER_SURAH}=?", arrayOf("$numberSurah"))

    }

    fun selectSurahFromSqlLiteTable(context:Context) {

        myListSqLiteHelperObj= MyListSqLiteHelper(context)

        val resultSet=myListSqLiteHelperObj!!.readableDatabase.rawQuery("SELECT * FROM ${Constant.TABLE_SURAH_NAME} ORDER BY ${Constant.COLUMN_NUMBER_SURAH}", arrayOf())

        while (resultSet.moveToNext()){

            TitleSurah(resultSet.getInt(0),resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),resultSet.getInt(4),resultSet.getString(5)).addToMyList(ManageData.newMyList)

        }

    }

    private fun TitleSurah.addToMyList(list: LinkedList<TitleSurah>){
        list.add(this)
    }

}

