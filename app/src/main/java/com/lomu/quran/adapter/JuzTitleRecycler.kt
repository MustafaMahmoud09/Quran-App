package com.lomu.quran.adapter

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.lomu.quran.R
import com.lomu.quran.activity.JuzActivity
import com.lomu.quran.data.Juz
import com.lomu.quran.manager.ManageData
import com.lomu.quran.manager.ServerRequest
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import java.util.concurrent.TimeUnit

class JuzTitleRecycler(private val list:List<Juz>,private val context:Context) :RecyclerView.Adapter<JuzTitleRecycler.ViewHolder>() {

    private val compositeDisposable=CompositeDisposable()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val nameJuzArabic:TextView=view.findViewById(R.id.nameArabicJuz)
        val numberJuz:TextView=view.findViewById(R.id.numberJuzItemId)
        val parentItem:ConstraintLayout=view.findViewById(R.id.parentJuzId)

    }//end class ViewHolder

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)= ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.juz_title_item,parent,false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        setTextInTextView(holder,position)
        onClickOnItem(holder,position)
    }//end onBindViewHolder()

    private fun onClickOnItem(holder: ViewHolder, position: Int) {
      holder.parentItem.setOnClickListener {
          getDataFromServer(position)
          executeIntent()
      }
    }//end onClickOnItem

    private fun getDataFromServer(position: Int){
        ManageData.removeAyahJuz()

        if(ServerRequest.checkConnection(context)) {

            ServerRequest.requestJuz(position + 1,context)

        }else{

            Toast.makeText(context,context.getString(R.string.not_connected),Toast.LENGTH_SHORT).show()

        }
    }//end getDataFromServer

   private fun executeIntent(){
       val observable=Observable.interval(100,TimeUnit.MILLISECONDS)
       observable.subscribe(::onNext,::onError).add(compositeDisposable)
   }//end executeIntent

    private fun onNext(num:Long){
        if(ManageData.getAyahJuz().size>0) {
            intentToJuzActivity()
        }
    }//end onNext

    private fun intentToJuzActivity() {

        val intent=Intent(context,JuzActivity::class.java)
        intent.flags=FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
        compositeDisposable.dispose()
    }//end intentToJuzActivity
    private fun onError(throwable: Throwable){

    }

    private fun setTextInTextView(holder: ViewHolder, position: Int) {

        val item=list[position]

        holder.apply {

            nameJuzArabic.text=item.name
            numberJuz.text=item.number.toString()

        }
    }//end setTextInTextView

    override fun getItemCount()=list.size

}//end class SurahTitleRecycler

private fun Disposable.add(composite:CompositeDisposable){
    composite.add(this)
}//end extension function add