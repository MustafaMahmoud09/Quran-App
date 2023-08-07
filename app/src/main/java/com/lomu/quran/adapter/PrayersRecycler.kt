package com.lomu.quran.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lomu.quran.R
import com.lomu.quran.auxiliary_data.PrayerDate
import java.util.*

class PrayersRecycler(private val list:LinkedList<PrayerDate>) : RecyclerView.Adapter<PrayersRecycler.ViewHolder>() {

    class ViewHolder(view:View):RecyclerView.ViewHolder(view) {
    val nameArabic:TextView=view.findViewById(R.id.namePrayerArabicId)
    val nameEnglish:TextView=view.findViewById(R.id.namePrayerEnglishId)
    val timePrayer:TextView=view.findViewById(R.id.timePrayerId)
    }//end class ViewHolder

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)= ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_prayer_in_recycler,parent,false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    setData(holder,position)
    }//end onBindViewHolder()

    private fun setData(holder: PrayersRecycler.ViewHolder, position: Int) {
        val item=list[position]
        holder.apply {
            nameArabic.text = item.nameArabic
            nameEnglish.text=item.nameEnglish
            timePrayer.text=item.time
        }
    }//end setData

    override fun getItemCount()=list.size

}//end class RecyclerPrayers
