package com.lomu.quran.adapter

import android.content.Context
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
import com.lomu.quran.auxiliary_data.TitleSurah
import com.lomu.quran.manager.ManageData
import com.lomu.quran.manager.ServerRequest
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit

class SurahsSoundRecycler(private val list:LinkedList<TitleSurah>,private val context: Context):RecyclerView.Adapter<SurahsSoundRecycler.ViewHolder>() {

    var lastHolder:ViewHolder?=null

    class ViewHolder(item:View):RecyclerView.ViewHolder(item) {

     val nameArabic:TextView=item.findViewById(R.id.nameSoundArabicSurah)
     val nameTranslateEnglish:TextView=item.findViewById(R.id.nameSoundEnglishTranslateId)
     val nameEnglish:TextView=item.findViewById(R.id.nameSoundEnglishId)
     val btnStartSound:AppCompatImageView=item.findViewById(R.id.btnStartSoundId)

    }//end ViewHolder

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)= ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_in_recycler_sound,parent,false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       setText(holder,position)
       clickItem(holder,position)
       checkBtnStartOrNo(holder,position)
    }//end onBindViewHolder

    private fun setText(holder:ViewHolder, position: Int) {

        val item=list[position]

        holder.apply {

            nameArabic.text=item.name
            nameTranslateEnglish.text=item.englishNameTranslation
            nameEnglish.text=item.englishName

        }

    }//end setText

    private fun checkBtnStartOrNo(holder:ViewHolder, position: Int) {
    if(position==ManageData.positionSurahStartSound){
         lastHolder=holder
         holder.btnStartSound.setImageDrawable(context.getDrawable(R.drawable.pause))

    }else{

        holder.btnStartSound.setImageDrawable(context.getDrawable(R.drawable.start))

    }
    }//end checkBtnStartOrNo

    private fun clickItem(holder:ViewHolder, position: Int) {
        holder.apply {
            btnStartSound.setOnClickListener {

                resumeSound(holder,position)

            }
        }

    }//end clickItem

    private fun resumeSound(holder: ViewHolder, position: Int) {

        correctValueAfterOperationOnSound()

        if (position != ManageData.positionSurahStartSound||ManageData.checkStopSurah) {

                      startSurah(position, holder)

        } else {

                  stopSurah(holder)

        }
        }//end resumeSound

    private fun correctValueAfterOperationOnSound() {
        ManageData.removeAyahSound()

        if (ManageData.checkDispose) {
            if (ManageData.mediaPlayerSurahComplete.isPlaying) {
                ManageData.mediaPlayerSurahComplete.pause()
            }
            if (ManageData.disposable!!.isDisposed) {
                ManageData.disposable!!.dispose()
            }
        }

        if (ManageData.mediaPlayer.isPlaying) {
            ManageData.mediaPlayer.stop()
        }
    }//end correctValueAfterOperationOnSound()

    private fun startSurah(
        position: Int,
        holder: ViewHolder
    ) {

        if(ManageData.positionSurahStartSound!=-1) {
            lastHolder?.btnStartSound?.setImageDrawable(context.getDrawable(R.drawable.start))
        }

        if (ManageData.positionSurahStartSound != position) {
            ManageData.numAyah = 0
        }

        lastHolder = holder
        ManageData.positionSurahStartSound = position
        val numItem = list[position].number

        ServerRequest.requestAyah(numItem, "ar.alafasy", "", context, false)

        ManageData.disposable =
            Observable.timer(2000, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io())
                .subscribe({ onNext(holder) }, {})

    }//end startSurah

    private fun stopSurah(holder: ViewHolder) {

        holder.btnStartSound.setImageDrawable(context.getDrawable(R.drawable.start))
        ManageData.checkDispose = false
        ManageData.checkStopSurah = true

    }//end stopSurah

    private fun onNext(holder: ViewHolder){

        ManageData.checkDispose=true

        if(ManageData.getAyahSound().size>0){
            holder.apply {

                btnStartSound.setImageDrawable(context.getDrawable(R.drawable.pause))

            }

            var count=ManageData.numAyah

            if(ManageData.checkStopSurah){

                ManageData.mediaPlayerSurahComplete.start()
                count++

            }

            ManageData.checkStopSurah = false
            while ( count< ManageData.getAyahSound().size) {

                 if(ManageData.mediaPlayerSurahComplete.isPlaying){
                    continue
                  }

                        prepareAndStartMediaPlayer(count)
                        count++
                        ManageData.numAyah=count-1

                if(ManageData.getAyahSound().size==0){
                    break
                }

            }

        }else{
            Toast.makeText(context,context.getString(R.string.not_connected),Toast.LENGTH_SHORT).show()
        }

    }//end onNext

    private fun prepareAndStartMediaPlayer(count: Int) {

        ManageData.mediaPlayerSurahComplete = MediaPlayer()
        ManageData.mediaPlayerSurahComplete.setAudioStreamType(AudioManager.STREAM_MUSIC)
        ManageData.mediaPlayerSurahComplete.setDataSource(ManageData.getAyahSound()[count].audio)
        ManageData.mediaPlayerSurahComplete.prepare()
        ManageData.mediaPlayerSurahComplete.start()

    }//end prepareAndStartMediaPlayer

    override fun getItemCount()=list.size
}//end SurahsSoundRecycler