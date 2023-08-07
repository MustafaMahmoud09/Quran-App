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
import com.lomu.quran.auxiliary_data.AyahJuz
import com.lomu.quran.manager.Constant
import com.lomu.quran.manager.ManageData
import com.lomu.quran.manager.ServerRequest
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit

class AyahJuzRecycler(private val list:LinkedList<AyahJuz>,private val context:Context): RecyclerView.Adapter<AyahJuzRecycler.ViewHolder>() {

    private var lastHolder: ViewHolder?=null
    private var soundPausePosition=-1

    private var lastHolderSaved: ViewHolder?=null
    private var checkLastSave=false

    abstract class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    open var nameSurahArabic:TextView?=null
    open var nameSurahEnglish:TextView?=null
    open var basmalaText:TextView?=null
    abstract var numberAyahInSurah:TextView
    abstract var soundBtn:AppCompatImageView
    abstract var btnSaveAyah:AppCompatImageView
    abstract var textAyah:TextView
    }//end class ViewHolder
    class ViewHolderDesignOne(item:View): ViewHolder(item){
       override var basmalaText: TextView?=item.findViewById(R.id.basmalaJuzId)
       override var nameSurahArabic:TextView?=item.findViewById(R.id.arabicNameSurahInJuzId)
       override var nameSurahEnglish:TextView?=item.findViewById(R.id.englishNameTranslateSurahInJuzId)
       override var numberAyahInSurah:TextView=item.findViewById(R.id.numberAyahSurahInJuzId)
       override var soundBtn:AppCompatImageView=item.findViewById(R.id.soundInJuzBtn)
       override var btnSaveAyah:AppCompatImageView=item.findViewById(R.id.saveInJuz)
       override var textAyah:TextView=item.findViewById(R.id.ayahInSurahInJuzArabic)
    }
    class ViewHolderDesignTwo(item:View): ViewHolder(item){
        override var numberAyahInSurah:TextView=item.findViewById(R.id.numberZekrId)
        override var soundBtn:AppCompatImageView=item.findViewById(R.id.soundBtn)
        override var btnSaveAyah:AppCompatImageView=item.findViewById(R.id.shareBtn)
        override var textAyah:TextView=item.findViewById(R.id.zekrTextId)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)= run {
        when(viewType){
            DESIGN_AYAH_ONE ->{
                ViewHolderDesignOne(LayoutInflater.from(parent.context).inflate(R.layout.first_ayah_in_surah,parent,false))

            }
            else->{
                ViewHolderDesignTwo(LayoutInflater.from(parent.context).inflate(R.layout.other_ayahs_in_surah,parent,false))
            }
        }

    }

    override fun getItemViewType(position: Int)=run{
        val item=list[position]

        when(item.numberInSurah){
            1-> DESIGN_AYAH_ONE
            else-> DESIGN_OTHER_AYAH
        }

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when(holder){
            is ViewHolderDesignOne ->{
                setDataSurah(holder,position)
                surahWithoutBasmal(holder,position)
            }
            else->{
              setIconSaveAyah(holder,position)
            }
        }
        setDataAyah(holder,position)
        callbackMethodClick(holder,position)
        callbackMethodComplete()
        setIconInSoundBtn(holder,position)
    }//end onBindViewHolder()

    private fun setIconSaveAyah(holder: ViewHolder, position: Int) {
        val item = list[position]

        val shared =
            context.getSharedPreferences(Constant.SHARED_NAME_AYAH_SAVED, Context.MODE_PRIVATE)

        val numberAyahSavedBefore = shared.getInt(Constant.NUMBER_AYAH_SAVED, -1)
        val nameSurahSavedBefore = shared.getString(Constant.NAME_SURAH_SAVED, "")

        if (item.numberInSurah == numberAyahSavedBefore && nameSurahSavedBefore == item.surah.englishName) {

            holder.btnSaveAyah.setImageDrawable(context.getDrawable(R.drawable.bookmark))
            lastHolderSaved = holder
            checkLastSave = true

        } else {

            holder.btnSaveAyah.setImageDrawable(context.getDrawable(R.drawable.save))

        }
    }

    private fun setIconInSoundBtn(holder: ViewHolder, position: Int) {
        if(position!=soundPausePosition){
            holder.soundBtn.setImageDrawable(context.getDrawable(R.drawable.start))
        }else{
            holder.soundBtn.setImageDrawable(context.getDrawable(R.drawable.pause))
        }
    }

    private fun surahWithoutBasmal(holder: ViewHolderDesignOne, position: Int) {
        if(list[position].surah.number==9){
            holder.basmalaText?.text=""
        }
    }

    private fun callbackMethodComplete() {
        ManageData.mediaPlayer.setOnCompletionListener {
            condeSoundCompletion()
        }
    }

    private fun condeSoundCompletion() {
        lastHolder?.soundBtn?.setImageDrawable(context.getDrawable(R.drawable.start))
        ManageData.check =false
        soundPausePosition=-1
    }

    private fun callbackMethodClick(holder: ViewHolder, position: Int) {
     holder.btnSaveAyah.setOnClickListener {
         saveAyah(holder,position)
     }
     holder.soundBtn.setOnClickListener {
         codeBtnSound(holder,position)
     }
    }

    private fun codeBtnSound(holder: ViewHolder, position: Int) {
        ManageData.removeAyahSound()
        ManageData.positionSurahStartSound = -1

        if (ManageData.checkDispose) {
            if (ManageData.mediaPlayerSurahComplete.isPlaying) {
                ManageData.mediaPlayerSurahComplete.stop()
            }
            if (ManageData.disposable!!.isDisposed) {
                ManageData.disposable!!.dispose()
            }
        }

        if (ServerRequest.checkConnection(context)) {
            if (ManageData.url.isEmpty()) {
                soundPausePosition = position
                ManageData.apply {
                    url = list[position].audio
                    Observable.timer(1, TimeUnit.MILLISECONDS).observeOn(Schedulers.io())
                        .subscribe(::onNext, ::onError)
                }
                lastHolder = holder
                holder.soundBtn.setImageDrawable(context.getDrawable(R.drawable.pause))
            } else {
                if (ManageData.url == list[position].audio) {
                    ManageData.check = if (ManageData.check) {
                        ManageData.mediaPlayer.pause()
                        holder.soundBtn.setImageDrawable(context.getDrawable(R.drawable.start))
                        soundPausePosition = -1
                        false
                    } else {
                        ManageData.mediaPlayer.start()
                        holder.soundBtn.setImageDrawable(context.getDrawable(R.drawable.pause))
                        soundPausePosition = position
                        true
                    }
                } else {
                    ManageData.apply {
                        check = true
                        soundPausePosition = position
                        mediaPlayer.stop()
                        mediaPlayer = MediaPlayer()
                        url = list[position].audio
                        Observable.timer(2, TimeUnit.MILLISECONDS).observeOn(Schedulers.io())
                            .subscribe(::onNext, ::onError)
                    }
                    lastHolder?.soundBtn?.setImageDrawable(context.getDrawable(R.drawable.start))
                    holder.soundBtn.setImageDrawable(context.getDrawable(R.drawable.pause))
                    lastHolder = holder
                }
            }
        } else {
            Toast.makeText(context, context.getString(R.string.not_connected), Toast.LENGTH_SHORT)
                .show()
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
    private fun saveAyah(holder: ViewHolder, position: Int) {
        val item=list[position]

        val shared =
            context.getSharedPreferences(Constant.SHARED_NAME_AYAH_SAVED, Context.MODE_PRIVATE)
        val edit = shared.edit()

        val numberAyahSavedBefore=shared.getInt(Constant.NUMBER_AYAH_SAVED,-1)
        val nameSurahSavedBefore=shared.getString(Constant.NAME_SURAH_SAVED,"")

        if(numberAyahSavedBefore!=item.numberInSurah||nameSurahSavedBefore!=item.surah.englishName){

            setDataInShared(edit,"${item.numberInSurah} - ${item.surah.englishName}",item.numberInSurah-1, item.surah.englishName!!)
            holder.btnSaveAyah.setImageDrawable(context.getDrawable(R.drawable.bookmark))

            if(checkLastSave){

                if(lastHolderSaved!=holder) {
                    lastHolderSaved?.btnSaveAyah?.setImageDrawable(context.getDrawable(R.drawable.save))
                }

            }

            lastHolderSaved=holder
            checkLastSave=true

        }else{

            setDataInShared(edit,"",-1,"")
            holder.btnSaveAyah.setImageDrawable(context.getDrawable(R.drawable.save))

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
        edit.putString(Constant.NAME_SURAH_SAVED, nameSurah)

    }

    private fun setDataAyah(holder: ViewHolder, position: Int) {
    val item=list[position]
    holder.numberAyahInSurah.text=item.numberInSurah.toString()
    holder.textAyah.text=item.text
    }

    private fun setDataSurah(holder: ViewHolder, position: Int){
        val item=list[position].surah
        holder.nameSurahArabic?.text=item.name
        holder.nameSurahEnglish?.text=item.englishNameTranslation
    }

    companion object{
        const val DESIGN_AYAH_ONE=1
        const val DESIGN_OTHER_AYAH=2
    }

    override fun getItemCount()=list.size
}//end class AyahJuzRecycler