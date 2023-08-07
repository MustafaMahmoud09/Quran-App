package com.lomu.quran.adapter

import android.content.Context
import android.content.SharedPreferences
import android.media.AudioManager
import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.lomu.quran.R
import com.lomu.quran.auxiliary_data.AyahSurah
import com.lomu.quran.auxiliary_data.AyahSurahEnglish
import com.lomu.quran.manager.Constant
import com.lomu.quran.manager.ManageData
import com.lomu.quran.manager.ServerRequest
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit

class AyahSurahRecycler(
    private val listArabic: LinkedList<AyahSurah>,
    private val listEnglish: LinkedList<AyahSurahEnglish>,
    private val baseContext: Context,
    private val nameSurah: String
): RecyclerView.Adapter<AyahSurahRecycler.ViewHolder>() {

     private var lastHolder: ViewHolder?=null
     private var soundPausePosition=-1
     private var lastHolderSaved:ViewHolder?=null
     private var checkLastSave=false

     class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
     val numAyah:TextView=view.findViewById(R.id.numberZekrId)
     val textArabic:TextView=view.findViewById(R.id.zekrTextId)
     val saveBtn: AppCompatImageView =view.findViewById(R.id.shareBtn)
     val soundBtn:AppCompatImageView=view.findViewById(R.id.soundBtn)
    }//end class ViewHolder

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)= ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.other_ayahs_in_surah,parent,false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    setText(holder,position)
    onClick(holder,position)
    completeSound()
    setIconInSoundBtn(holder,position)
    setIconSaveAyah(holder,position)
    }//end onBindViewHolder()

    private fun setIconSaveAyah(holder:ViewHolder, position: Int) {
        val shared =
            baseContext.getSharedPreferences(Constant.SHARED_NAME_AYAH_SAVED, Context.MODE_PRIVATE)

        val numberAyahSavedBefore=shared.getInt(Constant.NUMBER_AYAH_SAVED,-1)
        val nameSurahSavedBefore=shared.getString(Constant.NAME_SURAH_SAVED,"")

        if(position+1==numberAyahSavedBefore&&nameSurahSavedBefore==nameSurah){

            holder.saveBtn.setImageDrawable(baseContext.getDrawable(R.drawable.bookmark))
            lastHolderSaved=holder
            checkLastSave=true

        }else{

            holder.saveBtn.setImageDrawable(baseContext.getDrawable(R.drawable.save))

        }

    }

    private fun setIconInSoundBtn(holder: ViewHolder, position: Int) {
        if(position!=soundPausePosition){
            holder.soundBtn.setImageDrawable(baseContext.getDrawable(R.drawable.start))
        }else{
            holder.soundBtn.setImageDrawable(baseContext.getDrawable(R.drawable.pause))
        }
    }

    private fun completeSound() {

        ManageData.mediaPlayer.setOnCompletionListener {

            codeComplete()
        }

    }

    private fun codeComplete() {
        lastHolder?.soundBtn?.setImageDrawable(baseContext.getDrawable(R.drawable.start))
        ManageData.check = false
       soundPausePosition=-1
    }

    private fun onClick(holder: ViewHolder, position: Int) {
     holder.apply {
         saveBtn.setOnClickListener {
             codeSaveLastAyah(holder,position)
         }

         soundBtn.setOnClickListener {
          /*
          *lastUrl
          *if lastUrl is not empty:
          *if: lastUrl == url pause
          *else: stop ,start to new url
          *else: start to url
          **/
          codeClickSound(holder,position)
         }
     }

    }
    private fun codeClickSound(holder: ViewHolder, position:Int){
        ManageData.removeAyahSound()
        ManageData.positionSurahStartSound=-1

        if(ManageData.checkDispose){
            if(ManageData.mediaPlayerSurahComplete.isPlaying) {
                ManageData.mediaPlayerSurahComplete.stop()
            }
            if(ManageData.disposable!!.isDisposed) {
                ManageData.disposable!!.dispose()
            }
        }
        if(ServerRequest.checkConnection(baseContext)){
            if(ManageData.url.isEmpty()){
                soundPausePosition=position
                ManageData.apply {
                    url = listArabic[position].audio
                    Observable.timer(1, TimeUnit.MILLISECONDS).observeOn(Schedulers.io()).subscribe(::onNext,::onError)
                }
                lastHolder=holder
                holder.soundBtn.setImageDrawable(baseContext.getDrawable(R.drawable.pause))
            }else{
            if(ManageData.url ==listArabic[position].audio){
                ManageData.check = if(ManageData.check) {
                    ManageData.mediaPlayer.pause()
                    holder.soundBtn.setImageDrawable(baseContext.getDrawable(R.drawable.start))
                    soundPausePosition=-1
                    false
                }else{
                    ManageData.mediaPlayer.start()
                    holder.soundBtn.setImageDrawable(baseContext.getDrawable(R.drawable.pause))
                    soundPausePosition=position
                    true
                }
            }else {
              ManageData.apply {
                    soundPausePosition=position
                    check = true
                    mediaPlayer.stop()
                    mediaPlayer = MediaPlayer()
                    url = listArabic[position].audio
                    Observable.timer(1, TimeUnit.MILLISECONDS).observeOn(Schedulers.io()).subscribe(::onNext,::onError)
                }
                lastHolder?.soundBtn?.setImageDrawable(baseContext.getDrawable(R.drawable.start))
                holder.soundBtn.setImageDrawable(baseContext.getDrawable(R.drawable.pause))
                lastHolder=holder
            }}
        }else {
            Toast.makeText(baseContext,baseContext.getString(R.string.not_connected),Toast.LENGTH_SHORT).show()
             }
 }

    private fun onNext(it:Long){
        ManageData.mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        ManageData.mediaPlayer.setDataSource(ManageData.url)
        ManageData.mediaPlayer.prepare()
        ManageData.mediaPlayer.start()
    }

    private fun onError(throwable: Throwable){

    }
    private fun codeSaveLastAyah(holder: ViewHolder,position: Int) {

        val shared =
            baseContext.getSharedPreferences(Constant.SHARED_NAME_AYAH_SAVED, Context.MODE_PRIVATE)
        val edit = shared.edit()

        val numberAyahSavedBefore=shared.getInt(Constant.NUMBER_AYAH_SAVED,-1)
        val nameSurahSavedBefore=shared.getString(Constant.NAME_SURAH_SAVED,"")

        if(numberAyahSavedBefore!=position+1||nameSurahSavedBefore!=nameSurah){

            setDataInShared(edit,"${position + 1} - $nameSurah",position, nameSurah)
            holder.saveBtn.setImageDrawable(baseContext.getDrawable(R.drawable.bookmark))

            if(checkLastSave){

                if(lastHolderSaved!=holder) {
                    lastHolderSaved?.saveBtn?.setImageDrawable(baseContext.getDrawable(R.drawable.save))
                }

            }

            lastHolderSaved=holder
            checkLastSave=true

        }else{

            setDataInShared(edit,"",-1,"")
            holder.saveBtn.setImageDrawable(baseContext.getDrawable(R.drawable.save))

        }

            edit.apply()
    }

    private fun setDataInShared(
        edit: SharedPreferences.Editor,
        text: String,
        position: Int,
        nameSurah: String
    ) {

        edit.putString(Constant.DATA_AYAH_SAVED,text )
        edit.putInt(Constant.NUMBER_AYAH_SAVED, position + 1)
        edit.putString(Constant.NAME_SURAH_SAVED, this.nameSurah)

    }

    private fun setText(holder: ViewHolder, position: Int) {
      val itemArabic=listArabic[position]
      holder.apply {
          numAyah.text=itemArabic.numberInSurah.toString()
          textArabic.text=itemArabic.text
      }
    }

    override fun getItemCount()=listArabic.size

}//end class AyahSurahRecycler