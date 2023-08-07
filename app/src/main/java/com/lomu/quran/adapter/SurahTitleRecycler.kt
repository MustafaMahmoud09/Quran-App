package com.lomu.quran.adapter

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.lomu.quran.R
import com.lomu.quran.activity.SurahActivity
import com.lomu.quran.auxiliary_data.TitleSurah
import com.lomu.quran.manager.Constant
import com.lomu.quran.manager.DMLSqlite
import com.lomu.quran.manager.ManageData
import com.lomu.quran.manager.ServerRequest
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import java.util.*
import java.util.concurrent.TimeUnit

class SurahTitleRecycler(private val list:LinkedList<TitleSurah>,private val context: Context,private val typeFragment:Boolean) : RecyclerView.Adapter<SurahTitleRecycler.ViewHolder>() {


    private var msg:String?=null
    private var yes:String?=null
    private var no:String?=null

    private val compositeDisposable=CompositeDisposable()
    private var dialog :AlertDialog.Builder?=null
    private var createDialog:Dialog?=null

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val nameEnglish: TextView = view.findViewById(R.id.nameSoundEnglishId)
        val nameEnglishTranslate: TextView = view.findViewById(R.id.nameSoundEnglishTranslateId)
        val nameArabic: TextView = view.findViewById(R.id.nameSoundArabicSurah)
        val number: TextView = view.findViewById(R.id.btnStartSoundId)
        val parent: ConstraintLayout = view.findViewById(R.id.parentSurahTitleItemId)
        val image: AppCompatImageView = view.findViewById(R.id.imageRvelationType)

    }//end class ViewHolder

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.surah_title_item, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        setup(holder, position)

    }//end onBindViewHolder()

    private fun setup(holder: ViewHolder, position: Int) {
        setTextToTextView(holder, position)
        clickOnItemInList(holder, position)
    }//end setup

    private fun clickOnItemInList(holder: ViewHolder, position: Int) {

        holder.parent.setOnClickListener {

            getDataSurah(position)
            executeIntent(position)
        }

        holder.parent.setOnLongClickListener {
            codeLongClickOnParentItem(position)
            true
        }

    }//end clickOnItemInList

    private fun codeLongClickOnParentItem(position: Int) {
        if (typeFragment) {
            createDialogAdd(position)
        }else{
          createDialogDelete(position)
        }
    }//end codeLongClickOnParentItem

    private fun createDialogDelete(position: Int) {
       msg=context.getString(R.string.dialog_delete)
       yes=context.getString(R.string.yes)
       no=context.getString(R.string.no)

       dialog=AlertDialog.Builder(context)

       dialog!!.setMessage(msg)

       dialog!!.setPositiveButton(yes){_,_->

           codeDeleteFromMyList(position)

       }

       dialog!!.setNegativeButton(no){_,_->


       }

       createDialog=dialog!!.create()

       createDialog!!.show()
    }//end create createDialogDelete

    private fun codeDeleteFromMyList(position: Int) {
        DMLSqlite.executeDeleteItem(position,context)
        DMLSqlite.selectSurahFromSqlLiteTable(context)
        notifyChangByDiffUtil(position)
    }//end codeDeleteFromMyList


    private fun notifyChangByDiffUtil(position: Int) {

            ManageData.newMyList.removeAll(ManageData.newMyList)
            ManageData.removeMyListItem(position)
            this.notifyDataSetChanged()


    }//end notifyChangByDiffUtil

    private fun createDialogAdd(position: Int) {
         msg=context.getString(R.string.dialog_add)
         yes=context.getString(R.string.yes)
         no=context.getString(R.string.no)

         dialog = AlertDialog.Builder(context)

         dialog!!.setMessage(msg)

         dialog!!.setPositiveButton(yes){_,_->
            codeInsertData(position)
         }

         dialog!!.setNegativeButton(no){_,_->

        }

        createDialog = dialog!!.create()
        createDialog!!.show()

    }//end createDialogAdd

    private fun codeInsertData(position:Int){
        DMLSqlite.insetDataSurahInSqLiteTable(position,context)
        DMLSqlite.selectSurahFromSqlLiteTable(context)
        checkDifferentBetweenBeforeAndAfter()
    }//end codeInsertData


    private fun checkDifferentBetweenBeforeAndAfter() {

        ManageData.setAllMyList(ManageData.newMyList)
        ManageData.newMyList.removeAll(ManageData.newMyList)

    }//end checkDifferentBetweenBeforeAndAfter

    private fun getDataSurah(position: Int) {
        ManageData.removeAyahSurah()
        ManageData.removeAyahSurahEnglish()
             if(ServerRequest.checkConnection(context)) {
                 ServerRequest.requestAyah(list[position].number, "en.asad", "SurahEnglish",context,true)
                 ServerRequest.requestAyah(list[position].number, "ar.alafasy", "",context,true)
             }else{
               Toast.makeText(context,context.getString(R.string.not_connected),Toast.LENGTH_SHORT).show()
             }
    }//end getDataSurah

    private fun executeIntent(position: Int){
        val observable=Observable.interval(100,TimeUnit.MILLISECONDS)
        observable.subscribe({ onNext(position) },::onError).add(compositeDisposable)
    }//end executeIntent

    private fun onNext(position: Int){
        if(ManageData.getAyahSurah().size>0&& ManageData.getAyahSurahEnglish().size>0) {
           intentToSurah(position)
        }
    }//end onNext

    private fun intentToSurah(position: Int) {

        val intent = Intent(context, SurahActivity::class.java)
        intent.flags = FLAG_ACTIVITY_NEW_TASK
        intent.putExtra(Constant.SURAH_DATA_INTENT, list[position])
        context.startActivity(intent)
        compositeDisposable.dispose()

    }//end intentToSurah

    private fun onError(throwable: Throwable){

    }//end onError

    private fun setTextToTextView(holder: ViewHolder, position: Int) {
        val item=list[position]
        holder.apply {

            number.text = "${position+1}"
            nameArabic.text = item.name
            nameEnglish.text = item.englishName
            nameEnglishTranslate.text=item.englishNameTranslation

            if(item.revelationType=="Meccan"){

                    image.setImageDrawable(context.getDrawable(R.drawable.kaaba))

            }else{

                image.setImageDrawable(context.getDrawable(R.drawable.madina))

            }
        }
        }//end setTextToTextView

    override fun getItemCount()=list.size


}//end class SurahTitleRecycler

private fun Disposable.add(compositeDisposable: CompositeDisposable) {
    compositeDisposable.add(this)
}//end extension function add
